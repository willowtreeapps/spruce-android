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

package com.willowtreeapps.spruce.sort;

import android.graphics.PointF;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ContinuousSort extends RadialSort {

    private final long duration;
    private final boolean reversed;
    private double maxDistance;

    /**
     * Establishes the delay between object animations and their starting position based on distance,
     * delay, and a value from the Position enum
     *
     * @param interObjectDelay delay between object animations
     * @param reversed flag to indicate if the animation should be reversed
     * @param position enum value of the position the animation should start from
     */
    public ContinuousSort(long interObjectDelay, boolean reversed, Position position) {
        super(interObjectDelay, reversed, position);
        this.duration = interObjectDelay;
        this.reversed = reversed;
    }

    @Override
    public List<SpruceTimedView> getViewListWithTimeOffsets(ViewGroup parent, List<View> children) {
        final PointF comparisonPoint = getDistancePoint(parent, children);

        Collections.sort(children, new Comparator<View>() {
            @Override
            public int compare(View left, View right) {
                double leftDistance = getDistanceBetweenPoints(Utils.viewToPoint(left), comparisonPoint);
                double rightDistance = getDistanceBetweenPoints(Utils.viewToPoint(right), comparisonPoint);
                if (leftDistance > rightDistance && leftDistance > maxDistance) {
                    maxDistance = leftDistance;
                }
                return Double.compare(leftDistance, rightDistance);
            }
        });

        List<SpruceTimedView> timedViews = new ArrayList<>();
        for (View view : children) {
            double normalizedDistance;
            double viewDistance = getDistanceBetweenPoints(Utils.viewToPoint(view), comparisonPoint);
            if (reversed) {
                normalizedDistance = (maxDistance - viewDistance) / maxDistance;
            } else {
                normalizedDistance = viewDistance / maxDistance;
            }

            long offset = Math.round(duration * normalizedDistance);
            timedViews.add(new SpruceTimedView(view, offset));
        }

        return timedViews;
    }
}
