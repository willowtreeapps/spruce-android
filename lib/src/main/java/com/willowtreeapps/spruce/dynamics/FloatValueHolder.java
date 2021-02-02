package com.willowtreeapps.spruce.dynamics;

/**
 * <p>FloatValueHolder holds a float value. FloatValueHolder provides a setter and a getter (
 * i.e. {@link #setValue(float)} and {@link #getValue()}) to access this float value. Animations can
 * be performed on a FloatValueHolder instance. During each frame of the animation, the
 * FloatValueHolder will have its value updated via {@link #setValue(float)}. The caller can
 * obtain the up-to-date animation value via {@link FloatValueHolder#getValue()}.
 *
 * <p> Here is an example for creating a {@link FlingAnimation} with a FloatValueHolder:
 * <pre class="prettyprint">
 * // Create a fling animation with an initial velocity of 5000 (pixel/s) and an initial position
 * // of 20f.
 * FloatValueHolder floatValueHolder = new FloatValueHolder(20f);
 * FlingAnimation anim = new FlingAnimation(floatValueHolder).setStartVelocity(5000);
 * anim.start();
 * </pre>
 *
 * @see SpringAnimation#SpringAnimation(FloatValueHolder)
 * @see FlingAnimation#FlingAnimation(FloatValueHolder)
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
