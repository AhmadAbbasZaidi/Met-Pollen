package com.cfp.metpollen.view.customViews;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.cfp.metpollen.R;
import com.cfp.metpollen.data.db.model.ForecastModel;
import com.cfp.metpollen.view.utilities.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AhmedAbbas on 11/30/2017.
 */
public class HeatMapGraphView extends View {
    private static List<ForecastModel> forecastModelList;
    private static boolean canDraw = false;
    private static int currentTemperature;
    private DisplayMetrics displayMetrics;
    private Paint linePaint;
    private Paint paint;
    private Paint ballPaint;
    private List<Point> tempList;
    private int marginTop = 40;
    private int marginBottom = (int) (7 * getResources().getDisplayMetrics().density);
    private int marginLeft = 20;
    private int marginRight = 20;
    private float itemWidth = 0;
    private Paint tPaint;
    private List<Point> tempList2;
    private Paint ballStrokePaint;
    private Drawable sunRise, sunSet;
    private int sunsize = (int) (10 * getResources().getDisplayMetrics().density);
    private Paint shadowPaint;
    private Paint backgroundPaint;
    private GradientDrawable rainbow;
    private List<Point> tempListOther;
    private Paint shadowStrokePaint;
    private List<Point> peakValuesIndexes;
    private float text_size_large = 25f + getResources().getDisplayMetrics().density;
    private float text_size_medium = 20f + getResources().getDisplayMetrics().density;
    private float text_size_small = 12f + getResources().getDisplayMetrics().density;

    public HeatMapGraphView(Context context) {
        super(context);
//        setList();
        initializePaint();
    }

