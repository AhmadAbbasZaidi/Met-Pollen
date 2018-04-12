package com.cfp.metpollen.view.customViews;

import android.annotation.SuppressLint;
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
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.graphics.ColorUtils;
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
import com.cfp.metpollen.view.adapters.GraphViewAdapter;
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

public class GraphView extends RecyclerView implements GraphViewListener {

    private static boolean firsttime = true;
    private static boolean apiCalled = false;
    private static int z = 0;
    private static int prevCenterPos = 0;
    private static int prevOffset = 0;
    private static GraphViewAdapter adapter1;
    private static RecyclerViewObject object;
    private static int item = 0;
    private static View ballView;
    private static List<Point> list;
    private static int resourceId = 0;
    private static List<ForecastModel> forecastList;
    int alpha2;
    List<Integer> sunRiseSetIndex;
    private int itemWidth, itemHeight;
    private Paint paint;
    private Path path;
    private DisplayMetrics displayMetrics;
    private int pixelSize = 0;
    private int iCurStep = 0;
    private LinearLayoutManager manager;
    private LinearSnapHelper snapHelper;
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
                    },300);
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

            if (firsttime && object.getVisibleItemCount() == 0) {
                int diff = endPos - startPos;
                object.setVisibleItemCount(diff % 2 == 0 ? diff + 1 : diff);
//                prevCenterPos=centerPos;
                smoothScrollToPosition(diff);

               /* postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setVisibility(VISIBLE);
                    }
                }, 200);*/
            }

//            Log.i("startPos ", " =  " + startPos);
//            Log.i("centerPos ", " =  " + centerPos);
//            Log.i("prevCenterPos ", " =  " + prevCenterPos);
//            Log.i("endPos ", " =  " + endPos);
//            Log.i("pos ", " =  " + pos);
//            Log.i("dx ", " =  " + dx);
//            Log.i("dy ", " =  " + dy);
//            Log.i("scrollOffset ", " =  " + scrollOffset);

            int index = centerPos - (object.getVisibleItemCount() / 2);
