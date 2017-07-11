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

public class InlineSort extends CorneredSort {

    private final long interObjectDelay;
    private final boolean reversed;

    /**
     * Animate child views from side to side (based on the provided corner parameter).
     *
     * @param interObjectDelay long delay between objects
     * @param reversed boolean indicating if the selection is reversed
     * @param corner {@link com.willowtreeapps.spruce.sort.CorneredSort.Corner Corner} value to start from
     */
    public InlineSort(long interObjectDelay, boolean reversed, Corner corner) {
        super(interObjectDelay, reversed, corner);
        this.interObjectDelay = interObjectDelay;
        this.reversed = reversed;
    }

    @Override
    public List<SpruceTimedView> getViewListWithTimeOffsets(ViewGroup parent, List<View> children) {
        List<SpruceTimedView> timedViews = new ArrayList<>();
        long currentTimeOffset = 0;

        if (reversed) {
            Collections.reverse(children);
        }

        for (View view : children) {
            timedViews.add(new SpruceTimedView(view, currentTimeOffset));
            currentTimeOffset += interObjectDelay;
        }

        return timedViews;
    }

    @Override
    public void sortChildren(ViewGroup parent, List<View> children) {
        final PointF comparisonPoint = getDistancePoint(parent, children);
        Collections.sort(children, new Comparator<View>() {
            @Override
            public int compare(View left, View right) {
                double leftHorizontalDistance = Utils.horizontalDistance(comparisonPoint, Utils.viewToPoint(left));
                double leftVerticalDistance = Utils.verticalDistance(comparisonPoint, Utils.viewToPoint(left));
                double rightHorizontalDistance = Utils.horizontalDistance(comparisonPoint, Utils.viewToPoint(right));
                double rightVerticalDistance = Utils.verticalDistance(comparisonPoint, Utils.viewToPoint(right));

                if (leftVerticalDistance < rightVerticalDistance ||
                        leftVerticalDistance == rightVerticalDistance &&
                                leftHorizontalDistance < rightHorizontalDistance) {
                    return -1;
                }
                return 1;
            }
        });
    }

}