    public HeatMapGraphView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
//        setList();
        initializePaint();
    }

    public void setList(List<ForecastModel> forecastModelList, int temperature) {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        Log.i("Heatmap ", "set list called ");
        canDraw = false;
        this.currentTemperature = temperature;
        this.forecastModelList = forecastModelList;
        tempList = new ArrayList<>();
        int j = 0;
        for (int i = 0; i < forecastModelList.size(); i++) {
            if (i < forecastModelList.size() - 1) {
                Log.i("value heatmap= ", j + "");
                tempList.add(new Point(j,(int) Float.parseFloat(forecastModelList.get(i).getTemperature())));
                j++;
                int valuesRequired = 2;
                int diff = (int)(Float.parseFloat(forecastModelList.get(i).getTemperature()) - Integer.parseInt(forecastModelList.get(i + 1).getTemperature()));
                int minValToAdd = diff / (valuesRequired + 1);
                Log.i("value heatmap= ", j + "");
                tempList.add(new Point(j,(int) (Float.parseFloat(forecastModelList.get(i).getTemperature()) + (minValToAdd))));
                j++;
                Log.i("value heatmap= ", j + "");
                tempList.add(new Point(j, (int) (Float.parseFloat(forecastModelList.get(i).getTemperature()) + (2 * minValToAdd))));
                j++;
            } else {
                Log.i("value heatmap= ", j + "");
                tempList.add(new Point(j,(int) Float.parseFloat(forecastModelList.get(i).getTemperature())));
                j++;
                Log.i("value heatmap= ", j + "");
                tempList.add(new Point(j,(int) Float.parseFloat(forecastModelList.get(i).getTemperature())));
                j++;
                Log.i("value heatmap= ", j + "");
                tempList.add(new Point(j, (int) Float.parseFloat(forecastModelList.get(i).getTemperature())));
                j++;
            }
        }

        /*tempList.add(new Point(0, 8));
        tempList.add(new Point(1, 7));
        tempList.add(new Point(2, 7));
        tempList.add(new Point(3, 7));
        tempList.add(new Point(4, 6));
        tempList.add(new Point(5, 5));
        tempList.add(new Point(6, 6));
        tempList.add(new Point(7, 6));
        tempList.add(new Point(8, 7));
        tempList.add(new Point(9, 9));
        tempList.add(new Point(10, 11));
        tempList.add(new Point(11, 14));
        tempList.add(new Point(12, 15));
        tempList.add(new Point(13, 16));
        tempList.add(new Point(14, 17));
        tempList.add(new Point(15, 17));
        tempList.add(new Point(16, 19));
        tempList.add(new Point(17, 17));
        tempList.add(new Point(18, 14));
        tempList.add(new Point(19, 12));
        tempList.add(new Point(20, 12));
        tempList.add(new Point(21, 12));
        tempList.add(new Point(22, 11));
        tempList.add(new Point(23, 10));*/

        tempList2 = new ArrayList<>();

        int min = tempList.get(0).y;
        int min2 = tempList.get(0).x;
        int max = tempList.get(0).y;
        int max2 = tempList.get(0).x;

        for (Point point : tempList) {
            if (point.y < min) {
                min = point.y;
                min2 = point.x;
            }
            if (point.y > max) {
                max = point.y;
                max2 = point.x;
            }
        }
        int med2 = (min2 + max2) / 2;
        Log.i("min2 heatmap= ", min2 + "");
        Log.i("min heatmap= ", min + "");
        Log.i("med2 heatmap= ", med2 + "");
        Log.i("max2 heatmap= ", max2 + "");
        Log.i("max heatmap= ", max + "");
        Log.i("med heatmap= ", tempList.get(med2).y + "");

        /**/
        peakValuesIndexes = new ArrayList<>();
        peakValuesIndexes.add(new Point(tempList.get(0).x, tempList.get(0).y));
        if (min2 < max2) {
            peakValuesIndexes.add(new Point(tempList.get(min2).x, tempList.get(min2).y));
            peakValuesIndexes.add(new Point(tempList.get(med2).x, tempList.get(med2).y));
            peakValuesIndexes.add(new Point(tempList.get(max2).x, tempList.get(max2).y));
        } else {
            peakValuesIndexes.add(new Point(tempList.get(max2).x, tempList.get(max2).y));
            peakValuesIndexes.add(new Point(tempList.get(med2).x, tempList.get(med2).y));
            peakValuesIndexes.add(new Point(tempList.get(min2).x, tempList.get(min2).y));
        }
        peakValuesIndexes.add(new Point(tempList.size(), tempList.get(tempList.size() - 1).y));

        int diff = max - min;

        int height = (int) (/*getHeight()*0.8f*/ 100 * getResources().getDisplayMetrics().density);
        int pointDiff = (int) height / (diff + 2);


        for (int i = 0; i < peakValuesIndexes.size(); i++) {
            peakValuesIndexes.get(i).y = (int) (height - pointDiff * peakValuesIndexes.get(i).y);
            Log.i(" i  = ", i + "");
            Log.i("peak.get(i).y heatmap= ", peakValuesIndexes.get(i).y + "");
            Log.i(" height heatmap = ", height + "");
        }

        for (int i = 0; i < tempList.size(); i++) {
            Point point = tempList.get(i);
            tempList2.add(new Point(i, (pointDiff * (max - point.y)) + marginTop));
        }
        //        tempList.clear();

        setGraphColor();
    }

    private void mapValues() {
        tempList2 = new ArrayList<>();

        int min = tempList.get(0).y;
        int min2 = tempList.get(0).y;
        int max = tempList.get(0).y;
        int max2 = tempList.get(0).y;

        for (Point point : tempList) {
            if (point.y < min) {
                min = point.y;
                min2 = point.x;
            }
            if (point.y > max) {
                max = point.y;
                max2 = point.x;
            }
        }
        int med2 = (min2 + max2) / 2;
        Log.i("min2 heatmap= ", min2 + "");
        Log.i("min heatmap= ", min + "");
        Log.i("med2 heatmap= ", med2 + "");
        Log.i("med heatmap= ", tempList.get(med2).y + "");
        Log.i("max2 heatmap= ", max2 + "");
        Log.i("max heatmap= ", max + "");

        /**/
        peakValuesIndexes = new ArrayList<>();
        peakValuesIndexes.add(new Point(tempList.get(0).x, tempList.get(0).y));
        if (min2 < max2) {
            peakValuesIndexes.add(new Point(tempList.get(min2).x, tempList.get(min2).y));
            peakValuesIndexes.add(new Point(tempList.get(med2).x, tempList.get(med2).y));
            peakValuesIndexes.add(new Point(tempList.get(max2).x, tempList.get(max2).y));
        } else {
            peakValuesIndexes.add(new Point(tempList.get(max2).x, tempList.get(max2).y));
            peakValuesIndexes.add(new Point(tempList.get(med2).x, tempList.get(med2).y));
            peakValuesIndexes.add(new Point(tempList.get(min2).x, tempList.get(min2).y));
        }
        peakValuesIndexes.add(new Point(tempList.get(tempList.size() - 1).x, tempList.get(tempList.size() - 1).y));
        /**/
        int diff = max - min;

        int height = (int) (getHeight() * 0.8f /*100 * getResources().getDisplayMetrics().density*/);
        int pointDiff = (int) height / (diff + 2);


        for (int i = 0; i < peakValuesIndexes.size(); i++) {
//            peakValuesIndexes.get(i).y = (int) (height - pointDiff * peakValuesIndexes.get(i).y);
            peakValuesIndexes.get(i).y = (pointDiff * (max - peakValuesIndexes.get(i).y)) + marginTop;
            Log.i(" i  = ", i + "");
            Log.i("peak.get(i).y heatmap= ", peakValuesIndexes.get(i).y + "");
            Log.i(" height heatmap = ", height + "");
        }

        for (int i = 0; i < tempList.size(); i++) {
            Point point = tempList.get(i);
            tempList2.add(new Point(i, (pointDiff * (max - point.y)) + marginTop));
        }
        //        tempList.clear();
    }

    private void initializePaint() {
//        displayMetrics = new DisplayMetrics();
//        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        itemWidth = (float)(getResources().getDisplayMetrics().widthPixels)/tempList.size();

//        Log.i("Width Pixels *******= ", getResources().getDisplayMetrics().widthPixels+"");

        /*DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        int deviceWidth = displayMetrics.widthPixels;
        int deviceHeight = displayMetrics.heightPixels;

        itemWidth = deviceWidth/24;*/

        /*ball paint*/
        ballPaint = new Paint();
        ballPaint.setAntiAlias(true);
        ballPaint.setStyle(Paint.Style.FILL);
        ballPaint.setColor(0xFF128EF0);

        /*paint for horizontal bottom line*/
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(2);
        linePaint.setColor(Color.WHITE);

        shadowPaint = new Paint();
        shadowPaint.setAntiAlias(true);
        shadowPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        shadowPaint.setStrokeWidth(3.0f);
        shadowPaint.setStrokeCap(Paint.Cap.ROUND);
        shadowPaint.setColor(Color.TRANSPARENT);
        shadowPaint.setShadowLayer(30, 0, -6, 0xB2000000);

        shadowStrokePaint = new Paint();
        shadowStrokePaint.setAntiAlias(true);
        shadowStrokePaint.setStyle(Paint.Style.STROKE);
        shadowStrokePaint.setStrokeWidth(3.0f);
        shadowStrokePaint.setStrokeCap(Paint.Cap.ROUND);
        shadowStrokePaint.setColor(0x4C000000);

        backgroundPaint = new Paint();
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setStyle(Paint.Style.FILL);
//        backgroundPaint.setColor(0x28000000);

        ballStrokePaint = new Paint();
        ballStrokePaint.setAntiAlias(true);
        ballStrokePaint.setStyle(Paint.Style.STROKE);
        ballStrokePaint.setStrokeWidth(2 * getResources().getDisplayMetrics().density);
        ballStrokePaint.setColor(Color.WHITE);

        /*text paint for  graph fill*/
        tPaint = new Paint();
        tPaint.setAntiAlias(true);
        tPaint.setStyle(Paint.Style.FILL);
        tPaint.setStrokeWidth(1);
        tPaint.setColor(Color.WHITE);
        tPaint.setTextSize(16f);

        /*paint for  graph fill*/
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(0);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeMiter(10f);

        paint.setShader(new LinearGradient(0, 0, 0, getHeight(), 0x99FFFFFF, 0x2200FFFF, Shader.TileMode.CLAMP));

        sunRise = getResources().getDrawable(R.drawable.sunrise);
        sunSet = getResources().getDrawable(R.drawable.sunset);


    }

    private void setGraphColor() {
        displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        itemWidth = (float) (getResources().getDisplayMetrics().widthPixels) / tempList.size();
        Log.i("Width Pixels heatmap= ", getResources().getDisplayMetrics().widthPixels + "");
        /*
        50 – 40 maroon : #80ae0202
        40 – 30 red : #80e40404
        29 – 24  orange :  #80e45304
        23 – 18 Purple : #80b704e4
        17 – 9 Blue : #802f20f1
        8 – 0 and lower Aqua : #802087f1
        */

        /*
        50 – 40 maroon : #CCae0202
        39 – 30 red : #CCe40404
        29 – 20  orange :  #CCe45304
        19 – 11 Purple : #CCb704e4
        10 – 0 Blue : #CC2f20f1
        0 – lower Aqua :  #CC2087f1
        */

        List<Integer> colors = new ArrayList<>();
        for (int i = 0; i < tempList.size(); i++) {
            if (tempList.get(i).y >= 50) {
                colors.add(getResources().getColor(R.color.maroon));
            } else if (tempList.get(i).y >= 40 && tempList.get(i).y < 50) {
                colors.add(getResources().getColor(R.color.maroon));
            } else if (tempList.get(i).y >= 30 && tempList.get(i).y < 40) {
                colors.add(getResources().getColor(R.color.red));
            } else if (tempList.get(i).y >= 20 && tempList.get(i).y < 30) {
                colors.add(getResources().getColor(R.color.orange));
            } else if (tempList.get(i).y >= 11 && tempList.get(i).y < 20) {
                colors.add(getResources().getColor(R.color.purple));
            } else if (tempList.get(i).y >= 0 && tempList.get(i).y < 11) {
                colors.add(getResources().getColor(R.color.blue));
            } else if (/*tempList.get(i).y >= -1 && */tempList.get(i).y < 0) {
                colors.add(getResources().getColor(R.color.aqua));
            }
        }

        int divider = 1000;

        int[] heatMap = new int[]
                {
                        colors.get(0),
                        colors.get(7),
                        colors.get(12),
                        colors.get(17),
                        colors.get(23)
                };


        float[] pt = new float[]
                {
                        Float.valueOf((tempList2.get(0).x * itemWidth + itemWidth / 2) / divider),
                        Float.valueOf((tempList2.get(7).x * itemWidth + itemWidth / 2) / divider),
                        Float.valueOf((tempList2.get(12).x * itemWidth + itemWidth / 2) / divider),
                        Float.valueOf((tempList2.get(17).x * itemWidth + itemWidth / 2) / divider),
                        Float.valueOf((tempList2.get(23).x * itemWidth + itemWidth / 2) / divider)
                };
        Log.i("itemWidth heatmap", itemWidth + "");

        Shader shaderBg = new LinearGradient(0, 0, 0, displayMetrics.widthPixels, heatMap, /*null*/pt, Shader.TileMode.MIRROR);
        Matrix matrix = new Matrix();
        matrix.setRotate(90);
        shaderBg.setLocalMatrix(matrix);
        paint.setShader(shaderBg);
//        paint.setShadowLayer(3, 0, -5, 0xFF000000);
        paint.setDither(true);

        /*shadowPaint.setColor(Color.BLACK);
        setLayerType(LAYER_TYPE_SOFTWARE, shadowPaint);
        shadowPaint.setMaskFilter(new BlurMaskFilter(5, BlurMaskFilter.Blur.INNER));*/

        canDraw = true;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (canDraw) {
            Log.i("can Draw heatmap= ", "true++++++++++++++++++++++++++++++");
            mapValues();
//        setList();
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.OVERLAY);

            Path path = new Path();
            Path ballPath = new Path();
            Path shadowPath = new Path();

            path.moveTo(0, getHeight());
            path.lineTo(0, tempList2.get(0).y);
            ballPath.moveTo(0, tempList2.get(0).y);
            shadowPath.moveTo(0, getHeight());
            shadowPath.lineTo(0, tempList2.get(0).y);

//        path.cubicTo(0, tempList2.get(0).y, itemWidth * 7, tempList2.get(7).y, itemWidth * 9, tempList2.get(9).y);
//        path.cubicTo(itemWidth * 9, tempList2.get(9).y, itemWidth * 15, tempList2.get(15).y, itemWidth * 24, tempList2.get(23).y);

            path.cubicTo(peakValuesIndexes.get(0).x, peakValuesIndexes.get(0).y, itemWidth * peakValuesIndexes.get(1).x, peakValuesIndexes.get(1).y, itemWidth * peakValuesIndexes.get(2).x, getHeight() / 2/*peakValuesIndexes.get(2).y*/);
            path.cubicTo(itemWidth * peakValuesIndexes.get(2).x, getHeight() / 2/* peakValuesIndexes.get(2).y*/, itemWidth * peakValuesIndexes.get(3).x, peakValuesIndexes.get(3).y, itemWidth * peakValuesIndexes.get(4).x + itemWidth, peakValuesIndexes.get(4).y);

            ballPath.cubicTo(peakValuesIndexes.get(0).x, peakValuesIndexes.get(0).y, itemWidth * peakValuesIndexes.get(1).x, peakValuesIndexes.get(1).y, itemWidth * peakValuesIndexes.get(2).x, getHeight() / 2/*peakValuesIndexes.get(2).y*/);
            ballPath.cubicTo(itemWidth * peakValuesIndexes.get(2).x, getHeight() / 2/* peakValuesIndexes.get(2).y*/, itemWidth * peakValuesIndexes.get(3).x, peakValuesIndexes.get(3).y, itemWidth * peakValuesIndexes.get(4).x + itemWidth, peakValuesIndexes.get(4).y);

            shadowPath.cubicTo(peakValuesIndexes.get(0).x, peakValuesIndexes.get(0).y, itemWidth * peakValuesIndexes.get(1).x, peakValuesIndexes.get(1).y, itemWidth * peakValuesIndexes.get(2).x, getHeight() / 2/*peakValuesIndexes.get(2).y*/);
            shadowPath.cubicTo(itemWidth * peakValuesIndexes.get(2).x, getHeight() / 2/* peakValuesIndexes.get(2).y*/, itemWidth * peakValuesIndexes.get(3).x, peakValuesIndexes.get(3).y, itemWidth * peakValuesIndexes.get(4).x + itemWidth, peakValuesIndexes.get(4).y);

/*
            ballPath.cubicTo(peakValuesIndexes.get(0).x, peakValuesIndexes.get(0).y, itemWidth * peakValuesIndexes.get(1).x, peakValuesIndexes.get(1).y, itemWidth * peakValuesIndexes.get(2).x, peakValuesIndexes.get(2).y);
            ballPath.cubicTo(itemWidth * peakValuesIndexes.get(2).x, peakValuesIndexes.get(2).y, itemWidth * peakValuesIndexes.get(3).x, peakValuesIndexes.get(3).y, itemWidth * peakValuesIndexes.get(4).x + itemWidth, peakValuesIndexes.get(4).y);

            shadowPath.cubicTo(peakValuesIndexes.get(0).x, peakValuesIndexes.get(0).y, itemWidth * peakValuesIndexes.get(1).x, peakValuesIndexes.get(1).y, itemWidth * peakValuesIndexes.get(2).x, peakValuesIndexes.get(2).y);
            shadowPath.cubicTo(itemWidth * peakValuesIndexes.get(2).x, peakValuesIndexes.get(2).y, itemWidth * peakValuesIndexes.get(3).x, peakValuesIndexes.get(3).y, itemWidth * peakValuesIndexes.get(4).x + itemWidth, peakValuesIndexes.get(4).y);
*/

        /*ballPath.cubicTo(0, tempList2.get(0).y, itemWidth * 7, tempList2.get(7).y, itemWidth * 9, tempList2.get(9).y);
        ballPath.cubicTo(itemWidth * 9, tempList2.get(9).y, itemWidth * 15, tempList2.get(15).y, itemWidth * 24, tempList2.get(23).y);

        shadowPath.cubicTo(0, tempList2.get(0).y, itemWidth * 7, tempList2.get(7).y, itemWidth * 9, tempList2.get(9).y);
        shadowPath.cubicTo(itemWidth * 9, tempList2.get(9).y, itemWidth * 15, tempList2.get(15).y, itemWidth * 24, tempList2.get(23).y);
*/
            path.lineTo(itemWidth * 24, getHeight());
            shadowPath.lineTo(itemWidth * 24, getHeight());
//        path.quadTo(itemWidth * peakValuesIndexes.get(4).x+itemWidth, tempList2.get(tempList2.size() - 1).y, itemWidth * 24, getHeight());
//        shadowPath.quadTo(itemWidth * 24, tempList2.get(tempList2.size() - 1).y, itemWidth * 24, getHeight());
            path.close();
//      shadowPath.close();
            canvas.drawPath(shadowPath, shadowPaint);
            canvas.drawPath(path, paint);
//        canvas.drawPath(shadowPath, shadowStrokePaint);

            canvas.drawLine(0, getHeight() - (marginBottom * 2), getWidth() /*- marginRight*/, getHeight() - (marginBottom * 2), linePaint);

            for (int i = 0; i < tempList2.size(); i++) {
                if (i == 0) {
                    tPaint.setTextSize(text_size_large);
                    canvas.drawText(tempList.get(i).y < 10 ? "0" + tempList.get(i).y + "°" : tempList.get(i).y + "°", (itemWidth * i) + itemWidth / 2 - tPaint.getTextSize() / 2.5f, getHeight() - (2.5f * marginBottom), tPaint);
                } else if (i == tempList2.size() - 1) {

                    tPaint.setTextSize(text_size_large);
                    canvas.drawText(tempList.get(i).y < 10 ? "0" + tempList.get(i).y + "°" : tempList.get(i).y + "°", (itemWidth * i) + itemWidth / 2 - tPaint.getTextSize(), getHeight() - (2.5f * marginBottom), tPaint);
                }

                if (i % 3 == 0 || i == 23) {
                    tPaint.setTextSize(text_size_large);
                    canvas.drawText(i < 10 ? "0" + i : i + "", (itemWidth * i) + itemWidth / 2 - tPaint.getTextSize() / 2, getHeight() - (marginBottom / 2), tPaint);
                }
            }

            PathMeasure pm = new PathMeasure(ballPath, false);
            float fSegmentLen = pm.getLength() / 24;
            float afP[] = {0f, 0f};

            int hour = utils.getHourOfDay();
            pm.getPosTan(fSegmentLen * hour, afP, null);
//        canvas.drawCircle((itemWidth * hour) + itemWidth / 2, afP[1], itemWidth / 2, ballPaint);
            canvas.drawCircle(/*(itemWidth * hour) + itemWidth / 2*/(itemWidth * hour) + itemWidth / 2 /*+ ballStrokePaint.getStrokeWidth()/2*/, afP[1], text_size_large * 1.1f/*itemWidth/3*/, ballPaint);
            canvas.drawCircle(/*(itemWidth * hour) + itemWidth / 2*/(itemWidth * hour) + itemWidth / 2 /*+ ballStrokePaint.getStrokeWidth()/2*/, afP[1], text_size_large * 1.1f/*itemWidth/3*/, ballStrokePaint);
            tPaint.setTextSize(text_size_large);
//            canvas.drawText(tempList.get(hour).y < 10 ? "0" + tempList.get(hour).y + "°" : tempList.get(hour).y + "°", afP[0] /*+ tPaint.getTextSize() / 2*//*(itemWidth * hour) - tPaint.getTextSize()*/, getHeight() - (2.5f * marginBottom), tPaint);
//            canvas.drawText(tempList.get(hour).y < 10 ? "0" + tempList.get(hour).y + "°" : tempList.get(hour).y + "°", /*afP[0]*/(itemWidth * hour) + itemWidth / 2 - tPaint.getTextSize() / 2 - ballStrokePaint.getStrokeWidth() / 2/*+tPaint.getTextSize()/2*//*+ tPaint.getTextSize() / 2*//*(itemWidth * hour) - tPaint.getTextSize()*/, afP[1] + tPaint.getTextSize() / 2 - ballStrokePaint.getStrokeWidth() / 2, tPaint);
            canvas.drawText(currentTemperature < 10 ? "0" + currentTemperature + "°" : currentTemperature + "°", /*afP[0]*/(itemWidth * hour) + itemWidth / 2 - tPaint.getTextSize() / 2 - ballStrokePaint.getStrokeWidth() / 2/*+tPaint.getTextSize()/2*//*+ tPaint.getTextSize() / 2*//*(itemWidth * hour) - tPaint.getTextSize()*/, afP[1] + tPaint.getTextSize() / 2 - ballStrokePaint.getStrokeWidth() / 2, tPaint);

            int sunriseHour = Integer.parseInt(forecastModelList.get(0).getSunRiseHour());
            int sunsetHour = Integer.parseInt(forecastModelList.get(0).getSunSetHour());

            sunRise.setBounds((int) ((itemWidth * sunriseHour) + itemWidth / 2) - sunsize, (int) (getHeight() - 30 * getResources().getDisplayMetrics().density - sunsize - sunsize / 2), (int) ((itemWidth * sunriseHour) + itemWidth / 2) + sunsize, (int) (getHeight() - 30 * getResources().getDisplayMetrics().density));
            sunRise.draw(canvas);
            canvas.drawLine((itemWidth * sunriseHour) + itemWidth / 2, getHeight() - (marginBottom * 2), (itemWidth * sunriseHour) + itemWidth / 2, getHeight() - 30 * getResources().getDisplayMetrics().density /*getHeight() - tempList2.get(5).y */, tPaint);
            tPaint.setTextSize(text_size_medium);
            canvas.drawText(tempList.get(sunriseHour).y < 10 ? "0" + tempList.get(sunriseHour).y + "°" : tempList.get(sunriseHour).y + "°", (itemWidth * sunriseHour) + tPaint.getTextSize() * 1.4f/*/1.8f*/, getHeight() - (2.5f * marginBottom), tPaint);

            sunSet.setBounds((int) ((itemWidth * sunsetHour) + itemWidth / 2) - sunsize, (int) (getHeight() - 30 * getResources().getDisplayMetrics().density - sunsize - sunsize / 2), (int) ((itemWidth * sunsetHour) + itemWidth / 2) + sunsize, (int) (getHeight() - 30 * getResources().getDisplayMetrics().density));
            sunSet.draw(canvas);
            canvas.drawLine((itemWidth * sunsetHour) + itemWidth / 2, getHeight() - (marginBottom * 2), (itemWidth * sunsetHour) + itemWidth / 2, getHeight() - 30 * getResources().getDisplayMetrics().density, tPaint);
            tPaint.setTextSize(text_size_medium);
            canvas.drawText(tempList.get(sunsetHour).y < 10 ? "0" + tempList.get(sunsetHour).y + "°" : tempList.get(sunsetHour).y + "°", (itemWidth * sunsetHour) + tPaint.getTextSize() * 1.4f/*/1.8f*/, getHeight() - (2.5f * marginBottom), tPaint);
        } else {
            Log.i("can Draw heatmap= ", "false------------------------------------");
        }
    }
}
