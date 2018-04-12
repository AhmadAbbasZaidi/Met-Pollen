package com.cfp.metpollen.view.customViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cfp.metpollen.R;
import com.cfp.metpollen.data.db.model.ForecastModel;
import com.cfp.metpollen.view.adapters.SineGraphViewAdapter;
import com.cfp.metpollen.view.interfaces.GraphViewListener;
import com.cfp.metpollen.view.objectModels.RecyclerViewObject;
import com.cfp.metpollen.view.objectModels.WeatherCondition;
import com.cfp.metpollen.view.utilities.SunRiseSETUtils;
import com.cfp.metpollen.view.utilities.TempConditionUtils;
import com.cfp.metpollen.view.utilities.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AhmedAbbas on 11/8/2017.
 */

public class SineGraphView extends RecyclerView implements GraphViewListener {

    private static boolean firsttime = true;
    private static boolean apiCalled = false;
    private static boolean first = true;
    private static int z = 0;
    private static int prevCenterPos;
    private static int prevOffset = 0;
    private static SineGraphViewAdapter adapter1;
    private static RecyclerViewObject object;
    private static View ballView;
    private static List<Point> list;
    private static int resourceId = 0;
    private static List<ForecastModel> forecastModelList;
    private int itemWidth, itemHeight;
    private Paint paint;
    private Path path;
    private DisplayMetrics displayMetrics;
    private int pixelSize = 0;
    private int iCurStep = 0;
    private LinearLayoutManager manager;
    private LinearSnapHelper snapHelper;
    private boolean allowed = true;
    private Canvas can;
    private float xTranslate, yTranslate = 0, minus = 0;
    private int startPosition = 0, endPosition = 4;
    private int scrollOffset = 0;
    private int visibleItemCount = 0;
    private int cenPosition = 0;
    private int m = 0;
    private int startPos, centerPos, centerPos2, endPos;
    private int dxX, dyY = 0;
    private GraphViewListener graphViewListener;
    private Paint linePaint;
    private int marginTop = 50;
    private int centerPoint;
    private TextView tempView;
    private TextView conditionName;
    private ImageView conditionImage;
    private TextView datetime;
    private TextView chanceOfRain;
    private final OnScrollListener onScrollListener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            /*switch (newState) {
                case RecyclerView.SCROLL_STATE_IDLE:
                    new android.os.Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            swipeLayout.setEnabled(true);
                        }
                    }, 300);
                    break;
                case RecyclerView.SCROLL_STATE_DRAGGING:
                    swipeLayout.setEnabled(false);
                    break;
                case RecyclerView.SCROLL_STATE_SETTLING:
                    swipeLayout.setEnabled(false);
                    break;

            }*/
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            dxX = dx;
            dyY = dy;

            int center = getWidth() / 2;
            int center2 = getWidth() / 2 - itemWidth / 2;
            int start = 0;
            int end = getWidth();

            View startView = findChildViewUnder(start, getTop());
            startPos = getChildAdapterPosition(startView);

            View centerView = findChildViewUnder(center, getTop());
            centerPos = getChildAdapterPosition(centerView);

            View centerView2 = findChildViewUnder(center2, getTop());
            centerPos2 = getChildAdapterPosition(centerView2);

            LinearLayoutManager layoutManager = ((LinearLayoutManager) getLayoutManager());
            int pos = layoutManager.findLastCompletelyVisibleItemPosition();
            int posSt = layoutManager.findFirstCompletelyVisibleItemPosition();

            View endView = findChildViewUnder(end, getTop());
            endPos = getChildAdapterPosition(endView);

            scrollOffset = computeHorizontalScrollOffset();
            Log.i("scrollOffset", "" + scrollOffset);

            if (firsttime && object.getVisibleItemCount() == 0) {
                int diff = endPos - startPos;
                Log.i("**diff** ", "= " + diff);
                object.setVisibleItemCount(3);//diff % 2 == 0 ? diff + 1 : diff);
                Log.i("**object.VisCount** ", "= " + object.getVisibleItemCount());
//                prevCenterPos=centerPos;
                smoothScrollToPosition(diff);

/*
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        setVisibility(VISIBLE);
                    }
                }, 100);
*/
            }

//            Log.i("startPos ", " =  " + startPos);
//            Log.i("centerPos ", " =  " + centerPos);
//            Log.i("endPos ", " =  " + endPos);
//            Log.i("pos ", " =  " + pos);
//            Log.i("dx ", " =  " + dx);
//            Log.i("dy ", " =  " + dy);
//            Log.i("scrollOffset ", " =  " + scrollOffset);


