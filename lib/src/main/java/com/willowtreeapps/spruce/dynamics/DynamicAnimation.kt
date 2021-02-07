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

package com.willowtreeapps.spruce.dynamics

/**
 * Creates [SpruceFlingAnimation] for a property that can be accessed via the provided setter and getter.
 * For example, the following sample code creates a [SpruceFlingAnimation] for the alpha property of a
 * [View] object named `view`:
 * `flingAnimationOf(view::setAlpha, view::getAlpha)`
 *
 * @param setter The function that mutates the property being animated
 * @param getter The function that returns the value of the property
 * @return [SpruceFlingAnimation]
 */
fun flingAnimationOf(setter: (Float) -> Unit, getter: () -> Float): SpruceFlingAnimation {
    return SpruceFlingAnimation(createFloatValueHolder(setter, getter))
}

/**
 * Creates [SpruceSpringAnimation] for a property that can be accessed via the provided setter and getter.
 * If finalPosition is not [Float.NaN] then create [SpruceSpringAnimation] with
 * [SpringForce.mFinalPosition].
 *
 * @param setter The function that mutates the property being animated
 * @param getter The function that returns the value of the property
 * @param finalPosition [SpringForce.mFinalPosition] Final position of spring.
 * @return [SpruceSpringAnimation]
 */
fun springAnimationOf(
        setter: (Float) -> Unit,
        getter: () -> Float,
        finalPosition: Float = Float.NaN
): SpruceSpringAnimation {
    val valueHolder = createFloatValueHolder(setter, getter)
    return if (finalPosition.isNaN()) {
        SpruceSpringAnimation(valueHolder)
    } else {
        SpruceSpringAnimation(valueHolder, finalPosition)
    }
}

/**
 * Updates or applies spring force properties like [SpringForce.mDampingRatio],
 * [SpringForce.mFinalPosition] and stiffness on SpringAnimation.
 *
 * If [SpruceSpringAnimation.mSpring] is null in case [SpruceSpringAnimation] is created without final position
 * it will be created and attached to [SpruceSpringAnimation]
 *
 * @param func lambda with receiver on [SpringForce]
 * @return [SpruceSpringAnimation]
 */
inline fun SpruceSpringAnimation.withSpringForceProperties(
        func: SpringForce.() -> Unit
): SpruceSpringAnimation {
    if (spring == null) {
        spring = SpringForce()
    }
    spring.func()
    return this
}

private fun createFloatValueHolder(setter: (Float) -> Unit, getter: () -> Float): FloatValueHolder {
    return object : FloatValueHolder() {
        override fun getValue(): Float {
            return getter.invoke()
        }

        override fun setValue(value: Float) {
            setter.invoke(value)
        }
    }
}