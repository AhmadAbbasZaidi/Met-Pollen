package com.cfp.metpollen.view.customViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.cfp.metpollen.R;
import com.cfp.metpollen.view.utilities.utils;

/**
 * Created by AhmedAbbas on 10/24/2017.
 */

public class ArcView extends View {

    private final Paint mPaint, paint;
    private final RectF mRect;
    private final Paint tPaint;
    private final Drawable sun;
    private float textSize;
    private float arcAngle;
    private float textPadding;

    private int iCurStep = 0;
    private float sunSize = 15;
    private float circleSize = 0;
    private float padding = 0;

    private String date;
    private String riseTime = "06:30";
    private String setTime = "19:00";

    public ArcView(Context context, AttributeSet attrs) {

        super(context, attrs);
//        setLayerType(View.LAYER_TYPE_SOFTWARE,null);


        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ArcView, 0, 0);

        // Set Angle to 0 initially
        arcAngle = 0;

        /*paint to fill background*/
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(5);
        mPaint.setColor(getResources().getColor(R.color.white_overlay));

        /*paint to draw line and circles*/
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(2);
        paint.setColor(Color.YELLOW);

        /*paint to draw text*/
        tPaint = new Paint();
        tPaint.setAntiAlias(true);
        tPaint.setStyle(Paint.Style.FILL);
        tPaint.setStrokeWidth(1);
        tPaint.setColor(Color.WHITE);
        tPaint.setTextSize(24f);


        mRect = new RectF(100, 100, 500, 500);

        sun = getResources().getDrawable(R.drawable.sun);

        sunSize = ta.getFloat(R.styleable.ArcView_sun_size, (float) (getHeight() * 0.05f));
        circleSize = ta.getFloat(R.styleable.ArcView_circle_radius, (float) (getHeight() * 0.03f));
        textSize = ta.getFloat(R.styleable.ArcView_textsize, (float) (getHeight() * 0.07f));
        padding = ta.getFloat(R.styleable.ArcView_padding, (float) (getHeight() * 0.2f));
        textPadding = ta.getFloat(R.styleable.ArcView_textPadding, (float) (textSize * 0.2f));

        float density = getResources().getDisplayMetrics().density;

        sunSize = (int) (sunSize * density);
        circleSize = (int) (circleSize * density);
        textSize = (int) (textSize * density);
        padding = (int) (padding * density);

        tPaint.setTextSize(textSize);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRiseTime() {
        return riseTime;
    }

    public void setRiseTime(String riseTime) {
        this.riseTime = riseTime;
    }

    public String getSetTime() {
        return setTime;
    }

    public void setSetTime(String setTime) {
        this.setTime = setTime;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.DST);

//        tPaint.setTextSize(((getWidth()- getHeight())/2) / 15 * getResources().getDisplayMetrics().density);
//        sunSize = (int) ((getWidth()- getHeight()) / 40 * getResources().getDisplayMetrics().density);
//        circleSize = (int) ((getWidth()- getHeight()) / 80 * getResources().getDisplayMetrics().density);
//        float padding = getHeight() / 8 * getResources().getDisplayMetrics().density;

//        sunSize = (int) (getHeight()*0.05f);
//        circleSize = (int) (getHeight()*0.03f);
//        tPaint.setTextSize(getHeight()*0.07f);
//        padding = getHeight() * 0.2f;

//      canvas.save();
        float width = getWidth() - padding - padding;//this.getLayoutParams().height;//getHeight();//getWidth() / 3f;

        /*width of arc*/
//        float cx = getWidth()/1.25f/*width/1.25f*/ + padding/**1.75f*/;
        float cx = getWidth() - getWidth() / 2f /*width/1.25f*/ /**1.75f*/;
        /*height of arc*/
        float cy = getHeight() /*+ padding/2*/ - tPaint.getTextSize() * 3;


        /*size of dot for sun path*/
        float scaleMarkSize = getResources().getDisplayMetrics().density;
        float radius = getHeight() - getHeight() * 0.30f - padding - textSize/*padding*//*getHeight()/1.25f*/;

        float starting_left = padding;
        float starting_bottom = cy;

        float ending_right = width + padding + padding + padding + padding;
        float ending_bottom = cy;

        /*setting values for starting point indicator*/
        float starting_size = 5;
        float radius_starting = starting_size / 2;
        float scaleMarkSize_starting = getResources().getDisplayMetrics().density * radius_starting; // 16dp

