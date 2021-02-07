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

import androidx.annotation.FloatRange;

/**
 * <p>Fling animation is an animation that continues an initial momentum (most often from gesture
 * velocity) and gradually slows down. The fling animation will come to a stop when the velocity of
 * the animation is below the threshold derived from {@link #setMinimumVisibleChange(float)},
 * or when the value of the animation has gone beyond the min or max value defined via
 * {@link SpruceDynamics#setMinValue(float)} or {@link SpruceDynamics#setMaxValue(float)}.
 * It is recommended to restrict the fling animation with min and/or max value, such that the
 * animation can end when it goes beyond screen bounds, thus preserving CPU cycles and resources.
 *
 * <p>For example, you can create a fling animation that animates the translationX of a view:
 * <pre class="prettyprint">
 * FlingAnimation flingAnim = new FlingAnimation(view, DynamicAnimation.TRANSLATION_X)
 *         // Sets the start velocity to -2000 (pixel/s)
 *         .setStartVelocity(-2000)
 *         // Optional but recommended to set a reasonable min and max range for the animation.
 *         // In this particular case, we set the min and max to -200 and 2000 respectively.
 *         .setMinValue(-200).setMaxValue(2000);
 * flingAnim.start();
 * </pre>
 */
public final class SpruceFlingAnimation extends SpruceDynamics<SpruceFlingAnimation> {

    private final DragForce mFlingForce = new DragForce();

    /**
     * <p>This creates a FlingAnimation that animates a {@link FloatValueHolder} instance. During
     * the animation, the {@link FloatValueHolder} instance will be updated via
     * {@link FloatValueHolder#setValue(float)} each frame. The caller can obtain the up-to-date
     * animation value via {@link FloatValueHolder#getValue()}.
     *
     * <p><strong>Note:</strong> changing the value in the {@link FloatValueHolder} via
     * {@link FloatValueHolder#setValue(float)} outside of the animation during an
     * animation run will not have any effect on the on-going animation.
     *
     * @param floatValueHolder the property to be animated
     */
    public SpruceFlingAnimation(FloatValueHolder floatValueHolder) {
        super(floatValueHolder);
        mFlingForce.setValueThreshold(getValueThreshold());
    }

    /**
     * This creates a FlingAnimation that animates the property of the given object.
     *
     * @param object the Object whose property will be animated
     * @param property the property to be animated
     * @param <K> the class on which the property is declared
     */
    public <K> SpruceFlingAnimation(K object, FloatPropertyCompat<K> property) {
        super(object, property);
        mFlingForce.setValueThreshold(getValueThreshold());
    }

    /**
     * Sets the friction for the fling animation. The greater the friction is, the sooner the
     * animation will slow down. When not set, the friction defaults to 1.
     *
     * @param friction the friction used in the animation
     * @return the animation whose friction will be scaled
     * @throws IllegalArgumentException if the input friction is not positive
     */
    public SpruceFlingAnimation setFriction(
            @FloatRange(from = 0.0, fromInclusive = false) float friction) {
        if (friction <= 0) {
            throw new IllegalArgumentException("Friction must be positive");
        }
        mFlingForce.setFrictionScalar(friction);
        return this;
    }

    /**
     * Returns the friction being set on the animation via {@link #setFriction(float)}. If the
     * friction has not been set, the default friction of 1 will be returned.
     *
     * @return friction being used in the animation
     */
    public float getFriction() {
        return mFlingForce.getFrictionScalar();
    }

    /**
     * Sets the min value of the animation. When a fling animation reaches the min value, the
     * animation will end immediately. Animations will not animate beyond the min value.
     *
     * @param minValue minimum value of the property to be animated
     * @return the Animation whose min value is being set
     */
    @Override
    public SpruceFlingAnimation setMinValue(float minValue) {
        super.setMinValue(minValue);
        return this;
    }

