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

/**
 * DistancedSort
 */
class DistancedSort extends SortFunction {

    private final long interObjectDelay;
    private final boolean reversed;

    /**
     * Establishes the delay between object animations based on distance and a delay
     *
     * @param interObjectDelay delay between object animations
     * @param reversed flag to indicate if the animation should be reversed
     */
    DistancedSort(long interObjectDelay, boolean reversed) {
        this.interObjectDelay = interObjectDelay;
        this.reversed = reversed;
    }

    @Override
    public List<SpruceTimedView> getViewListWithTimeOffsets(ViewGroup parent, List<View> children) {
        final PointF comparisonPoint = getDistancePoint(parent, children);
        double lastDistance = getDistanceBetweenPoints(Utils.viewToPoint(children.get(0)), comparisonPoint);
        long currentTimeOffset = 0L;
        List<SpruceTimedView> childViews = new ArrayList<>();

        for (View childView : children) {
            if (lastDistance != getDistanceBetweenPoints(Utils.viewToPoint(childView), comparisonPoint)) {
                lastDistance = getDistanceBetweenPoints(Utils.viewToPoint(childView), comparisonPoint);
                currentTimeOffset += interObjectDelay;
            }
            childViews.add(new SpruceTimedView(childView, currentTimeOffset));
        }

        return childViews;
    }

    @Override
    public void sortChildren(ViewGroup parent, List<View> children) {
        final PointF comparisonPoint = getDistancePoint(parent, children);
        Collections.sort(children, new Comparator<View>() {
            @Override
            public int compare(View left, View right) {
                double leftDistance = getDistanceBetweenPoints(Utils.viewToPoint(left), comparisonPoint);
                double rightDistance = getDistanceBetweenPoints(Utils.viewToPoint(right), comparisonPoint);
                if (reversed) {
                    return Double.compare(rightDistance, leftDistance);
                }
                return Double.compare(leftDistance, rightDistance);
            }
        });
    }

    /**
     * Get the point that's closest to the start point
     *
     * @param children List of views to sort by distance
     * @return PointF of the distance between a point and 0, 0
     */
    public PointF getDistancePoint(ViewGroup parent, List<View> children) {
        PointF distancePoint = new PointF(0, 0);
        return translate(distancePoint, children);
    }

    /**
     * Find the double value of the distance between two points.
     * See {@link Utils#euclideanDistance(PointF, PointF) euclideanDistance} method.
     *
     * @param left  PointF
     * @param right PointF
     * @return the euclidean distance (float value) between the parameter points
     */
    public double getDistanceBetweenPoints(PointF left, PointF right) {
        return Utils.euclideanDistance(left, right);
    }

    /**
     * Return the view that is the closest to the distancePoint
     *
     * @param distancePoint Point for comparison
     * @param children List of Views to compare
     * @return Point closet to the distance point
     */
    PointF translate(final PointF distancePoint, List<View> children) {
        Collections.sort(children, new Comparator<View>() {
            @Override
            public int compare(View left, View right) {
                double leftDistance = getDistanceBetweenPoints(Utils.viewToPoint(left), distancePoint);
                double rightDistance = getDistanceBetweenPoints(Utils.viewToPoint(right), distancePoint);
                return Double.compare(leftDistance, rightDistance);
            }
        });
        return Utils.viewToPoint(children.get(0));
    }

}