       /* *//*fill graph start and end circle*//*
        for (float i = 0; i <= 360; i += 0.5) {
            float angle = (float) Math.toRadians(i); // Need to convert to radians first

            float startX = (float) (starting_left + starting_size * Math.sin(angle));
            float startY = (float) (starting_bottom - starting_size * Math.cos(angle));
            float stopX = (float) (starting_left + (starting_size - scaleMarkSize_starting) * Math.sin(angle));
            float stopY = (float) (starting_bottom - (starting_size - scaleMarkSize_starting) * Math.cos(angle));

            float start2X = (float) (ending_right + starting_size * Math.sin(angle));
            float start2Y = (float) (ending_bottom - starting_size * Math.cos(angle));
            float stop2X = (float) (ending_right + (starting_size - scaleMarkSize_starting) * Math.sin(angle));
            float stop2Y = (float) (ending_bottom - (starting_size - scaleMarkSize_starting) * Math.cos(angle));

            canvas.drawLine(startX, startY, stopX, stopY, paint);
            canvas.drawLine(start2X, start2Y, stop2X, stop2Y, paint);
        }*/

        /*ploting arc points*/
        for (float i = -90; i <= 90; i += 3) {
            float angle = (float) Math.toRadians(i); // Need to convert to radians first

            float startX = (float) (cx + radius * Math.sin(angle));
            float startY = (float) (cy - radius * Math.cos(angle));

            float stopX = (float) (cx + (radius - scaleMarkSize) * Math.sin(angle));
            float stopY = (float) (cy - (radius - scaleMarkSize) * Math.cos(angle));

            canvas.drawLine(startX, startY, stopX, stopY, paint);

            if (i == -90) {
//                fill graph start yellow circle
                canvas.drawCircle(startX, startY, circleSize, paint);
            } else if (i == 90) {
//                fill graph end yellow circle
                canvas.drawCircle(stopX, stopY, circleSize, paint);
            }

        }

        Path fill = new Path();
        Path sunPath = new Path();

        /*plotting graph background fill*/
        for (float i = -90; i <= 90; i += 0.5) {
            float angle = (float) Math.toRadians(i); // Need to convert to radians first

            float startX = (float) (cx + radius * Math.sin(angle));
            float startY = (float) (cy - radius * Math.cos(angle));


            float stopX = (float) (cx + (radius - scaleMarkSize) * Math.sin(angle));
            float stopY = (float) (cy - (radius - scaleMarkSize) * Math.cos(angle));


            if (i == -90) {
                fill.moveTo(startX, startY);
                fill.lineTo(startX, startY);
                sunPath.moveTo(startX, startY);
                sunPath.lineTo(startX, startY);
                canvas.drawText(utils.getConvertedDateFromOneFormatToOther(riseTime, utils.FORMAT6, utils.FORMAT5), (float) (cx - radius - tPaint.getTextSize()/** scaleMarkSize*/), startY + 1.5f * tPaint.getTextSize() + textPadding/*+ padding*/, tPaint);
                canvas.drawText(utils.getConvertedDateFromOneFormatToOther(setTime, utils.FORMAT6, utils.FORMAT5), (float) (cx + (radius - tPaint.getTextSize() /** scaleMarkSize*/) * Math.sin(90)), cy + 1.5f * tPaint.getTextSize() + textPadding /*+ padding*/, tPaint);
            } else if (i > -90 && i < arcAngle - 90) {
                fill.lineTo(stopX, stopY);
                sunPath.lineTo(stopX, stopY);
                fill.lineTo(stopX, cy);
            } else if (i == arcAngle - 90) {
                fill.lineTo(stopX, startY);
                sunPath.lineTo(stopX, startY);
                fill.lineTo(stopX, cy);
            }
        }
        fill.close();
        if (arcAngle >= 0 && arcAngle <= 180) {
            canvas.drawPath(fill, mPaint);
        }
        /*draw horizontal line*/
        canvas.drawLine(0, cy, getWidth(), cy, paint);


        if (arcAngle >= 0 && arcAngle <= 180) {
             /*measuring path for sun movement*/
            PathMeasure pm = new PathMeasure(sunPath, false);
            float fSegmentLen = pm.getLength();//pm.getLength() / 20;//we'll get 20 points from path to animate the circle
            float afP[] = {100f, 100f};

        /*setting points and drawing sun image */
            if (iCurStep < fSegmentLen) {
                pm.getPosTan(fSegmentLen * iCurStep, afP, null);
//            canvas.drawCircle(afP[0], afP[1], 30, mPaint);
                sun.setBounds((int) (afP[0] - sunSize), (int) (afP[1] - sunSize), (int) (afP[0] + sunSize), (int) (afP[1] + sunSize));
                sun.draw(canvas);
                iCurStep++;

            } else {
                iCurStep = 0;
            }
        }
//        canvas.restore();

    }

    public float getArcAngle() {
        return arcAngle;
    }

    public void setArcAngle(float arcAngle) {
        this.arcAngle = arcAngle;
    }

}