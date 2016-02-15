package com.example.jzj.androidanimation;

import android.view.animation.Interpolator;

/**
 * Created by jzj on 15/8/5.
 */
public class MyInterpolator implements Interpolator {

    float[] rawStep; // 1f, 0.5f, 0.5f, 0.25f, 0.25f, 0.125f, 0.125f, ...
    float[] normalizedStep; // 1k, 0.5k, 0.5k, ...
    float[] normalizedStepSum; // 0, 1k, (1+0.5)k, (1+0.5+0.5)k, ..., 1
    float sum;

    public MyInterpolator() {
        final int k = 10;
        rawStep = new float[2 * k + 1];
        normalizedStep = new float[rawStep.length];
        normalizedStepSum = new float[rawStep.length + 1];

        rawStep[0] = 1f;
        float f = 1f;
        for (int i = 0; i < k; ++i) {
            rawStep[2 * i] = f;
            rawStep[2 * i + 1] = f;
            f /= 2f;
        }

        sum = 0;
        for (float f1 : rawStep) {
            sum += f1;
        }

        normalizedStepSum[0] = 0;
        for (int i = 0; i < rawStep.length; ++i) {
            normalizedStep[i] = rawStep[i] / sum;
            normalizedStepSum[i + 1] = normalizedStepSum[i] + normalizedStep[i];
        }
    }

    public float getInterpolation(float input) {
        float r = 1;
        for (int i = 0; i < normalizedStep.length; ++i) {
            if (input < normalizedStepSum[i + 1]) {
                float x = (input - normalizedStepSum[i]) / normalizedStep[i]; // x = 0~1
                if (i % 2 == 0) { // down
                    return accelerate(x, r);
                } else { // up
                    return decelerate(x, r);
                }
            }
            if (i % 2 == 0) {
                r /= 2;
            }
        }
        return input;
    }

    /**
     * @param x
     * @param range
     * @return 1-range ~ 1
     */
    float accelerate(float x, float range) {
        return x * x * range + 1 - range;
    }

    /**
     * @param x
     * @param range
     * @return 1-range ~ 1
     */
    float decelerate(float x, float range) {
        return (1 - x) * (1 - x) * range + 1 - range;
    }
}