package com.example.jzj.androidanimation;

import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by jzj on 15/8/5.
 */
public class MyAnimation extends Animation {

    private float mFromXValue = 0.0f;
    private float mFromYValue = 0.0f;
    private float mFromXDelta;
    private float mFromYDelta;

    public MyAnimation(float fromXDelta, float fromYDelta) {
        mFromXValue = fromXDelta;
        mFromYValue = fromYDelta;
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        mFromXDelta = parentWidth * mFromXValue;
        mFromYDelta = parentHeight * mFromYValue;
    }

    /**
     * @param interpolatedTime 0~1f
     * @param t
     */
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        float dx = mFromXDelta + ((0 - mFromXDelta) * interpolatedTime);
        float dy = mFromYDelta + ((0 - mFromYDelta) * interpolatedTime);
        final Matrix matrix = t.getMatrix();
        // pre-方法向前生长，post-方法向后生长，set-方法先清空已有变换，再进行变换
        matrix.setTranslate(dx, dy);
    }
}
