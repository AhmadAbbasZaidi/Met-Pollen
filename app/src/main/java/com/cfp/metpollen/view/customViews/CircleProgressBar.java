package com.cfp.metpollen.view.customViews;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.cfp.metpollen.R;

/**
 * Created by AhmedAbbas on 11/20/2017.
 */

public class CircleProgressBar extends View {


    /**
     * ProgressBar's line thickness
     */
    private float strokeWidth = 8 * getResources().getDisplayMetrics().density;
    private float progress = 0;
    private int min = 0;
    private int max = 100;
    /**
     * Start the progress at 12 o'clock
     */
    private int startAngle = 90;
    private int backgroundColor = 0xCCFFFFFF;//Color.WHITE;//getResources().getColor(R.color.white_overlay);
    private int color = 0xFF48bb03/*Color.GREEN*/;
    private RectF rectF;
    private Paint backgroundPaint;
    private Paint foregroundPaint;
    private Paint backgroundColorPaint;
    private RectF rectCollorF;

    public CircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        rectF = new RectF();
        rectCollorF = new RectF();
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CircleProgressBar,
                0, 0);
        //Reading values from the XML layout
        strokeWidth = typedArray.getFloat(R.styleable.CircleProgressBar_progressBarThickness, strokeWidth);
        progress = typedArray.getFloat(R.styleable.CircleProgressBar_progress, progress);
        color = typedArray.getInt(R.styleable.CircleProgressBar_progressbarColor, color);
        min = typedArray.getInt(R.styleable.CircleProgressBar_min, min);
        max = typedArray.getInt(R.styleable.CircleProgressBar_max, max);


        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setDither(true);
        backgroundPaint.setStrokeCap(Paint.Cap.ROUND);
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setColor(backgroundColor /*adjustAlpha(backgroundColor, 0.3f)*/);
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(strokeWidth);

        backgroundColorPaint = new Paint();
        backgroundColorPaint.setDither(true);
        backgroundColorPaint.setStrokeCap(Paint.Cap.ROUND);
        backgroundColorPaint.setAntiAlias(true);
        backgroundColorPaint.setColor(getResources().getColor(R.color.white_overlay_pollen) /*adjustAlpha(backgroundColor, 0.3f)*/);
        backgroundColorPaint.setStyle(Paint.Style.FILL/*_AND_STROKE*/);
        backgroundColorPaint.setStrokeWidth(strokeWidth);

        foregroundPaint = new Paint(/*Paint.ANTI_ALIAS_FLAG*/);
        foregroundPaint.setColor(color);
        foregroundPaint.setDither(true);
        foregroundPaint.setAntiAlias(true);
        foregroundPaint.setStyle(Paint.Style.STROKE);
        foregroundPaint.setStrokeWidth(strokeWidth);
    }

    private int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        final int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        final int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int min = Math.min(width, height);
        setMeasuredDimension(min, min);
        rectF.set(0 + strokeWidth / 2, 0 + strokeWidth / 2, min - strokeWidth / 2, min - strokeWidth / 2);
        rectCollorF.set(0 + strokeWidth/*/ 2*/, 0 + strokeWidth/*/ 2*/, min - strokeWidth/*/ 2*/, min - strokeWidth/*/ 2*/);
    }

    @SuppressLint("LongLogTag")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        Log.i("getResources().getDisplayMetrics().density", getResources().getDisplayMetrics().density + "");
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.OVERLAY);
        canvas.drawArc(rectCollorF, 0, 360, false, backgroundColorPaint);
        canvas.drawOval(rectF, backgroundPaint);
        float angle = (float)360 * progress / max;
        canvas.drawArc(rectF, startAngle, angle, false, foregroundPaint);

    }

    public void setProgressWithAnimation(float progress) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "progress", progress);
        objectAnimator.setDuration(5000);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.start();
    }

    public int lightenColor(int color, float factor) {
        float r = Color.red(color) * factor;
        float g = Color.green(color) * factor;
        float b = Color.blue(color) * factor;
        int ir = Math.min(255, (int) r);
        int ig = Math.min(255, (int) g);
        int ib = Math.min(255, (int) b);
        int ia = Color.alpha(color);
        return (Color.argb(ia, ir, ig, ib));
    }


    public float getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
        foregroundPaint.setStrokeWidth(strokeWidth);
        backgroundPaint.setStrokeWidth(strokeWidth);
//        invalidate();// Notify the view to redraw it self (the onDraw method is called)
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();// Notify the view to redraw it self (the onDraw method is called)
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
//        invalidate();// Notify the view to redraw it self (the onDraw method is called)
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
//        invalidate();// Notify the view to redraw it self (the onDraw method is called)
    }

    public int getStartAngle() {
        return startAngle;
    }

    public void setStartAngle(int startAngle) {
        this.startAngle = startAngle;
        invalidate();// Notify the view to redraw it self (the onDraw method is called)
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        foregroundPaint.setColor(color);
//        invalidate();// Notify the view to redraw it self (the onDraw method is called)
    }

    public RectF getRectF() {
        return rectF;
    }

    public void setRectF(RectF rectF) {
        this.rectF = rectF;
        invalidate();// Notify the view to redraw it self (the onDraw method is called)
    }

    public Paint getBackgroundPaint() {
        return backgroundPaint;
    }

    public void setBackgroundPaint(Paint backgroundPaint) {
        this.backgroundPaint = backgroundPaint;
        invalidate();// Notify the view to redraw it self (the onDraw method is called)
    }

    public Paint getForegroundPaint() {
        return foregroundPaint;
    }

    public void setForegroundPaint(Paint foregroundPaint) {
        this.foregroundPaint = foregroundPaint;
        invalidate();// Notify the view to redraw it self (the onDraw method is called)
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}