//            Log.i("+++++++index = ", index + "");

            /*highlighting center views*/
            if (prevCenterPos != centerPos) {
                // dehighlight the previously highlighted view
                View prevView = getLayoutManager().findViewByPosition(prevCenterPos);
                if (prevView != null) {
                    View button = prevView.findViewById(R.id.convertView);
                    button.setBackgroundColor(Color.TRANSPARENT);
                    TextView tv = (TextView) button.findViewById(R.id.tv);
                    if (tv != null) {
                        tv.setTextColor(getResources().getColor(R.color.white_pollen_text));
                    }
                    ImageView iv = (ImageView) button.findViewById(R.id.cloud);
                    if (iv != null) {
                        iv.setColorFilter(getResources().getColor(R.color.white_pollen_text));
                    }
                    View ivLine = button.findViewById(R.id.line);

                    if (ivLine != null) {
//                        ivLine.setBackgroundColor(Color.WHITE);
                        ivLine.setBackground(getResources().getDrawable(R.drawable.graph_vertical_line));
                    }
                }
//                paint.setShader(new LinearGradient(0, 0, 0, getHeight(), 0x99FFFFFF, 0x2200FFFF, Shader.TileMode.CLAMP));

                // highlight view in the middle
                if (centerView != null) {
                    View button = centerView.findViewById(R.id.convertView);
                    TextView tv = (TextView) button.findViewById(R.id.tv);
                    updateViews(forecastList.get(index));
                    if (tv != null) {
//                        tv.setTextColor(Color.WHITE);
                        tv.setTextColor(getResources().getColor(android.R.color.holo_orange_light));
                    }
                    ImageView iv = (ImageView) button.findViewById(R.id.cloud);
                    if (iv != null) {
//                        iv.setColorFilter(Color.WHITE);
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
                    updateViews(forecastList.get(index));

                    if (tv != null) {
//                        tv.setTextColor(Color.WHITE);
                        tv.setTextColor(getResources().getColor(android.R.color.holo_orange_light));
                    }
                    ImageView iv = (ImageView) button.findViewById(R.id.cloud);
                    if (iv != null) {
//                        iv.setColorFilter(Color.WHITE);
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
    private SwipeRefreshLayout swipeLayout;
    private View sunRiseSetBackgroundLayout;
    private int sunRiseSetColor = Color.TRANSPARENT;
    private int previousScrollIndex = 0;
    private boolean shouldChangeThemeDay = false;
    private int itemIndex = 0;
    private int subtractDistance = 0;
    private boolean shouldChangeThemeNight = false;
    private boolean isDay = true;

    public GraphView(Context context) {
        super(context);

        initializePaint();
        addOnScrollListener(onScrollListener);
        setLayoutManager(null);
    }

    public GraphView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initializePaint();
        addOnScrollListener(onScrollListener);
        setLayoutManager(null);
    }

    public void setSwipeLayout(SwipeRefreshLayout swipeLayout) {
        this.swipeLayout = swipeLayout;
    }

    /*@Override
    public boolean fling(int velocityX, int velocityY) {

        velocityX *= 0.7;
        // velocityX *= 0.7; for Horizontal recycler view. comment velocityY line not require for Horizontal Mode.

        return super.fling(velocityX, velocityY);
    }
*/
    public void setDatetime(TextView datetime) {
        this.datetime = datetime;
    }

    public void setConditionName(TextView conditionName) {
        this.conditionName = conditionName;
    }

    public void setConditionImage(ImageView conditionImage) {
        this.conditionImage = conditionImage;
    }

    private void updateViews(ForecastModel forecastModel) {
//        String tmp = object.getListOriginalTemp().get(centerPos - object.getVisibleItemCount() / 2).y < 10 ? "0" + object.getListOriginalTemp().get(centerPos - object.getVisibleItemCount() / 2).y : object.getListOriginalTemp().get(centerPos - object.getVisibleItemCount() / 2).y + "";
        String tmp = forecastModel.getTemperature().equalsIgnoreCase("--") ? "--" : (int) Float.parseFloat(forecastModel.getTemperature()) < 10 ? "0" + (int) Float.parseFloat(forecastModel.getTemperature()) : (int) Float.parseFloat(forecastModel.getTemperature()) + "";
        tempView.setText(tmp + "°");

        Log.i("Foramtted Time = ", forecastModel.getFormattedWeatherTime());
        WeatherCondition weatherCondition = TempConditionUtils.getConditionForecast(forecastModel.getPrecipitation(), forecastModel.getCloud(), SunRiseSETUtils.isDayForecast(forecastModel.getFormattedHour(), forecastModel.getSunRiseHour(), forecastModel.getSunRiseMin(), forecastModel.getSunSetHour(), forecastModel.getSunSetMin()));
        if (weatherCondition != null) {
            conditionName.setText(weatherCondition.getCondition());
            conditionImage.setImageResource(weatherCondition.getResourceId());
        }
        datetime.setText(utils.getTemperatureDateTime(forecastModel.getFormattedWeatherTime()));

        String prec = forecastModel.getPrecipitation().equalsIgnoreCase("--") ? "--" : (int) Float.parseFloat(forecastModel.getPrecipitation()) < 10 ? "0" + (int) Float.parseFloat(forecastModel.getPrecipitation()) : (int) Float.parseFloat(forecastModel.getPrecipitation()) + "";
        chanceOfRain.setText(prec + "mm");
    }

    private void initializePaint() {
        displayMetrics = getResources().getDisplayMetrics();
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
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(5);
        paint.setDither(true);
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

    /*
        @SuppressLint("LongLogTag")
        public void setPoints(List<ForecastModel> forecastList, List<Point> pointsList, int resourceId, View ballView) {

            if (sunRiseSetIndex == null) {
                sunRiseSetIndex = new ArrayList<>();
            } else {
                sunRiseSetIndex.clear();
            }
    //        setting rendering to software
            if (!firsttime)
                apiCalled = true;
            this.forecastList = forecastList;
            this.ballView = ballView;
            setLayerType(RecyclerView.LAYER_TYPE_SOFTWARE, null);
            setVisibility(INVISIBLE);
            ballView.setVisibility(INVISIBLE);
    //        set layout
            setLayoutManager(null);
    //        setting child views layout
            this.resourceId = resourceId;

            if (object == null) {
                object = new RecyclerViewObject(getContext(), resourceId, null, null, ballView);
            }
    //        else
    //        {
    //            object.setBallView(ballView);
    //        }

            Log.i("ForecastListSize = ", forecastList.size() + "");
            pointsList.clear();
            for (int i = 0; i < forecastList.size(); i++) {
                pointsList.add(new Point(i + 1, (int) forecastList.get(i).getTemperature()));
                if (Integer.parseInt(forecastList.get(i).getSunRiseHour()) - Integer.parseInt(forecastList.get(i).getFormattedHour()) >= 0 && Integer.parseInt(forecastList.get(i).getSunRiseHour()) - Integer.parseInt(forecastList.get(i).getFormattedHour()) <= 2) {
                    sunRiseSetIndex.add(0);
                } else if (Integer.parseInt(forecastList.get(i).getSunSetHour()) - Integer.parseInt(forecastList.get(i).getFormattedHour()) >= 0 && Integer.parseInt(forecastList.get(i).getSunSetHour()) - Integer.parseInt(forecastList.get(i).getFormattedHour()) <= 2) {
                    sunRiseSetIndex.add(1);
                } else {
                    sunRiseSetIndex.add(-1);
                }
            }
            Log.i("ForecastListSize Point= ", pointsList.size() + "");
            List<Point> points = new ArrayList<>();
            List<Point> canvasPointsList = new ArrayList<>();
            int j = 0;

    //        adding items at start and end to start first and end last item at center
    //        points for adapter
    //        canvasPointsList for plotting graph on canvas
    //        int min = 0, max = 0, index = 0;
            for (int i = 0; i < forecastList.size() + (visibleItemCount / 2) + (visibleItemCount / 2); i++) {
                if (i < (visibleItemCount / 2) || i > (forecastList.size() - 1) + visibleItemCount / 2) {
                    points.add(new Point(0, 0));
                } else {
    //                if (index == 0) {
    //                    min = max = pointsList.get(j).y;
    //                } else {
    //                    if (pointsList.get(j).y < min) {
    //                        min = pointsList.get(j).y;
    //                    }
    //                    if (pointsList.get(j).y > max) {
    //                        max = pointsList.get(j).y;
    //                    }
    //                }
    //                index++;
                    points.add(new Point(pointsList.get(j).x, pointsList.get(j).y));
                    canvasPointsList.add(new Point(getXValue(pointsList.get(j).x), pointsList.get(j).y));
                    j++;
                }
            }
            this.list = points;

    //        object.setMinTemp(min);
    //        object.setMaxTemp(max);
    //
    //        List<Point> newCanvasPointsList = new ArrayList<>();
    //        int pixelDiff = getHeight() - 200;
    //        int tempDiff = object.getMaxTemp() - object.getMinTemp();
    //        int pointDiff = pixelDiff / tempDiff;
    //
    //        for (int i = 0; i < canvasPointsList.size(); i++) {
    //            if (object.getMinTemp() == canvasPointsList.get(i).y)
    //            {
    //                newCanvasPointsList.add(new Point(canvasPointsList.get(i).x,100));
    //            }
    //            else if(object.getMaxTemp() == canvasPointsList.get(i).y)
    //            {
    //                newCanvasPointsList.add(new Point(canvasPointsList.get(i).x,getWidth()-100));
    //            }
    //            else
    //            {
    //                newCanvasPointsList.add(new Point(canvasPointsList.get(i).x,100+(canvasPointsList.get(i).y-object.getMinTemp())*pointDiff));
    //            }
    //        }
    //
    //
    //        object.setListCanvas(newCanvasPointsList);

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
    @SuppressLint("LongLogTag")
    public void setPoints(final List<ForecastModel> forecastList, final List<Point> pointsList, final int resourceId, final View ballView) {
        setLayerType(RecyclerView.LAYER_TYPE_SOFTWARE, null);
        setVisibility(INVISIBLE);
        ballView.setVisibility(INVISIBLE);
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                if (sunRiseSetIndex == null) {
                    sunRiseSetIndex = new ArrayList<>();
                } else {
                    sunRiseSetIndex.clear();
                }
//        setting rendering to software
                if (!firsttime)
                    apiCalled = true;
                GraphView.forecastList = forecastList;
                GraphView.ballView = ballView;

//        set layout
                setLayoutManager(null);
//        setting child views layout
                GraphView.resourceId = resourceId;

                if (object == null) {
                    object = new RecyclerViewObject(getContext(), resourceId, null, null, ballView);
                }

                Log.i("ForecastListSize = ", forecastList.size() + "");
                pointsList.clear();
                for (int i = 0; i < forecastList.size(); i++) {
                    pointsList.add(new Point(i + 1, (int) Float.parseFloat(forecastList.get(i).getTemperature())));
                    if (Integer.parseInt(forecastList.get(i).getSunRiseHour()) - Integer.parseInt(forecastList.get(i).getFormattedHour()) >= 0 && Integer.parseInt(forecastList.get(i).getSunRiseHour()) - Integer.parseInt(forecastList.get(i).getFormattedHour()) <= 2) {
                        sunRiseSetIndex.add(0);
                    } else if (Integer.parseInt(forecastList.get(i).getSunSetHour()) - Integer.parseInt(forecastList.get(i).getFormattedHour()) >= 0 && Integer.parseInt(forecastList.get(i).getSunSetHour()) - Integer.parseInt(forecastList.get(i).getFormattedHour()) <= 2) {
                        sunRiseSetIndex.add(1);
                    } else {
                        sunRiseSetIndex.add(-1);
                    }
                }
                Log.i("ForecastListSize Point= ", pointsList.size() + "");
                List<Point> points = new ArrayList<>();
                List<Point> canvasPointsList = new ArrayList<>();
                int j = 0;

//        adding items at start and end to start first and end last item at center
//        points for adapter
//        canvasPointsList for plotting graph on canvas

                for (int i = 0; i < forecastList.size() + (visibleItemCount / 2) + (visibleItemCount / 2); i++) {
                    if (i < (visibleItemCount / 2) || i > (forecastList.size() - 1) + visibleItemCount / 2) {
                        points.add(new Point(0, 0));
                    } else {
                        points.add(new Point(pointsList.get(j).x, pointsList.get(j).y));
                        canvasPointsList.add(new Point(getXValue(pointsList.get(j).x), pointsList.get(j).y));
                        j++;
                    }
                }
                GraphView.list = points;

                object.setListOriginalTemp(pointsList);
                object.setListCanvas(canvasPointsList);
                object.setListAdapter(points);
                object.setBallView(ballView);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
//                super.onPostExecute(aBoolean);
                object.getBallView().setY((float) object.getListCanvas().get(0).y);
                setAdapter(null);
            }
        }.execute();
    }

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
            adapter1 = new GraphViewAdapter(forecastList, getContext(), resourceId, graphViewListener);
            /*postDelayed(new Runnable() {
                @Override
                public void run() {
                    setVisibility(VISIBLE);
                    ballView.setVisibility(VISIBLE);
                }
            }, 500);*/
            super.setAdapter(adapter1);
        } else {
            super.setAdapter(adapter1);
            /*postDelayed(new Runnable() {
                @Override
                public void run() {
                    setVisibility(VISIBLE);
                    ballView.setVisibility(VISIBLE);
                }
            }, 500);*/
            addOnScrollListener(onScrollListener);
            setLayoutManager(null);
        }

    }

    @Override
    public void addOnScrollListener(OnScrollListener listener) {
        super.addOnScrollListener(listener);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /*clear canvas*/
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.OVERLAY);

        if (object != null && object.getListCanvas() != null) {

//            Log.i("obj vis cnt = ", object.getVisibleItemCount() + "");

            /*getting width in pixels of views added at left to move starting point to center*/
            int space = (object.getVisibleItemCount() / 2 * object.getItemWidth());

            path = new Path();
            Path path1 = new Path();

            for (int a = 0; a < object.getListCanvas().size(); a++) {
                Point point = object.getListCanvas().get(a);

                if (a == 0) {
                    /* draw color from 0,0 to starting point*/
                    canvas.drawRect(0 /*+ space*/ - scrollOffset, point.y, point.x + space - scrollOffset, object.getItemHeight() * displayMetrics.density, paint);
                    path.moveTo(point.x + space - scrollOffset, getHeight()/*object.getItemHeight() * displayMetrics.density*/);
                    path.lineTo(point.x + space - scrollOffset, point.y);
                    path1.moveTo(point.x + space - scrollOffset, point.y);
//                    path1.lineTo(point.x + space - scrollOffset, point.y);
                }/* else if (a == object.getListCanvas().size() - 1) {
                    path.lineTo(point.x + space - scrollOffset, point.y);
                    path.lineTo(point.x + space - scrollOffset, object.getItemHeight() * displayMetrics.density);
                    path.moveTo(point.x + space - scrollOffset, object.getItemHeight() * displayMetrics.density);
                    path.lineTo(point.x + space - scrollOffset, point.y);
                    path1.lineTo(point.x + space - scrollOffset, point.y);
//                     draw color from end poimt to width,height
                    canvas.drawRect(point.x + space - scrollOffset, point.y, getWidth(), object.getItemHeight() * displayMetrics.density, paint);
                }*/ else {
                    /*plotting curves between values */
                    path.quadTo(object.getListCanvas().get(a - 1).x + space - scrollOffset, object.getListCanvas().get(a - 1).y
                            , (point.x + object.getListCanvas().get(a - 1).x) / 2 + space - scrollOffset, (point.y + object.getListCanvas().get(a - 1).y) / 2);

                    path1.quadTo(object.getListCanvas().get(a - 1).x + space - scrollOffset, object.getListCanvas().get(a - 1).y
                            , (point.x + object.getListCanvas().get(a - 1).x) / 2 + space - scrollOffset, (point.y + object.getListCanvas().get(a - 1).y) / 2);

                    if (a == object.getListCanvas().size() - 1) {
                        path.lineTo(point.x + space - scrollOffset, point.y);
                        path.lineTo(point.x + space - scrollOffset, object.getItemHeight() * displayMetrics.density);
                        path.moveTo(point.x + space - scrollOffset, object.getItemHeight() * displayMetrics.density);
                        path.lineTo(point.x + space - scrollOffset, point.y);
                        path1.lineTo(point.x + space - scrollOffset, point.y);
//                     draw color from end poimt to width,height
                        canvas.drawRect(point.x + space - scrollOffset, point.y, getWidth(), object.getItemHeight() * displayMetrics.density, paint);

                    }
                }
            }
            path.close();
//            path1.close();
            canvas.drawPath(path, paint);
//            canvas.drawPath(path1, linePaint);

           /* *//*measuring path length*//*
            PathMeasure pm = new PathMeasure(path1, false);
            float afP[] = {0f, 0f};
            */

            /*getting point*//*
            pm.getPosTan(scrollOffset, afP, null);
            Log.i("PathMeasure = ",pm.getLength()+"");
            Log.i("ScrollOffset = ",scrollOffset+"");
            if (object.getBallView() != null) {
                object.getBallView().setX(displayMetrics.widthPixels / 2 - object.getBallView().getWidth() / 2);
                object.getBallView().setY(afP[1] - object.getBallView().getWidth() / 2);
            }*/


            PathMeasure pm = new PathMeasure(path1, false);
            float n = /*1.026851f;*/((object.getListCanvas().size() - 1) * object.getItemWidth()/*.get(object.getListCanvas().size()-1).x*/);
            float fSegmentLen = pm.getLength() / (n)/*pm.getLength() / (getWidth())*/;//pm.getLength() / 20;//we'll get 20 points from path to animate the circle
            float afP[] = {100f, 100f};

        /*setting points and drawing sun image */
//            if (iCurStep < fSegmentLen) {
            pm.getPosTan(fSegmentLen * scrollOffset /*+ pm.getLength() / getChildCount()*/, afP, null);

//            Log.i("PathMeasure n = ", n + "");
//            Log.i("FSEGMENT = ", fSegmentLen + "");
//            Log.i("PathMeasure = ", pm.getLength() + "");
//            Log.i("ScrollOffset = ", scrollOffset + "");
//            Log.i("object.getItemWidth()= ", object.getItemWidth() + "");
//            Log.i("fSegmentLen * scrOff= ", fSegmentLen * scrollOffset + "");
//            Log.i("fSegLen * scrOff / wth = ", (fSegmentLen * scrollOffset) / object.getItemWidth() + "");
            item = (int) ((pm.getLength() - (pm.getLength() - scrollOffset)) / object.getItemWidth());
            int hr = (int) (item * 3);
            int hrOfDay = hr - (24 * (hr / 24));
//            Log.i("hour= ", hrOfDay + "");
//            Log.i("hour Item= ", item + "");


//              sunrise
            if (scrollOffset == 0) {
                sunRiseSetColor = Color.argb(object.getItemWidth() /*+ (256 - object.getItemWidth()) / 2*/, 1, 29, 57);
                sunRiseSetBackgroundLayout.setBackgroundColor(sunRiseSetColor);
            } else if (Integer.parseInt(forecastList.get(item).getSunRiseHour()) - Integer.parseInt(forecastList.get(item).getFormattedHour()) >= 0 && Integer.parseInt(forecastList.get(item).getSunRiseHour()) - Integer.parseInt(forecastList.get(item).getFormattedHour()) <= 2) {

                if (scrollOffset < ((object.getItemWidth() * item + object.getItemWidth() / 2)) /*- 20*/) {
                    float addSub = scrollOffset > previousScrollIndex ? -(256 - object.getItemWidth()) / 2 : (256 - object.getItemWidth()) / 2;
                    alpha2 = (int) (((object.getItemWidth() * (item + 1)) - scrollOffset) /*+ addSub*/);
                    Log.i("Alpha 1 = ", alpha2 + "");
                    sunRiseSetColor = Color.argb((int) (alpha2), 1, 29, 57);
                } else if (scrollOffset > ((object.getItemWidth() * item + object.getItemWidth() / 2)) /*+ 20*/) {
                    float addSub = scrollOffset > previousScrollIndex ? (256 - object.getItemWidth()) / 2 : -(256 - object.getItemWidth()) / 2;
                    alpha2 = (int) ((scrollOffset - (object.getItemWidth() * (item))) /*+ addSub*/);
                    Log.i("Alpha 2 = ", alpha2 + "");
                    sunRiseSetColor = Color.argb((int) (alpha2), 0, 66, 134);
                } else {
//                    sunRiseSetColor = ColorUtils.blendARGB(Color.argb(alpha2, 1, 29, 57),Color.argb((int)(alpha2), 0, 66, 134),0.5f);
                }
                sunRiseSetBackgroundLayout.setBackgroundColor(sunRiseSetColor);
            }

//            sunset
            else if (Integer.parseInt(forecastList.get(item).getSunSetHour()) - Integer.parseInt(forecastList.get(item).getFormattedHour()) >= 0 && Integer.parseInt(forecastList.get(item).getSunSetHour()) - Integer.parseInt(forecastList.get(item).getFormattedHour()) <= 2) {
//                Log.i("statementBBB = ", (scrollOffset - (object.getItemWidth() * item)) + "");
                if (scrollOffset < ((object.getItemWidth() * item + object.getItemWidth() / 2)) /*- 20*/) {
//                    Log.i("item+1 i = ",item+" reverse first half");
                    float addSub = scrollOffset > previousScrollIndex ? -(256 - object.getItemWidth()) / 2 : (256 - object.getItemWidth()) / 2;
                    alpha2 = (int) (((object.getItemWidth() * (item + 1)) - scrollOffset) /*+ addSub*/);
                    Log.i("Alpha 3 = ", alpha2 + "");
                    sunRiseSetColor = Color.argb(alpha2, 0, 66, 134);
                } else if (scrollOffset > ((object.getItemWidth() * item + object.getItemWidth() / 2)) /*+ 20*/) {
//                    Log.i("item+ i = ",item+" reverse second half");
                    float addSub = scrollOffset > previousScrollIndex ? (256 - object.getItemWidth()) / 2 : -(256 - object.getItemWidth()) / 2;
                    alpha2 = (int) ((scrollOffset - (object.getItemWidth() * (item))) /*+ addSub*/);
                    Log.i("Alpha 4 = ", alpha2 + "");
                    sunRiseSetColor = Color.argb(alpha2, 1, 29, 57);
                }
                sunRiseSetBackgroundLayout.setBackgroundColor(sunRiseSetColor);
            } else {
                sunRiseSetBackgroundLayout.setBackgroundColor(ColorUtils.setAlphaComponent(sunRiseSetColor, object.getItemWidth()));
//                sunRiseSetBackgroundLayout.setBackgroundColor(sunRiseSetColor);
            }
            previousScrollIndex = scrollOffset;

//            else if (Integer.parseInt(forecastList.get(item).getSunRiseHour()) - Integer.parseInt(forecastList.get(item).getFormattedHour()) < 0||Integer.parseInt(forecastList.get(item).getSunSetHour()) - Integer.parseInt(forecastList.get(item).getFormattedHour()) < 0) {
//                sunRiseSetBackgroundLayout.setBackgroundColor(sunRiseSetColor);
//            }

/*
            int sunRiseHour = Integer.parseInt(forecastList.get(item).getSunRiseHour());
            int sunSetHour = Integer.parseInt(forecastList.get(item).getSunSetHour());
            int formattedHour = Integer.parseInt(forecastList.get(item).getFormattedHour());


//            if(previousScrollIndex<scrollOffset) {
            if (sunRiseSetIndex.get(item) == 0)//(isDay&&(sunRiseHour - formattedHour >= 0 && sunRiseHour - formattedHour <= 2))//||(sunRiseHour-formattedHour>2&&sunSetHour-formattedHour>2))
            {
                Log.i("DAY = ", "true");
                this.itemIndex = item;
                shouldChangeThemeDay = true;
                shouldChangeThemeNight = false;
                subtractDistance = item * object.getItemWidth();
                isDay = false;
            } else if (sunRiseSetIndex.get(item) == 1)//((sunSetHour - formattedHour >= 0 && sunSetHour - formattedHour <= 2))
            {
                Log.i("DAY = ", "false");
                this.itemIndex = item;
                shouldChangeThemeDay = false;
                shouldChangeThemeNight = true;
                subtractDistance = item * object.getItemWidth();
            } else if (item == 0 || item == 1)//((sunSetHour - formattedHour >= 0 && sunSetHour - formattedHour <= 2))
            {
                Log.i("DAY = ", "false" + " Night = false");
                this.itemIndex = item;
                shouldChangeThemeDay = false;
                shouldChangeThemeNight = false;
                subtractDistance = item * object.getItemWidth();
            }
            updateSunRiseSetTheme(shouldChangeThemeDay, shouldChangeThemeNight, scrollOffset > previousScrollIndex ? true : false);
//            }
            previousScrollIndex = scrollOffset;
*/
//            canvas.drawCircle(afP[0], afP[1], 30, linePaint);
            if (object.getBallView() != null) {
                object.getBallView().setX(displayMetrics.widthPixels / 2 /*afP[0]*/ - object.getBallView().getWidth() / 2);
                object.getBallView().setY(afP[1] - object.getBallView().getWidth() / 2);
            }
        }
    }

    @SuppressLint("LongLogTag")
    private void updateSunRiseSetTheme(boolean shouldChangeThemeDay, boolean shouldChangeThemeNight, boolean isForwardMovement) {
        if (isForwardMovement) {
            if (shouldChangeThemeDay) {
                int sunRiseHour = Integer.parseInt(forecastList.get(itemIndex).getSunRiseHour());
                int sunSetHour = Integer.parseInt(forecastList.get(itemIndex).getSunSetHour());
                int formattedHour = Integer.parseInt(forecastList.get(itemIndex).getFormattedHour());
                float dividend = (float) (scrollOffset - (object.getItemWidth() * ((itemIndex / 8) * 8 + (sunRiseHour / 3)) /*+ subtractDistance*/));
                float divisor = (float) (object.getItemWidth() * (sunSetHour / 3 - sunRiseHour / 3));
                float val = (float) (dividend / divisor) * 128 + 128;

//            Log.i("val dividend = ", dividend + "");
//            Log.i("val divisor = ", divisor + "");
//            Log.i("val = ", val + "");

//            int alpha = object.getItemWidth() * sunRiseHour - (scrollOffset * val);
                sunRiseSetColor = Color.argb((int) val, 0, 66, 134);
//            sunRiseSetColor = Color.argb((int) val, 1, 29, 57);

                sunRiseSetBackgroundLayout.setBackgroundColor(sunRiseSetColor);
            } else if (shouldChangeThemeNight) {
                int in = (itemIndex / 8 + 1) * 8;
                int sunRiseHour = Integer.parseInt(forecastList.get(in).getSunRiseHour());
                int sunSetHour = Integer.parseInt(forecastList.get(itemIndex).getSunSetHour());
                int formattedHour = Integer.parseInt(forecastList.get(itemIndex).getFormattedHour());
                float dividend = (float) (scrollOffset - ((object.getItemWidth() * (in + (sunRiseHour / 3)) /*- scrollOffset*/) /*+ subtractDistance*/));
                int nextSunRise = (itemIndex / 8) * 8 + 8;
                int nextSunRiseHour = Integer.parseInt(forecastList.get(nextSunRise).getSunRiseHour());
                int add = nextSunRiseHour / 3 + nextSunRise;
                float divisor = (float) (object.getItemWidth() * (add - ((itemIndex / 8) * 8 + (sunSetHour / 3))));
                float val = (float) (dividend / divisor) * 128 + 256;

                Log.i("val scrollOffset = ", scrollOffset + "");
                Log.i("val in + (sunRiseHour / 3) = ", in + (sunRiseHour / 3) + "");
                Log.i("val object.getItemWidth() * (in + (sunRiseHour / 3)) = ", object.getItemWidth() * (in + (sunRiseHour / 3)) + "");
                Log.i("val dividend = ", dividend + "");
                Log.i("val divisor = ", divisor + "");
                Log.i("val = ", val + "");

//            int alpha = object.getItemWidth() * sunRiseHour - (scrollOffset * val);

//            sunRiseSetColor = Color.argb((int) val, 0, 66, 134);
                sunRiseSetColor = Color.argb((int) val, 1, 29, 57);

                sunRiseSetBackgroundLayout.setBackgroundColor(sunRiseSetColor);
            } else if (!shouldChangeThemeDay && !shouldChangeThemeNight) {
                int in = (itemIndex / 8 + 1) * 8;
                int sunRiseHour = Integer.parseInt(forecastList.get(in).getSunRiseHour());
                int sunSetHour = Integer.parseInt(forecastList.get(itemIndex).getSunSetHour());
                int formattedHour = Integer.parseInt(forecastList.get(itemIndex).getFormattedHour());
                float dividend = (float) (scrollOffset);
                int nextSunRise = (itemIndex / 8) * 8 + 8;
                int nextSunRiseHour = Integer.parseInt(forecastList.get(nextSunRise).getSunRiseHour());
                int add = nextSunRiseHour / 3 + nextSunRise;
                float divisor = (float) (object.getItemWidth() * (Integer.parseInt(forecastList.get(itemIndex).getSunRiseHour()) % 3));
                float val = (float) ((dividend) / divisor) * 128 + 256 /*+256/5*/;

                Log.i("val scrollOffset = ", scrollOffset + "");
                Log.i("val in + (sunRiseHour / 3) = ", in + (sunRiseHour / 3) + "");
                Log.i("val object.getItemWidth() * (in + (sunRiseHour / 3)) = ", object.getItemWidth() * (in + (sunRiseHour / 3)) + "");
                Log.i("val dividend = ", dividend + "");
                Log.i("val divisor = ", divisor + "");
                Log.i("val = ", val + "");

//            int alpha = object.getItemWidth() * sunRiseHour - (scrollOffset * val);

//            sunRiseSetColor = Color.argb((int) val, 0, 66, 134);
                sunRiseSetColor = Color.argb((int) val, 1, 29, 57);

                sunRiseSetBackgroundLayout.setBackgroundColor(sunRiseSetColor);
            }
        } else {
            if (shouldChangeThemeDay) {
                int sunRiseHour = Integer.parseInt(forecastList.get(itemIndex).getSunRiseHour());
                int sunSetHour = Integer.parseInt(forecastList.get(itemIndex).getSunSetHour());
                int formattedHour = Integer.parseInt(forecastList.get(itemIndex).getFormattedHour());
                float dividend = (float) ((object.getItemWidth() * ((itemIndex / 8) * 8 + (sunRiseHour / 3)) - scrollOffset/*+ subtractDistance*/));
                float divisor = (float) (object.getItemWidth() * (sunSetHour / 3 - sunRiseHour / 3));
                float val = (float) (dividend / divisor) * 128 + 128;

//            Log.i("val dividend = ", dividend + "");
//            Log.i("val divisor = ", divisor + "");
//            Log.i("val = ", val + "");

//            int alpha = object.getItemWidth() * sunRiseHour - (scrollOffset * val);
                sunRiseSetColor = Color.argb((int) val, 0, 66, 134);
//            sunRiseSetColor = Color.argb((int) val, 1, 29, 57);

                sunRiseSetBackgroundLayout.setBackgroundColor(sunRiseSetColor);
            } else if (shouldChangeThemeNight) {
                int in = (itemIndex / 8 + 1) * 8;
                int sunRiseHour = Integer.parseInt(forecastList.get(in).getSunRiseHour());
                int sunSetHour = Integer.parseInt(forecastList.get(itemIndex).getSunSetHour());
                int formattedHour = Integer.parseInt(forecastList.get(itemIndex).getFormattedHour());
                float dividend = (float) (((object.getItemWidth() * (in + (sunRiseHour / 3)) - scrollOffset) /*+ subtractDistance*/));
                int nextSunRise = (itemIndex / 8) * 8 + 8;
                int nextSunRiseHour = Integer.parseInt(forecastList.get(nextSunRise).getSunRiseHour());
                int add = nextSunRiseHour / 3 + nextSunRise;
                float divisor = (float) (object.getItemWidth() * (add - ((itemIndex / 8) * 8 + (sunSetHour / 3))));
                float val = (float) (dividend / divisor) * 128 + 256;

                Log.i("val scrollOffset = ", scrollOffset + "");
                Log.i("val in + (sunRiseHour / 3) = ", in + (sunRiseHour / 3) + "");
                Log.i("val object.getItemWidth() * (in + (sunRiseHour / 3)) = ", object.getItemWidth() * (in + (sunRiseHour / 3)) + "");
                Log.i("val dividend = ", dividend + "");
                Log.i("val divisor = ", divisor + "");
                Log.i("val = ", val + "");

//            int alpha = object.getItemWidth() * sunRiseHour - (scrollOffset * val);

//            sunRiseSetColor = Color.argb((int) val, 0, 66, 134);
                sunRiseSetColor = Color.argb((int) val, 1, 29, 57);

                sunRiseSetBackgroundLayout.setBackgroundColor(sunRiseSetColor);
            } else if (!shouldChangeThemeDay && !shouldChangeThemeNight) {
                int in = (itemIndex / 8 + 1) * 8;
                int sunRiseHour = Integer.parseInt(forecastList.get(in).getSunRiseHour());
                int sunSetHour = Integer.parseInt(forecastList.get(itemIndex).getSunSetHour());
                int formattedHour = Integer.parseInt(forecastList.get(itemIndex).getFormattedHour());
                float dividend = (float) (scrollOffset);
                int nextSunRise = (itemIndex / 8) * 8 + 8;
                int nextSunRiseHour = Integer.parseInt(forecastList.get(nextSunRise).getSunRiseHour());
                int add = nextSunRiseHour / 3 + nextSunRise;
                float divisor = (float) (object.getItemWidth() * (Integer.parseInt(forecastList.get(itemIndex).getSunRiseHour()) % 3));
                float val = (float) ((dividend) / divisor) * 128 + 256 /*+256/5*/;

                Log.i("val scrollOffset = ", scrollOffset + "");
                Log.i("val in + (sunRiseHour / 3) = ", in + (sunRiseHour / 3) + "");
                Log.i("val object.getItemWidth() * (in + (sunRiseHour / 3)) = ", object.getItemWidth() * (in + (sunRiseHour / 3)) + "");
                Log.i("val dividend = ", dividend + "");
                Log.i("val divisor = ", divisor + "");
                Log.i("val = ", val + "");

//            int alpha = object.getItemWidth() * sunRiseHour - (scrollOffset * val);
//            sunRiseSetColor = Color.argb((int) val, 0, 66, 134);

                sunRiseSetColor = Color.argb((int) val, 1, 29, 57);
                sunRiseSetBackgroundLayout.setBackgroundColor(sunRiseSetColor);
            }
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
                            int j = 0;
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
                            int pointDiff = (int) ((getHeight() * 0.6f)/*(60 * getResources().getDisplayMetrics().density)*/ / diff);
                            for (int k = 0; k < list.size(); k++) {
                                list.get(k).y = pointDiff * (max - list.get(k).y) + marginTop;
                            }

                            for (int i = 0; i < list.size() + (object.getVisibleItemCount() / 2) + (object.getVisibleItemCount() / 2); i++) {
                                if (i < (object.getVisibleItemCount() / 2) || i > (list.size() - 1) + object.getVisibleItemCount() / 2) {
                                    points.add(new Point(0, 0));
                                } else {
                                    points.add(new Point(/*getXValue(*/list.get(j).x/*)*/, list.get(j).y));
                                    canvasPointsList.add(new Point(getXValue(list.get(j).x), list.get(j).y));
                                    j++;
                                }
                            }
                            object.setListCanvas(canvasPointsList);
                            object.setListAdapter(points);
                            adapter1.setWidth(object.getItemWidth());


                            return true;
                        }

                        @Override
                        protected void onPostExecute(Boolean aBoolean) {
//                            super.onPostExecute(aBoolean);

                            if (adapter1 != null) {
                                sunRiseSetColor = Color.argb(object.getItemWidth() /*+ (256 - object.getItemWidth()) / 2*/, 1, 29, 57);
                                sunRiseSetBackgroundLayout.setBackgroundColor(sunRiseSetColor);
                                object.getBallView().setY((float) object.getListCanvas().get(0).y);
                                adapter1.setVisibleItemCount(object.getVisibleItemCount());
                                adapter1.setList(forecastList/*object.getListAdapter()*/);
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
                sunRiseSetColor = Color.argb(object.getItemWidth()/* + (256 - object.getItemWidth()) / 2*/, 1, 29, 57);
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
