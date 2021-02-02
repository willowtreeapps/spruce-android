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

package com.willowtreeapps.spruce.dynamics;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is eqivalent to {@link android.view.animation.AnimationSet} for dynamic animations.
 */
public class DynamicAnimatorSet {

    private List<DynamicAnimation<?>> anim = new ArrayList<>();

    /**
     * Sets up this AnimatorSet to play all of the supplied animations at the same time.
     *
     * @param items The animations that will be started simultaneously.
     */
    public void playTogether(List<DynamicAnimation<?>> items) {
        anim = items;
    }

    /**
     * This method is used to cancel the animation.
     */
    public void cancel() {
        for (DynamicAnimation<?> anim : anim) {
            anim.cancel();
        }
    }


    /**
     * This method is used to start all queued dynamic animations.
     */
    public void start() {
        for (DynamicAnimation<?> anim : anim) {
            anim.start();
        }
    }
}