            /*highlighting center views*/
            if (prevCenterPos != centerPos) {
                // dehighlight the previously highlighted view
                View prevView = getLayoutManager().findViewByPosition(prevCenterPos);
                if (prevView != null) {
                    View button = prevView.findViewById(R.id.convertView);
                    button.setBackgroundColor(Color.TRANSPARENT);
                    TextView tv = (TextView) button.findViewById(R.id.tv);
                    if (tv != null) {
//                        tv.setTextColor(Color.WHITE);
                        tv.setTextColor(getResources().getColor(R.color.white_pollen_text));
                    }
                    ImageView iv = (ImageView) button.findViewById(R.id.cloud);
                    if (iv != null) {
//                        iv.setColorFilter(Color.WHITE);
                        iv.setColorFilter(getResources().getColor(R.color.white_pollen_text));
                    }
                    View ivLine = button.findViewById(R.id.line);

                    if (ivLine != null) {
//                        ivLine.setBackgroundColor(Color.WHITE);
                        ivLine.setBackground(getResources().getDrawable(R.drawable.graph_vertical_line));
                    }
                }

                // highlight view in the middle
                if (centerView != null) {
                    View button = centerView.findViewById(R.id.convertView);
                    TextView tv = (TextView) button.findViewById(R.id.tv);
                    updateViews(forecastModelList.get(centerPos - object.getVisibleItemCount() / 2), centerPos - object.getVisibleItemCount() / 2);

                    if (tv != null) {
//                        tv.setTextColor(Color.WHITE);
//                        tv.setTextColor(Color.MAGENTA);
                        tv.setTextColor(getResources().getColor(android.R.color.holo_orange_light));
                    }
                    ImageView iv = (ImageView) button.findViewById(R.id.cloud);
                    if (iv != null) {
//                        iv.setColorFilter(Color.WHITE);
//                        iv.setColorFilter(Color.MAGENTA);
                        iv.setColorFilter(getResources().getColor(android.R.color.holo_orange_light));
                    }
                    View ivLine = button.findViewById(R.id.line);

                    if (ivLine != null) {
//                        ivLine.setBackgroundColor(Color.WHITE);
//                        ivLine.setBackgroundColor(Color.MAGENTA);
                        ivLine.setBackground(getResources().getDrawable(R.drawable.graph_vertical_line_selected));
                    }
//                        button.setBackgroundColor(Color.LTGRAY);
                }

                prevCenterPos = centerPos;
            } else {

                /*highlighting when initially list is loaded*/
                if (centerView != null) {
                    View button = centerView.findViewById(R.id.convertView);
                    TextView tv = (TextView) button.findViewById(R.id.tv);
                    updateViews(forecastModelList.get(centerPos - object.getVisibleItemCount() / 2), centerPos - object.getVisibleItemCount() / 2);

                    if (tv != null) {
//                        tv.setTextColor(Color.WHITE);
//                        tv.setTextColor(Color.MAGENTA);
                        tv.setTextColor(getResources().getColor(android.R.color.holo_orange_light));
                    }
                    ImageView iv = (ImageView) button.findViewById(R.id.cloud);
                    if (iv != null) {
//                        iv.setColorFilter(Color.WHITE);
//                        iv.setColorFilter(Color.MAGENTA);
                        iv.setColorFilter(getResources().getColor(android.R.color.holo_orange_light));
                    }
                    View ivLine = button.findViewById(R.id.line);

                    if (ivLine != null) {
//                        ivLine.setBackgroundColor(Color.WHITE);
//                        ivLine.setBackgroundColor(Color.MAGENTA);
                        ivLine.setBackground(getResources().getDrawable(R.drawable.graph_vertical_line_selected));
                    }

//                        button.setBackgroundColor(Color.LTGRAY);
                }
            }

