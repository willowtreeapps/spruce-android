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

public class CorneredSort extends DistancedSort {

    public enum Corner {
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT
    }

    private final long interObjectDelay;
    private final Corner corner;
    private final boolean reversed;

    /**
     * Animates views in a corner like fashion. The views near the starting corner will animate first.
     *
     * @param interObjectDelay long delay between objects
     * @param reversed boolean
     * @param corner Corner enum value {@link Corner corner}
     */
    public CorneredSort(long interObjectDelay, boolean reversed, Corner corner) {
        super(interObjectDelay, reversed);
        if (corner == null) {
            throw new NullPointerException("Corner can't be null and must be a valid type");
        }
        this.interObjectDelay = interObjectDelay;
        this.corner = corner;
        this.reversed = reversed;
    }

    @Override
    public List<SpruceTimedView> getViewListWithTimeOffsets(ViewGroup parent, List<View> children) {
        final PointF comparisonPoint = getDistancePoint(parent, children);
        List<SpruceTimedView> timedViews = new ArrayList<>();
        long currentTimeOffset = 0;

        double lastDistance = 0;
        for (View view : children) {
            double viewDistance = getDistanceBetweenPoints(Utils.viewToPoint(view), comparisonPoint);
            if (Math.floor(lastDistance) != Math.floor(viewDistance)) {
                lastDistance = viewDistance;
                currentTimeOffset += interObjectDelay;
            }
            timedViews.add(new SpruceTimedView(view, currentTimeOffset));
        }

        return timedViews;
    }

    @Override
    public void sortChildren(ViewGroup parent, List<View> children) {
        final PointF comparisonPoint = getDistancePoint(parent, children);
        Collections.sort(children, new Comparator<View>() {
            @Override
            public int compare(View left, View right) {
                double leftDistance = Math.abs(comparisonPoint.x - left.getX()) + Math.abs(comparisonPoint.y - left.getY());
                double rightDistance = Math.abs(comparisonPoint.x - right.getX()) + Math.abs(comparisonPoint.y - right.getY());
                if (reversed) {
                    return Double.compare(rightDistance, leftDistance);
                }
                return Double.compare(leftDistance, rightDistance);
            }
        });
    }

    @Override
    public PointF getDistancePoint(ViewGroup parent, List<View> children) {
        PointF distancePoint;

        switch (corner) {
            case TOP_LEFT:
                distancePoint = new PointF(0, 0);
                break;
            case TOP_RIGHT:
                distancePoint = new PointF(parent.getWidth(), 0);
                break;
            case BOTTOM_LEFT:
                distancePoint = new PointF(0, parent.getHeight());
                break;
            case BOTTOM_RIGHT:
                distancePoint = new PointF(parent.getWidth(), parent.getHeight());
                break;
            default:
                throw new AssertionError("Must be a valid Corner argument type");
        }

        return distancePoint;
    }

}
