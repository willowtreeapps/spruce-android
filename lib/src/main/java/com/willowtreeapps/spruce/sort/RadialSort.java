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

import java.util.List;

public class RadialSort extends DistancedSort {

    private final Position position;

    public enum Position {
        TOP_LEFT,
        TOP_MIDDLE,
        TOP_RIGHT,
        LEFT,
        MIDDLE,
        RIGHT,
        BOTTOM_LEFT,
        BOTTOM_MIDDLE,
        BOTTOM_RIGHT
    }

    /**
     * Establishes the delay between object animations and their starting position based on distance,
     * delay, and a value from the Position enum
     *
     * @param interObjectDelay delay between object animations
     * @param reversed flag to indicate if the animation should be reversed
     * @param position enum value of the position the animation should start from
     */
    public RadialSort(long interObjectDelay, boolean reversed, Position position) {
        super(interObjectDelay, reversed);
        if (position == null) {
            throw new NullPointerException("Position can't be null and must be a valid type");
        }
        this.position = position;
    }

    @Override
    public PointF getDistancePoint(ViewGroup parent, List<View> children) {
        PointF distancePoint;

        switch (position) {
            case TOP_LEFT:
                distancePoint = new PointF(0, 0);
                break;
            case TOP_MIDDLE:
                distancePoint = new PointF(parent.getWidth() / 2, 0);
                break;
            case TOP_RIGHT:
                distancePoint = new PointF(parent.getWidth(), 0);
                break;
            case LEFT:
                distancePoint = new PointF(0, parent.getHeight() / 2);
                break;
            case MIDDLE:
                distancePoint = new PointF(parent.getWidth() / 2, parent.getHeight() / 2);
                break;
            case RIGHT:
                distancePoint = new PointF(parent.getWidth(), parent.getHeight() / 2);
                break;
            case BOTTOM_LEFT:
                distancePoint = new PointF(0, parent.getHeight());
                break;
            case BOTTOM_MIDDLE:
                distancePoint = new PointF(parent.getWidth() / 2, parent.getHeight());
                break;
            case BOTTOM_RIGHT:
                distancePoint = new PointF(parent.getWidth(), parent.getHeight());
                break;
            default:
                throw new AssertionError("Must be a valid Position argument type");
        }

        return super.translate(distancePoint, children);
    }

}
