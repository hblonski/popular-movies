package com.popularmovies.util;

import android.animation.ValueAnimator;

import com.airbnb.lottie.LottieAnimationView;

public abstract class LottieHelper {

    //Default animation duration, in milliseconds
    private static final int DEFAULT_DURATION = 1000;

    //Used to represent the state between animations start and end points
    public static final float PROGRESS_START = 0f;
    public static final float PROGRESS_END = 1f;

    //As seen on https://medium.com/@daniel.nesfeder/android-animations-with-lottie-d4aa5a5f237
    public static void startAnimation(final LottieAnimationView animationView) {
        ValueAnimator animator = ValueAnimator.ofFloat(PROGRESS_START, PROGRESS_END).setDuration(DEFAULT_DURATION);
        animator.addUpdateListener(valueAnimator -> animationView.setProgress((Float) valueAnimator.getAnimatedValue()));

        if (animationView.getProgress() == PROGRESS_START) {
            animator.start();
        } else {
            animationView.setProgress(PROGRESS_START);
        }
    }
}
