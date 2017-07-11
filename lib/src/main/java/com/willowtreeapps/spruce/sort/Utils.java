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

import java.util.List;

/**
 * Utility class for distance calculations and view to point conversion
 */
class Utils {

    /**
     * Get the euclidean distance between two points
     *
     * @param firstPoint PointF object
     * @param secondPoint PointF object
     * @return float value representing the distance in a straight line between two points
     */
    public static float euclideanDistance(PointF firstPoint, PointF secondPoint) {
        return PointF.length(secondPoint.x - firstPoint.x, secondPoint.y - firstPoint.y);
    }

    /**
     * Get the horizontal, or x distance between two points
     *
     * @param firstPoint PointF object
     * @param secondPoint PointF object
     * @return float value representing the horizontal (or x) distance between two points
     */
    public static float horizontalDistance(PointF firstPoint, PointF secondPoint) {
        return Math.abs(secondPoint.x - firstPoint.x);
    }

    /**
     * Get the vertical, or y distance between two points
     *
     * @param firstPoint PointF object
     * @param secondPoint PointF object
     * @return float value representing the vertical (or y) distance between two points
     */
    public static float verticalDistance(PointF firstPoint, PointF secondPoint) {
        return Math.abs(secondPoint.y - firstPoint.y);
    }

    /**
     * Convert a view into it's coordinates as a Point
     *
     * @param view Object
     * @return PointF containing the x, y coordinates of the view
     */
    public static PointF viewToPoint(View view) {
        return new PointF(Math.round(view.getX()), Math.round(view.getY()));
    }

}
