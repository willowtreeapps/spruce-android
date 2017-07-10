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
import android.support.annotation.FloatRange;
import android.support.annotation.VisibleForTesting;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ContinuousWeightedSort extends ContinuousSort {

    public static final double LIGHT_WEIGHT = 0.5;
    public static final double MEDIUM_WEIGHT = 1.0;
    public static final double HEAVY_WEIGHT = 2.0;

    private final long duration;
    private final boolean reversed;
    private final double horizontalWeight;
    private final double verticalWeight;
    private double maxHorizontalDistance;
    private double maxVerticalDistance;

    /**
     * Establishes a weighted delay between object animations and their starting position based on distance,
     * delay, a value from the Position enum, a horizontal value of Weight and
     * a vertical Weight value from the Weight enum
     *
     * @param interObjectDelay delay between object animations
     * @param reversed flag to indicate if the animation should be reversed
     * @param position enum value of the position the animation should start from
     * @param horizontalWeight double value of the horizontal weight
     * @param verticalWeight double value of the vertical weight
     */
    public ContinuousWeightedSort(long interObjectDelay, boolean reversed, Position position,
                                  @FloatRange(from = 0.0) double horizontalWeight,
                                  @FloatRange(from = 0.0) double verticalWeight) {
        super(interObjectDelay, reversed, position);
        this.duration = interObjectDelay;
        this.reversed = reversed;
        if (horizontalWeight < 0) {
            throw new AssertionError("horizontalWeight can not be a negative value");
        } else if (verticalWeight < 0) {
            throw new AssertionError("verticalWeight can not be a negative value");
        }
        this.horizontalWeight = horizontalWeight;
        this.verticalWeight = verticalWeight;
    }

    @Override
    public List<SpruceTimedView> getViewListWithTimeOffsets(ViewGroup parent, List<View> children) {
        final PointF comparisonPoint = getDistancePoint(parent, children);

        List<SpruceTimedView> timedViews = new ArrayList<>();
        long maxTimeOffset = 1;
        for (View view : children) {
            double viewHorizontalDistance = Utils.horizontalDistance(comparisonPoint, Utils.viewToPoint(view)) * horizontalWeight;
            double viewVerticalDistance = Utils.verticalDistance(comparisonPoint, Utils.viewToPoint(view)) * verticalWeight;
            double normalizedHorizontalDistance = viewHorizontalDistance / maxHorizontalDistance;
            double normalizedVerticalDistance = viewVerticalDistance / maxVerticalDistance;
            long offset = Math.round(duration * (normalizedHorizontalDistance * horizontalWeight + normalizedVerticalDistance * verticalWeight));

            if (offset > maxTimeOffset) {
                maxTimeOffset = offset;
            }

            if (reversed) {
                offset = duration - (offset / 2);
            }

            timedViews.add(new SpruceTimedView(view, offset));
        }

        return timedViews;
    }

    @Override
    public void sortChildren(ViewGroup parent, List<View> children) {
        final PointF comparisonPoint = getDistancePoint(parent, children);

        // non-sort to calculate max horizontal/vertical values
        Collections.sort(children, new Comparator<View>() {
            @Override
            public int compare(View left, View right) {
                double leftHorizontalDistance = Utils.horizontalDistance(comparisonPoint, Utils.viewToPoint(left)) * horizontalWeight;
                double rightHorizontalDistance = Utils.horizontalDistance(comparisonPoint, Utils.viewToPoint(right)) * horizontalWeight;
                double leftVerticalDistance = Utils.verticalDistance(comparisonPoint, Utils.viewToPoint(left)) * verticalWeight;
                double rightVerticalDistance = Utils.verticalDistance(comparisonPoint, Utils.viewToPoint(right)) * verticalWeight;

                maxHorizontalDistance = calculateMaxDistance(leftHorizontalDistance, rightHorizontalDistance, maxHorizontalDistance);
                maxVerticalDistance = calculateMaxDistance(leftVerticalDistance, rightVerticalDistance, maxVerticalDistance);

                return 0;
            }
        });
    }

    @VisibleForTesting
    double calculateMaxDistance(double leftHorizontalDistance, double rightHorizontalDistance, double maximum) {
        if (leftHorizontalDistance > rightHorizontalDistance && leftHorizontalDistance > maximum) {
            maximum = leftHorizontalDistance;
        } else if (rightHorizontalDistance > leftHorizontalDistance && rightHorizontalDistance > maximum) {
            maximum = rightHorizontalDistance;
        }
        return maximum;
    }

}