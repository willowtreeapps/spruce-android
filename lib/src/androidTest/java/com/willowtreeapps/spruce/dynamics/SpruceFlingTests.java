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


import android.view.View;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.willowtreeapps.spruce.AnimationActivity;
import com.willowtreeapps.spruce.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("unchecked")
@LargeTest
@RunWith(AndroidJUnit4.class)
public class SpruceFlingTests {
    @SuppressWarnings("deprecation")
    @Rule
    public final ActivityTestRule<AnimationActivity> mActivityTestRule;
    public View mView1;
    public View mView2;

    @Rule
    public ExpectedException mExpectedException = ExpectedException.none();

    @SuppressWarnings("deprecation")
    public SpruceFlingTests() {
        mActivityTestRule = new ActivityTestRule<>(AnimationActivity.class);
    }

    @Before
    public void setup() throws Exception {
        mView1 = mActivityTestRule.getActivity().findViewById(R.id.anim_view);
        mView2 = mActivityTestRule.getActivity().findViewById(R.id.anim_another_view);
    }

    /**
     * Test that custom properties are supported.
     */
    @Test
    public void testCustomProperties() {
        final Object animObj = new Object();
        FloatPropertyCompat property = new FloatPropertyCompat("") {
            private float mValue = 0f;

            @Override
            public float getValue(Object object) {
                assertEquals(animObj, object);
                return mValue;
            }

            @Override
            public void setValue(Object object, float value) {
                assertEquals(animObj, object);
                assertTrue(value > mValue);
                assertTrue(value >= 100);
                mValue = value;
            }
        };
        final SpruceFlingAnimation anim = new SpruceFlingAnimation(animObj, property);
        SpruceDynamics.OnAnimationEndListener listener = (animation, canceled, value, velocity) -> {
            assertEquals(animation.hashCode(), anim.hashCode());
            assertFalse(canceled);
            assertTrue(value > 110f);
            assertEquals(velocity, 0f,0f);
        };
        anim.addEndListener(listener);
        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                anim.setStartValue(100).setStartVelocity(2000).start();
            }
        });
    }

    /**
     * Test that fling animation can work with a single property without an object.
     */
    @Test
    public void testFloatValueHolder() {
        FloatValueHolder floatValueHolder = new FloatValueHolder();
        assertEquals(0.0f, floatValueHolder.getValue(), 0.01f);

        final SpruceFlingAnimation anim = new SpruceFlingAnimation(floatValueHolder).setStartVelocity(-2500);

        SpruceDynamics.OnAnimationEndListener listener = (animation, canceled, value, velocity) -> {
            assertEquals(anim.hashCode(),animation.hashCode());
            assertFalse(canceled);
            assertTrue(value < -50f);
            assertEquals(0, velocity, 0.0);
        };
        anim.addEndListener(listener);
        InstrumentationRegistry.getInstrumentation().runOnMainSync(anim::start);
    }


    /**
     * Test that friction does affect how fast the slow down happens. Fling animation with
     * higher friction should finish first.
     */
    @Test
    public void testFriction() {
        FloatValueHolder floatValueHolder = new FloatValueHolder();
        float lowFriction = 0.5f;
        float highFriction = 2f;
        final SpruceFlingAnimation animLowFriction = new SpruceFlingAnimation(floatValueHolder);
        final SpruceFlingAnimation animHighFriction = new SpruceFlingAnimation(floatValueHolder);

        animHighFriction.setFriction(highFriction);
        animLowFriction.setFriction(lowFriction);

        SpruceDynamics.OnAnimationEndListener listener = (animation, canceled, value, velocity) -> {
            assertEquals(animation.hashCode(), animHighFriction.hashCode());
            assertFalse(canceled);
            assertTrue(value > 200f);
            assertEquals(0f, velocity, 0.0);
        };

        animHighFriction.addEndListener(listener);
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            animHighFriction.setStartVelocity(5000).setStartValue(0).start();
            animLowFriction.setStartVelocity(5000).setStartValue(0).start();
        });

        // By the time high scalar animation finishes, the lower friction animation should still be
        // running.
        assertTrue(animLowFriction.isRunning());
        InstrumentationRegistry.getInstrumentation().runOnMainSync(animLowFriction::cancel);

        assertEquals(lowFriction, animLowFriction.getFriction(), 0f);
        assertEquals(highFriction, animHighFriction.getFriction(), 0f);

    }

    /**
     * Test that velocity threshold does affect how early fling animation ends. An animation with
     * higher velocity threshold should finish first.
     */
    @Test
    public void testVelocityThreshold() {
        FloatValueHolder floatValueHolder = new FloatValueHolder();
        float lowThreshold = 5f;
        final float highThreshold = 10f;
        final SpruceFlingAnimation animLowThreshold = new SpruceFlingAnimation(floatValueHolder);
        final SpruceFlingAnimation animHighThreshold = new SpruceFlingAnimation(floatValueHolder);

        animHighThreshold.setMinimumVisibleChange(highThreshold);
        animHighThreshold.addUpdateListener((animation, value, velocity) -> {
            if (velocity != 0f) {
                // Other than last frame, velocity should always be above threshold
                assertTrue(velocity >= highThreshold);
            }
        });
        animLowThreshold.setMinimumVisibleChange(lowThreshold);

        SpruceDynamics.OnAnimationEndListener listener = (animation, canceled, value, velocity) -> {
            assertEquals(animHighThreshold.hashCode(), animation.hashCode());
            assertFalse(canceled);
            assertTrue(value > 200f);
            assertEquals(0f, velocity, 0.0);
        };
        animHighThreshold.addEndListener(listener);
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            animHighThreshold.setStartVelocity(2000).setStartValue(0).start();
            animLowThreshold.setStartVelocity(2000).setStartValue(0).start();
        });

        // By the time high scalar animation finishes, the lower friction animation should still be
        // running.
        assertTrue(animLowThreshold.isRunning());
        InstrumentationRegistry.getInstrumentation().runOnMainSync(animLowThreshold::cancel);

        assertEquals(lowThreshold, animLowThreshold.getMinimumVisibleChange(), 0f);
        assertEquals(highThreshold, animHighThreshold.getMinimumVisibleChange(), 0f);

    }
}
