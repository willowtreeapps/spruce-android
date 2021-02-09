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

/**
 * <p>FloatValueHolder holds a float value. FloatValueHolder provides a setter and a getter (
 * i.e. {@link #setValue(float)} and {@link #getValue()}) to access this float value. Animations can
 * be performed on a FloatValueHolder instance. During each frame of the animation, the
 * FloatValueHolder will have its value updated via {@link #setValue(float)}. The caller can
 * obtain the up-to-date animation value via {@link FloatValueHolder#getValue()}.
 *
 * <p> Here is an example for creating a {@link SpruceFlingAnimation} with a FloatValueHolder:
 * <pre class="prettyprint">
 * // Create a fling animation with an initial velocity of 5000 (pixel/s) and an initial position
 * // of 20f.
 * FloatValueHolder floatValueHolder = new FloatValueHolder(20f);
 * FlingAnimation anim = new FlingAnimation(floatValueHolder).setStartVelocity(5000);
 * anim.start();
 * </pre>
 *
 * @see SpruceSpringAnimation#SpruceSpringAnimation(FloatValueHolder)
 * @see SpruceFlingAnimation#SpruceFlingAnimation(FloatValueHolder)
 */

public class FloatValueHolder {
    private float mValue = 0.0f;

    /**
     * Constructs a holder for a float value that is initialized to 0.
     */
    public FloatValueHolder() {
    }

    /**
     * Constructs a holder for a float value that is initialized to the input value.
     *
     * @param value the value to initialize the value held in the FloatValueHolder
     */
    public FloatValueHolder(float value) {
        setValue(value);
    }

    /**
     * Sets the value held in the FloatValueHolder instance.
     *
     * @param value float value held in the FloatValueHolder instance
     */
    public void setValue(float value) {
        mValue = value;
    }

    /**
     * Returns the float value held in the FloatValueHolder instance.
     *
     * @return float value held in the FloatValueHolder instance
     */
    public float getValue() {
        return mValue;
    }
}
