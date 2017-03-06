/*
 *     Spruce
 *
 *     Copyright (c) 2017 WillowTree, Inc.
 *     Permission is hereby granted, free of charge, to any person obtaining a copy
 *     of this software and associated documentation files (the "Software"), to deal
 *     in the Software without restriction, including without limitation the rights
 *     to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *     copies of the Software, and to permit persons to whom the Software is
 *     furnished to do so, subject to the following conditions:
 *     The above copyright notice and this permission notice shall be included in
 *     all copies or substantial portions of the Software.
 *     THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *     IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *     FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *     AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *     LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *     OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *     THE SOFTWARE.
 *
 */

package com.willowtreeapps.spruce.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;

/**
 * Convenience methods for retrieving default view animators
 */
public class DefaultAnimations {

    private static final float GROW_SCALE = 1.5F;
    private static final float SHRINK_SCALE = 0.1F;
    private static final float ORIGINAL_SCALE = 1.0F;
    private static final float FADE_AWAY_TO = 0.0F;
    private static final float FADE_IN_TO = 1.0F;
    private static final float START_ROTATION = 0F;
    private static final float END_ROTATION = 360F;

    public static Animator growAnimator(View view, long duration) {
        return ObjectAnimator.ofPropertyValuesHolder(view,
                PropertyValuesHolder.ofFloat(View.SCALE_X, GROW_SCALE, ORIGINAL_SCALE),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, GROW_SCALE, ORIGINAL_SCALE))
                .setDuration(duration);
    }

    public static Animator shrinkAnimator(View view, long duration) {
        return ObjectAnimator.ofPropertyValuesHolder(view,
                PropertyValuesHolder.ofFloat(View.SCALE_X, SHRINK_SCALE, ORIGINAL_SCALE),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, SHRINK_SCALE, ORIGINAL_SCALE))
                .setDuration(duration);
    }

    public static Animator fadeAwayAnimator(View view, long duration) {
        return ObjectAnimator.ofFloat(view, View.ALPHA, FADE_AWAY_TO)
                .setDuration(duration);
    }

    public static Animator fadeInAnimator(View view, long duration) {
        return ObjectAnimator.ofFloat(view, View.ALPHA, FADE_IN_TO)
                .setDuration(duration);
    }

    public static Animator spinAnimator(View view, long duration) {
        return ObjectAnimator.ofFloat(view, View.ROTATION, START_ROTATION, END_ROTATION)
                .setDuration(duration);
    }

}
