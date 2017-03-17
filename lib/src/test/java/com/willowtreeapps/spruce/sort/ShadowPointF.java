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

import android.graphics.Point;

import org.robolectric.annotation.Implements;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.RealObject;

/**
 * Shadow implementation of {@code PointF}
 */
@SuppressWarnings({"UnusedDeclaration"})
@Implements(PointF.class)
public class ShadowPointF {
    @RealObject private PointF realPointF;

    public void __constructor__(float x, float y) {
        realPointF.x = x;
        realPointF.y = y;
    }

    public void __constructor__(Point src) {
        realPointF.x = src.x;
        realPointF.y = src.y;
    }

    @Implementation
    public void set(float x, float y) {
        realPointF.x = x;
        realPointF.y = y;
    }

    @Implementation
    public final void negate() {
        realPointF.x = -realPointF.x;
        realPointF.y = -realPointF.y;
    }

    @Implementation
    public final void offset(float dx, float dy) {
        realPointF.x += dx;
        realPointF.y += dy;
    }

    @Override @Implementation
    public boolean equals(Object object) {
        if (object == null) return false;
        if (this == object) return true;
        if (object.getClass() != PointF.class) return false;

        PointF that = (PointF) object;
        if (this.realPointF.x == that.x && this.realPointF.y == that.y) return true;

        return false;
    }

    @Override @Implementation
    public int hashCode() {
        return (int) (realPointF.x * 32713 + realPointF.y);
    }

    @Override @Implementation
    public String toString() {
        return "Point(" + realPointF.x + ", " + realPointF.y + ")";
    }

    /**
     * Non-Android utility method for comparing a point to a well-known value
     *
     * @param x x
     * @param y y
     * @return this.x == x && this.y == y
     */
    @Implementation
    public final boolean equals(float x, float y) {
        return realPointF.x == x && realPointF.y == y;
    }
}