            invalidate();
        }
    };
    /*@Override
    public boolean fling(int velocityX, int velocityY) {

        velocityX *= 0.3;
        // velocityX *= 0.7; for Horizontal recycler view. comment velocityY line not require for Horizontal Mode.

        return super.fling(velocityX, velocityY);
    }*/
    private SwipeRefreshLayout swipeLayout;
    private View sunRiseSetBackgroundLayout;
    private int item = 0;
    private int previousScrollIndex = 0;
    private int alpha2 = 0;
    private int sunRiseSetColor;

    public SineGraphView(Context context) {
        super(context);

        initializePaint();
        addOnScrollListener(onScrollListener);
        setLayoutManager(null);
    }

    public SineGraphView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initializePaint();
        addOnScrollListener(onScrollListener);
        setLayoutManager(null);
    }

    public void setSwipeLayout(SwipeRefreshLayout swipeLayout) {
        this.swipeLayout = swipeLayout;
    }

    public void setDatetime(TextView datetime) {
        this.datetime = datetime;
    }

    public void setConditionName(TextView conditionName) {
        this.conditionName = conditionName;
    }

    public void setConditionImage(ImageView conditionImage) {
        this.conditionImage = conditionImage;
    }

    private void updateViews(ForecastModel forecastModel, int i) {
//        String tmp = object.getListOriginalTemp().get(centerPos - object.getVisibleItemCount() / 2).y < 10 ? "0" + object.getListOriginalTemp().get(centerPos - object.getVisibleItemCount() / 2).y : object.getListOriginalTemp().get(centerPos - object.getVisibleItemCount() / 2).y + "";
        String tmp = i % 2 == 0 ? (forecastModel.getMaxTemperature().equalsIgnoreCase("--")?"--":(int)Float.parseFloat(forecastModel.getMaxTemperature()) < 10 ? "0" + (int)Float.parseFloat(forecastModel.getMaxTemperature()) : (int)Float.parseFloat(forecastModel.getMaxTemperature()) + "") : (forecastModel.getMinTemperature().equalsIgnoreCase("--")?"--":(int)Float.parseFloat(forecastModel.getMinTemperature()) < 10 ? "0" + (int)Float.parseFloat(forecastModel.getMinTemperature()) : (int)Float.parseFloat(forecastModel.getMinTemperature()) + "");
//        String tmp = forecastModel.getTemperature() < 10 ? "0" + (int) forecastModel.getTemperature() : (int) forecastModel.getTemperature()+"";
        tempView.setText(tmp + "°");
        datetime.setText(utils.getTemperatureDate(forecastModel.getFormattedWeatherTime()));
        WeatherCondition weatherCondition = TempConditionUtils.getConditionForecast(forecastModel.getPrecipitation(), forecastModel.getCloud(), SunRiseSETUtils.isDayForecast(forecastModel.getFormattedHour(), forecastModel.getSunRiseHour(), forecastModel.getSunRiseMin(), forecastModel.getSunSetHour(), forecastModel.getSunSetMin()));
        if(weatherCondition!=null) {
            conditionName.setText(weatherCondition.getCondition());
            conditionImage.setImageResource(weatherCondition.getResourceId());
        }
        else
        {
            conditionName.setText("--");
        }

        String prec = forecastModel.getPrecipitation().equalsIgnoreCase("--")?"--":(int)Float.parseFloat(forecastModel.getPrecipitation()) < 10 ? "0" + (int)Float.parseFloat(forecastModel.getPrecipitation()) : (int)Float.parseFloat(forecastModel.getPrecipitation()) + "";
        chanceOfRain.setText(prec + "mm");

    }

    private void initializePaint() {
        displayMetrics = getResources().getDisplayMetrics();

        itemWidth = displayMetrics.widthPixels / 2;

        /*new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        */

        /*paint for horizontal bottom line*/

        linePaint = new Paint();
        linePaint.setAntiAlias(false);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(2);
        linePaint.setColor(Color.YELLOW);

        /*paint for  graph fill*/
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(5);
        paint.setShader(new LinearGradient(0, 0, 0, 120 * getResources().getDisplayMetrics().density, 0x80FFFFFF, 0x00FFFFFF, Shader.TileMode.CLAMP));
        if (graphViewListener == null) {
            graphViewListener = this;
        }
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public void setItemWidth(int itemWidth) {
        this.itemWidth = dptopx(itemWidth);
    }

    public void setItemHeight(int itemHeight) {
        this.itemHeight = dptopx(itemHeight);
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {

        /*setting horizontal as deffault orientation*/
        if (manager == null) {
            manager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            super.setLayoutManager(manager);
        }

        /*setting snap to nearest scrolled view*/
        if (snapHelper == null) {
//            snapHelper = new GravitySnapHelper(Gravity.START);
            snapHelper = new LinearSnapHelper();
            snapHelper.attachToRecyclerView(this);
            super.setOnFlingListener(snapHelper);
        }
    }

    public void setPoints(final List<ForecastModel> forecastList, final List<Point> pointsList, final int resourceId, final View ballView) {

        setLayerType(RecyclerView.LAYER_TYPE_SOFTWARE, null);
        setVisibility(INVISIBLE);
        ballView.setVisibility(INVISIBLE);
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
        /*setting rendering to software*/

                if (!firsttime)
                    apiCalled = true;

                if (object == null) {
                    object = new RecyclerViewObject(getContext(), resourceId, null, null, ballView);
                }

                SineGraphView.forecastModelList = new ArrayList<>();
                for (int i = 0; i < forecastList.size(); i++) {
                    SineGraphView.forecastModelList.add(forecastList.get(i));
                }

                SineGraphView.ballView = ballView;

        /*setting child views layout*/
                SineGraphView.resourceId = resourceId;

                pointsList.clear();
                for (int i = 0; i < SineGraphView.forecastModelList.size(); i++) {
                    if (i % 2 == 0) {
                        pointsList.add(new Point(i + 1,(int) Float.parseFloat(SineGraphView.forecastModelList.get(i).getMaxTemperature())));
                    } else {
                        pointsList.add(new Point(i + 1,(int) Float.parseFloat(SineGraphView.forecastModelList.get(i).getMinTemperature())));
                    }
                }

                List<Point> points = new ArrayList<>();
                List<Point> canvasPointsList = new ArrayList<>();
                int j = 0;

        /*adding items at start and end to start first and end last item at center*/
        /*points for adapter*/
        /*canvasPointsList for plotting graph on canvas*/

                for (int i = 0; i < pointsList.size() + (visibleItemCount / 2) + (visibleItemCount / 2); i++) {
                    if (i < (visibleItemCount / 2) || i > (pointsList.size() - 1) + visibleItemCount / 2) {
                        points.add(new Point(0, 0));
                    } else {
                        points.add(new Point(pointsList.get(j).x, pointsList.get(j).y));
                        canvasPointsList.add(new Point(getXValue(pointsList.get(j).x), pointsList.get(j).y));
                        j++;
                    }
                }
                SineGraphView.list = points;

                object.setListOriginalTemp(pointsList);
                object.setListCanvas(canvasPointsList);
                object.setListAdapter(points);
                object.setBallView(ballView);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean aVoid) {
//                super.onPostExecute(aVoid);
                Log.i("aVoid = ", aVoid == true ? "true" : "false" + "");
                if (aVoid) {
                    object.getBallView().setY((float) object.getListCanvas().get(0).y);
                    /*set layout*/
//                    setLayoutManager(null);
                    setAdapter(null);
                }
            }
        }.execute();

    }
