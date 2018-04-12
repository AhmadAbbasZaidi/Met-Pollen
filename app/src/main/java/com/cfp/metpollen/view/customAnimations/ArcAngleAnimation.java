package com.cfp.metpollen.view.customAnimations;

import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.cfp.metpollen.view.customViews.ArcView;

/**
 * Created by AhmedAbbas on 10/24/2017.
 */

public class ArcAngleAnimation extends Animation {

    private ArcView arcView;

    private float oldAngle;
    private float newAngle;

    public ArcAngleAnimation(ArcView arcView, int newAngle) {
        this.oldAngle = arcView.getArcAngle();
        this.newAngle = newAngle;
        this.arcView = arcView;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation transformation) {
        float angle = (float)(0 + ((newAngle - oldAngle) * interpolatedTime));

        arcView.setArcAngle(angle);
        arcView.requestLayout();
    }
}