    /**
     * Sets the max value of the animation. When a fling animation reaches the max value, the
     * animation will end immediately. Animations will not animate beyond the max value.
     *
     * @param maxValue maximum value of the property to be animated
     * @return the Animation whose max value is being set
     */
    @Override
    public SpruceFlingAnimation setMaxValue(float maxValue) {
        super.setMaxValue(maxValue);
        return this;
    }

    /**
     * Start velocity of the animation. Default velocity is 0. Unit: pixel/second
     *
     * <p>A <b>non-zero</b> start velocity is required for a FlingAnimation. If no start velocity is
     * set through {@link #setStartVelocity(float)}, the start velocity defaults to 0. In that
     * case, the fling animation will consider itself done in the next frame.
     *
     * <p>Note when using a fixed value as the start velocity (as opposed to getting the velocity
     * through touch events), it is recommended to define such a value in dp/second and convert it
     * to pixel/second based on the density of the screen to achieve a consistent look across
     * different screens.
     *
     * <p>To convert from dp/second to pixel/second:
     * <pre class="prettyprint">
     * float pixelPerSecond = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpPerSecond,
     *         getResources().getDisplayMetrics());
     * </pre>
     *
     * @param startVelocity start velocity of the animation in pixel/second
     * @return the Animation whose start velocity is being set
     */
    @Override
    public SpruceFlingAnimation setStartVelocity(float startVelocity) {
        super.setStartVelocity(startVelocity);
        return this;
    }

    @Override
    boolean updateValueAndVelocity(long deltaT) {

        MassState state = mFlingForce.updateValueAndVelocity(mValue, mVelocity, deltaT);
        mValue = state.mValue;
        mVelocity = state.mVelocity;

        // When the animation hits the max/min value, consider animation done.
        if (mValue < mMinValue) {
            mValue = mMinValue;
            return true;
        }
        if (mValue > mMaxValue) {
            mValue = mMaxValue;
            return true;
        }

        if (isAtEquilibrium(mValue, mVelocity)) {
            return true;
        }
        return false;
    }

    @Override
    float getAcceleration(float value, float velocity) {
        return mFlingForce.getAcceleration(value, velocity);
    }

    @Override
    boolean isAtEquilibrium(float value, float velocity) {
        return value >= mMaxValue
                || value <= mMinValue
                || mFlingForce.isAtEquilibrium(value, velocity);
    }

    @Override
    void setValueThreshold(float threshold) {
        mFlingForce.setValueThreshold(threshold);
    }

    static final class DragForce implements Force {

        private static final float DEFAULT_FRICTION = -4.2f;

        // This multiplier is used to calculate the velocity threshold given a certain value
        // threshold. The idea is that if it takes >= 1 frame to move the value threshold amount,
        // then the velocity is a reasonable threshold.
        private static final float VELOCITY_THRESHOLD_MULTIPLIER = 1000f / 16f;
        private float mFriction = DEFAULT_FRICTION;
        private float mVelocityThreshold;

        // Internal state to hold a value/velocity pair.
        private final SpruceDynamics.MassState mMassState = new SpruceDynamics.MassState();

        void setFrictionScalar(float frictionScalar) {
            mFriction = frictionScalar * DEFAULT_FRICTION;
        }

        float getFrictionScalar() {
            return mFriction / DEFAULT_FRICTION;
        }

        MassState updateValueAndVelocity(float value, float velocity, long deltaT) {
            mMassState.mVelocity = (float) (velocity * Math.exp((deltaT / 1000f) * mFriction));
            mMassState.mValue = (float) (value + (mMassState.mVelocity - velocity) / mFriction);
            if (isAtEquilibrium(mMassState.mValue, mMassState.mVelocity)) {
                mMassState.mVelocity = 0f;
            }
            return mMassState;
        }

        @Override
        public float getAcceleration(float position, float velocity) {
            return velocity * mFriction;
        }

        @Override
        public boolean isAtEquilibrium(float value, float velocity) {
            return Math.abs(velocity) < mVelocityThreshold;
        }

        void setValueThreshold(float threshold) {
            mVelocityThreshold = threshold * VELOCITY_THRESHOLD_MULTIPLIER;
        }
    }

}