/*
    public void setPoints(List<ForecastModel> forecastList, List<Point> pointsList, int resourceId, View ballView) {

//        setting rendering to software

        if (!firsttime)
            apiCalled = true;

        if (object == null) {
            object = new RecyclerViewObject(getContext(), resourceId, null, null, ballView);
        }

//        this.forecastModelList = forecastList;
        this.forecastModelList = new ArrayList<>();
        for (int i = 0; i < forecastList.size(); i++) {
            this.forecastModelList.add(forecastList.get(i));
        }

        this.ballView = ballView;

        setLayerType(RecyclerView.LAYER_TYPE_SOFTWARE, null);
        setVisibility(INVISIBLE);
//        set layout
        setLayoutManager(null);
//        setting child views layout
        this.resourceId = resourceId;

//        int currentHour = 12;//utils.getHourOfDay();
//        int reqHour = currentHour - (currentHour % 3);
//        if (reqHour == 0) {
//            reqHour = 12;
//        }
//        if (this.forecastModelList == null) {
//            this.forecastModelList = new ArrayList<>();
//        } else {
//            this.forecastModelList.clear();
//        }
//        for (ForecastModel model : forecastList) {
//            if (Integer.parseInt(model.getFormattedHour()) == reqHour) {
//                this.forecastModelList.add(model);
//                this.forecastModelList.add(model);
//                Log.i("Model Hour = ", model.getFormattedHour() + "");
//            }
//        }

        pointsList.clear();
        for (int i = 0; i < this.forecastModelList.size(); i++) {
            if (i % 2 == 0) {
                pointsList.add(new Point(i + 1, (int) this.forecastModelList.get(i).getMaxTemperature()));
            } else {
                pointsList.add(new Point(i + 1, (int) this.forecastModelList.get(i).getMinTemperature()));
            }
        }

        List<Point> points = new ArrayList<>();
        List<Point> canvasPointsList = new ArrayList<>();
        int j = 0;
//        adding items at start and end to start first and end last item at center
//        points for adapter
//        canvasPointsList for plotting graph on canvas

        for (int i = 0; i < pointsList.size() + (visibleItemCount / 2) + (visibleItemCount / 2); i++) {
            if (i < (visibleItemCount / 2) || i > (pointsList.size() - 1) + visibleItemCount / 2) {
                points.add(new Point(0, 0));
            } else {
                points.add(new Point(pointsList.get(j).x, pointsList.get(j).y));
                canvasPointsList.add(new Point(getXValue(pointsList.get(j).x), pointsList.get(j).y));
                j++;
            }
        }
        this.list = points;

        object.setListOriginalTemp(pointsList);
        object.setListCanvas(canvasPointsList);
        object.setListAdapter(points);
        object.setBallView(ballView);
        object.getBallView().setY((float) object.getListCanvas().get(0).y);
        setAdapter(null);

//        postDelayed(new Runnable() {
//            @Override
//            public void run() {
////                smoothScrollToPosition(10);
////                smoothScrollToPosition(0);
//            }
//        }, 200);
    }
*/

    public void setBallView(View ballView) {
        /*set floating view on curve*/
        this.ballView = ballView;
    }

    @Override
    public Adapter getAdapter() {
        return super.getAdapter();
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (graphViewListener == null) {
            graphViewListener = this;
        }
        if (adapter1 == null) {
            adapter1 = new SineGraphViewAdapter(forecastModelList, getContext(), resourceId, graphViewListener);
            /*postDelayed(new Runnable() {
                @Override
                public void run() {
                    setVisibility(VISIBLE);
                    ballView.setVisibility(VISIBLE);
                }
            }, 300);*/
            super.setAdapter(adapter1);
        } else {
            super.setAdapter(adapter1);
            /*postDelayed(new Runnable() {
                @Override
                public void run() {
                    setVisibility(VISIBLE);
                    ballView.setVisibility(VISIBLE);
                }
            }, 300);*/
            addOnScrollListener(onScrollListener);
            setLayoutManager(null);
        }

    }

    @Override
    public void addOnScrollListener(OnScrollListener listener) {
        super.addOnScrollListener(listener);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /*clear canvas*/
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.OVERLAY);

        if (object != null && object.getListCanvas() != null) {

            Log.i("objviscnt = ", object.getVisibleItemCount() + "");

            /*getting width in pixels of views added at left to move starting point to center*/
            int space = (object.getVisibleItemCount() / 2 * object.getItemWidth());

            path = new Path();
            Path path1 = new Path();
//            path.moveTo(space - scrollOffset, object.getItemHeight() * displayMetrics.density);
//            path.lineTo(space - scrollOffset, centerPoint);
//            path1.moveTo(object.getListCanvas().get(0).x - object.getItemWidth() / 2 + space - scrollOffset, centerPoint);
            path1.moveTo(space - scrollOffset, centerPoint);
//            path1.lineTo(object.getListCanvas().get(0).x+space - scrollOffset, centerPoint);

            path.moveTo(0, getHeight());
            path.lineTo(0 - scrollOffset, getHeight());
//            path.moveTo(0,getHeight());
//            path.lineTo(0 - scrollOffset,centerPoint);
//
            path.quadTo(space - object.getItemWidth() / 2 - scrollOffset, object.getListCanvas().get(1).y, object.getListCanvas().get(0).x - object.getItemWidth() / 2 + space - scrollOffset, centerPoint);
//            path.cubicTo(0 - scrollOffset, object.getListCanvas().get(1).y
//                    , object.getListCanvas().get(0).x - object.getItemWidth() / 2 + space - scrollOffset,object.getListCanvas().get(1).y, object.getListCanvas().get(0).x - object.getItemWidth() / 2 + space - scrollOffset, centerPoint);

            for (int a = 0; a < object.getListCanvas().size(); a++) {
                Point point = object.getListCanvas().get(a);

                path.cubicTo(object.getListCanvas().get(a).x - object.getItemWidth() / 2 + space - scrollOffset, centerPoint
                        , object.getListCanvas().get(a).x + space - scrollOffset, object.getListCanvas().get(a).y, object.getListCanvas().get(a).x + object.getItemWidth() / 2 + space - scrollOffset, centerPoint);
                path1.cubicTo(object.getListCanvas().get(a).x - object.getItemWidth() / 2 + space - scrollOffset, centerPoint
                        , object.getListCanvas().get(a).x + space - scrollOffset, object.getListCanvas().get(a).y, object.getListCanvas().get(a).x + object.getItemWidth() / 2 + space - scrollOffset, centerPoint);
            }
            path.quadTo(object.getListCanvas().get(object.getListCanvas().size() - 1).x + object.getItemWidth() + space - scrollOffset, object.getListCanvas().get(object.getListCanvas().size() - 2).y, object.getListCanvas().get(object.getListCanvas().size() - 1).x + object.getItemWidth() + object.getItemWidth() / 2 + space - scrollOffset, centerPoint);
//            path.quadTo(object.getListCanvas().get(object.getListCanvas().size() - 1).x + object.getItemWidth() + object.getItemWidth() / 2 + space - scrollOffset, centerPoint, object.getListCanvas().size()*object.getItemWidth()- object.getItemWidth()/2 + 2*space - scrollOffset, object.getListCanvas().get(object.getListCanvas().size()-1).y);
            path.lineTo(object.getListCanvas().get(object.getListCanvas().size() - 1).x + object.getItemWidth() + object.getItemWidth() + space - scrollOffset, getHeight());

//            path.cubicTo(object.getListCanvas().get(object.getListCanvas().size() - 1).x + object.getItemWidth() / 2 + space - scrollOffset, centerPoint
//                    , object.getListCanvas().get(object.getListCanvas().size() - 1).x + object.getItemWidth() + space - scrollOffset, object.getListCanvas().get(object.getListCanvas().size() - 2).y, object.getListCanvas().get(object.getListCanvas().size() - 1).x + object.getItemWidth() + object.getItemWidth() / 2 + space - scrollOffset, centerPoint);


//            path.quadTo(object.getListCanvas().get(object.getListCanvas().size() - 1).x + object.getItemWidth() + object.getItemWidth() / 2 + space - scrollOffset, centerPoint, object.getListCanvas().size()*object.getItemWidth()- object.getItemWidth()/2 + 2*space - scrollOffset, object.getListCanvas().get(object.getListCanvas().size()-1).y);

//            path.lineTo(object.getListCanvas().size()*object.getItemWidth()- object.getItemWidth() + 2*space - scrollOffset, getHeight());

            path.close();
//            path1.close();

            canvas.drawPath(path, paint);
//            canvas.drawPath(path1, linePaint);

            PathMeasure pm = new PathMeasure(path1, false);
            float n = (pm.getLength() / forecastModelList.size()) / (object.getItemWidth());
            float fSegmentLen = n/*pm.getLength() / (getWidth())*/;//pm.getLength() / 20;//we'll get 20 points from path to animate the circle
            float afP[] = {100f, 100f};

        /*setting points and drawing sun image */
//            if (iCurStep < fSegmentLen) {
            pm.getPosTan(fSegmentLen * scrollOffset + pm.getLength() / (2 * forecastModelList.size()), afP, null);
//            canvas.drawCircle(afP[0], afP[1], 30, linePaint);
            if (object.getBallView() != null) {
                object.getBallView().setX(displayMetrics.widthPixels / 2 /*afP[0]*/ - object.getBallView().getWidth() / 2);
                object.getBallView().setY(afP[1] - object.getBallView().getWidth() / 2);
            }
//                iCurStep++;
//
//            } else {
//                iCurStep = 0;
//            }
/*
//                        measuring path length

            PathMeasure pm = new PathMeasure(path1, false);
            float pathLength = (float) (pm.getLength()/6);
            float afP[] = {0f, 0f};

//                        getting point

            Log.i("pm.length",pm.getLength()+"");
            int length = scrollOffset;

            Log.i("pm.length2",length+"");

            pm.getPosTan(length, afP, null);


            if (object.getBallView() != null) {
                object.getBallView().setX(displayMetrics.widthPixels / 2 - object.getBallView().getWidth() / 2);
                object.getBallView().setY(afP[1] - object.getBallView().getWidth() / 2);
            }*/

            item = (int) ((pm.getLength() - (pm.getLength() - scrollOffset)) / object.getItemWidth());
            Log.i("Alpha Item = ", item + "");
//              sunrise
            if (item % 2 == 0) {
                if (scrollOffset < object.getItemWidth() * item + object.getItemWidth() / 2) {
                    float addSub = ((object.getItemWidth() * (item) + object.getItemWidth() / 2));
                    alpha2 = (int) ((addSub - scrollOffset) + (256 - object.getItemWidth() / 2) / 2);
                    Log.i("Alpha 1 = ", alpha2 + "");
                    sunRiseSetColor = Color.argb((int) (alpha2), 0, 66, 134);
                } else if (scrollOffset > object.getItemWidth() * item + object.getItemWidth() / 2) {
                    float addSub = ((object.getItemWidth() * (item) + object.getItemWidth() / 2));
                    alpha2 = (int) ((scrollOffset - addSub) + (256 - object.getItemWidth() / 2) / 2);
                    Log.i("Alpha 2 = ", alpha2 + "");
                    sunRiseSetColor = Color.argb((int) (alpha2), 1, 29, 57);
                }
                sunRiseSetBackgroundLayout.setBackgroundColor(sunRiseSetColor);
            }

//            sunset
            else {
                if (scrollOffset < object.getItemWidth() * item + object.getItemWidth() / 2) {
                    float addSub = ((object.getItemWidth() * (item) + object.getItemWidth() / 2));
                    alpha2 = (int) ((addSub - scrollOffset) + (256 - object.getItemWidth() / 2) / 2);
                    Log.i("Alpha 3 = ", alpha2 + "");
                    sunRiseSetColor = Color.argb((int) (alpha2), 1, 29, 57);
                } else if (scrollOffset > object.getItemWidth() * item + object.getItemWidth() / 2) {
                    float addSub = ((object.getItemWidth() * (item) + object.getItemWidth() / 2));
                    alpha2 = (int) ((scrollOffset - addSub) + (256 - object.getItemWidth() / 2) / 2);
                    Log.i("Alpha 4 = ", alpha2 + "");
                    sunRiseSetColor = Color.argb((int) (alpha2), 0, 66, 134);
                }
                sunRiseSetBackgroundLayout.setBackgroundColor(sunRiseSetColor);
            }
            previousScrollIndex = scrollOffset;

        }
    }

    public int getXValue(int num) {

        /*converting hour to pixels value for plotting*/

        displayMetrics = getResources().getDisplayMetrics();

        int val = ((int) object.getItemWidth()) * num - ((int) (object.getItemWidth() / 2));
//        Log.i("val ", " = " + val);
        return val;
    }

    public int dptopx(int val) {

        /*convert valut to pixels*/

        displayMetrics = getResources().getDisplayMetrics();

        return (int) (displayMetrics.density * val);
    }

    @Override
    public void OnItemWidthHeightListener(final int width, final int height) {

        /*listens new item width and height and updated view to set to any resolution*/

        if (firsttime && object.getVisibleItemCount() != 0 || apiCalled && object.getVisibleItemCount() != 0) {

            postDelayed(new Runnable() {
                @Override
                public void run() {

                    new AsyncTask<Void, Void, Boolean>() {
                        @Override
                        protected Boolean doInBackground(Void... voids) {
                            object.setItemHeight(height);
                            List<Point> points = new ArrayList<>();
                            List<Point> canvasPointsList = new ArrayList<>();

                    /*adding items at start and end to start first and end last item at center*/
                    /*points for adapter*/
                    /*canvasPointsList for plotting graph on canvas*/
                            int min = list.get(0).y, max = list.get(0).y, index = 0;
                            for (Point point : list) {
                                if (point.y < min) {
                                    min = point.y;
                                }
                                if (point.y > max) {
                                    max = point.y;
                                }
                            }
                            int diff = max - min;
                            int pointCenter = max + min / 2;
                            float height = (getHeight() * 0.5f);
                            int pointDiff = (int) (height / (diff));
                            centerPoint = (int) (height);// (int) (height - marginTop) / 2 + marginTop / 4;//(pointDiff * (((max + min) / 2)));
                            for (int k = 0; k < list.size(); k++) {
                                if (list.get(k).y > (max + min) / 2)
                                    list.get(k).y = (int) (centerPoint) - (pointDiff * (list.get(k).y) - pointCenter) /*- marginTop*/;
                                else {
                                    list.get(k).y = (int) (centerPoint) + (pointDiff * (pointCenter - list.get(k).y)) /*+ marginTop / 3*/;
                                }
                            }
                            int j = 0;
                            for (int i = 0; i < list.size() + (object.getVisibleItemCount() / 2) + (object.getVisibleItemCount() / 2); i++) {
                                if (i < (object.getVisibleItemCount() / 2) || i > (object.getListAdapter().size() - 1) + object.getVisibleItemCount() / 2) {
                                    points.add(new Point(0, 0));
                                } else {
                                    points.add(new Point(/*getXValue(*/list.get(j).x/*)*/, list.get(j).y));
                                    canvasPointsList.add(new Point(getXValue(list.get(j).x), list.get(j).y));
                                    j++;
                                }
                            }

//                    object.setListOriginalTemp(canvasPointsList);
                            object.setListCanvas(canvasPointsList);
                            object.setListAdapter(points);
                            adapter1.setWidth(object.getItemWidth());

                            return true;
                        }

                        @Override
                        protected void onPostExecute(Boolean aBoolean) {
//                            super.onPostExecute(aBoolean);
                            if (adapter1 != null) {
                                object.getBallView().setY((float) object.getListCanvas().get(0).y);
                                adapter1.setVisibleItemCount(object.getVisibleItemCount());
                                adapter1.setList(forecastModelList/*object.getListAdapter()*/);
                                setVisibility(VISIBLE);
                                ballView.setVisibility(VISIBLE);
                            }
                        }
                    }.execute();


                }
            }, 100);
            firsttime = false;
            apiCalled = false;
        }
    }

    public void setTempView(TextView tempView) {
        this.tempView = tempView;
    }

    public void setChanceOfRain(TextView chanceOfRain) {
        this.chanceOfRain = chanceOfRain;
    }

    public void setSunRiseSetBackgroundLayout(View sunRiseSetBackgroundLayout) {
        this.sunRiseSetBackgroundLayout = sunRiseSetBackgroundLayout;
    }

    public void updateBackground() {
        if (object != null && object.getListCanvas() != null) {
            if (item == 0)
                sunRiseSetColor = Color.argb(object.getItemWidth() /*+ (256 - object.getItemWidth()) / 2*/, 0, 66, 134);
            sunRiseSetBackgroundLayout.setBackgroundColor(sunRiseSetColor);
            /*int colorFrom = getResources().getColor(R.color.blueoverlay);
            int colorTo = sunRiseSetColor;
            int duration = 1000;
            ObjectAnimator.ofObject(sunRiseSetBackgroundLayout, "backgroundColor", new ArgbEvaluator(), colorFrom, colorTo)
                    .setDuration(duration)
                    .start();*/
        }
    }
}
