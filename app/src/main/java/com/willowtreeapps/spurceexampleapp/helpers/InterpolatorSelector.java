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

package com.willowtreeapps.spurceexampleapp.helpers;

import android.os.Build;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import androidx.annotation.RequiresApi;

import com.willowtreeapps.spruce.interpolators.SpruceInterpolators;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is used to get the interpolator from {@link com.willowtreeapps.spruce.interpolators.SpruceInterpolators}
 * <p>
 * This class is a helper for the Interpolation selection in {@link com.willowtreeapps.spurceexampleapp.fragments.ControlsFragment}
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class InterpolatorSelector {

    private final Map<Integer, Interpolator> interpolatorMap = new HashMap<>();

    public InterpolatorSelector() {
        initializeHashMap();
    }

    /**
     * This hash map is used to the {@link android.app.AlertDialog} in
     * {@link com.willowtreeapps.spurceexampleapp.fragments.ControlsFragment}
     */
    private void initializeHashMap() {
        interpolatorMap.put(0, SpruceInterpolators.EASE);
        interpolatorMap.put(1, SpruceInterpolators.EASE_IN);
        interpolatorMap.put(2, SpruceInterpolators.EASE_OUT);
        interpolatorMap.put(3, SpruceInterpolators.EASE_IN_OUT);
        interpolatorMap.put(4, SpruceInterpolators.EASE_IN_QUAD);
        interpolatorMap.put(5, SpruceInterpolators.EASE_IN_CUBIC);
        interpolatorMap.put(6, SpruceInterpolators.EASE_IN_QUART);
        interpolatorMap.put(7, SpruceInterpolators.EASE_IN_QUINT);
        interpolatorMap.put(8, SpruceInterpolators.EASE_IN_SINE);
        interpolatorMap.put(9, SpruceInterpolators.EASE_IN_EXPO);
        interpolatorMap.put(10, SpruceInterpolators.EASE_IN_CIRC);
        interpolatorMap.put(11, SpruceInterpolators.EASE_IN_BACK);
        interpolatorMap.put(12, SpruceInterpolators.EASE_OUT_QUAD);
        interpolatorMap.put(13, SpruceInterpolators.EASE_OUT_CUBIC);
        interpolatorMap.put(14, SpruceInterpolators.EASE_OUT_QUART);
        interpolatorMap.put(15, SpruceInterpolators.EASE_OUT_QUINT);
        interpolatorMap.put(16, SpruceInterpolators.EASE_OUT_SINE);
        interpolatorMap.put(17, SpruceInterpolators.EASE_OUT_EXPO);
        interpolatorMap.put(18, SpruceInterpolators.EASE_OUT_CIRC);
        interpolatorMap.put(19, SpruceInterpolators.EASE_OUT_BACK);
        interpolatorMap.put(20, SpruceInterpolators.EASE_IN_OUT_QUAD);
        interpolatorMap.put(21, SpruceInterpolators.EASE_IN_OUT_CUBIC);
        interpolatorMap.put(22, SpruceInterpolators.EASE_IN_OUT_QUART);
        interpolatorMap.put(23, SpruceInterpolators.EASE_IN_OUT_QUINT);
        interpolatorMap.put(24, SpruceInterpolators.EASE_IN_OUT_SINE);
        interpolatorMap.put(25, SpruceInterpolators.EASE_IN_OUT_EXPO);
        interpolatorMap.put(26, SpruceInterpolators.EASE_IN_OUT_CIRC);
        interpolatorMap.put(27, SpruceInterpolators.EASE_IN_OUT_BACK);
    }

    /**
     * This method returns the interpolator for a specific position from the {@link HashMap}
     *
     * @param position position from the {@link android.app.AlertDialog}
     * @return {@link Interpolator} that is selected view {@link android.app.AlertDialog}
     */
    public Interpolator getInterpolatorMap(int position) {
        if (interpolatorMap.containsKey(position)) {
            return interpolatorMap.get(position);
        } else {
            return new LinearInterpolator();
        }
    }
}
