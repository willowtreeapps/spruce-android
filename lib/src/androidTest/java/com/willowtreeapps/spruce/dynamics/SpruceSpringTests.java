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

import android.os.HandlerThread;
import android.os.Looper;
import android.os.SystemClock;
import android.util.AndroidRuntimeException;
import android.view.View;

import androidx.core.view.ViewCompat;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.filters.MediumTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.willowtreeapps.spruce.AnimationActivity;
import com.willowtreeapps.spruce.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


@SuppressWarnings("unchecked")
@MediumTest
@RunWith(AndroidJUnit4.class)
public class SpruceSpringTests {
    @SuppressWarnings("deprecation")
    @Rule
    public final ActivityTestRule<AnimationActivity> mActivityTestRule;
    public View mView1;
    public View mView2;
    @Rule
    public ExpectedException mExpectedException = ExpectedException.none();
    int updateCount = 0;

    @SuppressWarnings("deprecation")
    public SpruceSpringTests() {
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
                assertTrue(value >= mValue);
                mValue = value;
            }
        };
        final SpruceSpringAnimation anim = new SpruceSpringAnimation(animObj, property, 1f);
        anim.addEndListener((animation, canceled, value, velocity) -> assertEquals(1f, property.getValue(animObj), 0f));
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> anim.start());

    }

    /**
     * Test that spring animation can work with a single property without an object.
     */
    @Test
    public void testFloatValueHolder() {
        final FloatValueHolder floatValueHolder = new FloatValueHolder(0f);
        SpruceDynamics.OnAnimationUpdateListener updateListener =
                new SpruceDynamics.OnAnimationUpdateListener() {
                    private float mLastValue = 0f;

                    @Override
                    public void onAnimationUpdate(SpruceDynamics animation, float value, float velocity) {
                        // New value >= value from last frame
                        assertTrue(value >= mLastValue);
                        mLastValue = value;
                        assertEquals(value, floatValueHolder.getValue(), 0f);
                    }
                };


        final SpruceSpringAnimation anim = new SpruceSpringAnimation(floatValueHolder);

        anim.setSpring(new SpringForce(1000).setDampingRatio(1.2f));

        anim.addUpdateListener(updateListener);
        anim.addEndListener((animation, canceled, value, velocity) -> {
            assertEquals(anim.hashCode(), animation.hashCode());
            assertTrue(1000f == value);
            assertTrue(0f == velocity);
        });

        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> anim.setStartValue(0).start());
    }

    /**
     * Cancel a spring animation right after an animateToFinalPosition() is called.
     */
    @Test
    public void testCancelAfterAnimateToFinalPosition() {
        final SpruceSpringAnimation anim = new SpruceSpringAnimation(mView1, SpruceDynamics.TRANSLATION_X,
                0);

        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            SpruceDynamics.OnAnimationEndListener listener1 = (animation, canceled, value, velocity) -> {
                assertEquals(anim.hashCode(), animation.hashCode());
                assertEquals(canceled, true);
                assertTrue(0f == value);
                assertTrue(0f == velocity);
            };

            SpruceDynamics.OnAnimationEndListener listener2 = (animation, canceled, value, velocity) -> {
                assertEquals(anim.hashCode(), animation.hashCode());
                assertTrue(canceled == false);
                assertTrue(-200f == value);
                assertTrue(0f == velocity);
            };

            anim.addEndListener(listener1);
            anim.start();
            assertTrue(anim.isRunning());
            anim.animateToFinalPosition(200f);
            anim.cancel();
            anim.removeEndListener(listener1);
            anim.addEndListener(listener2);
            anim.animateToFinalPosition(-200f);
            anim.skipToEnd();
        });
    }


    /**
     * Check the final position of the default spring against what's being set through the
     * constructor.
     */
    @Test
    public void testGetFinalPosition() {
        SpruceSpringAnimation animation = new SpruceSpringAnimation(mView1, SpruceDynamics.TRANSLATION_X, 20);
        assertEquals(20, animation.getSpring().getFinalPosition(), 0);

        SpringForce spring = new SpringForce();
        spring.setFinalPosition(25.0f);
        assertEquals(25.0f, spring.getFinalPosition(), 0.0f);
    }

    /**
     * Verify that for over-damped springs, the higher the damping ratio, the slower it is. Also
     * verify that critically damped springs finish faster than overdamped springs.
     */
    @Test
    public void testDampingRatioOverAndCriticallyDamped() {
        // Compare overdamped springs
        final SpruceSpringAnimation anim1 = new SpruceSpringAnimation(mView1, SpruceDynamics.X, 0);
        final SpruceSpringAnimation anim2 = new SpruceSpringAnimation(mView2, SpruceDynamics.Y, 0);
        final SpruceSpringAnimation anim3 = new SpruceSpringAnimation(mView2, SpruceDynamics.Z, 0);
        final SpruceDynamics.OnAnimationUpdateListener updateListener =
                new SpruceDynamics.OnAnimationUpdateListener() {
                    public float position1 = 1000;
                    public float position2 = 1000;
                    public float position3 = 1000;

                    @Override
                    public void onAnimationUpdate(SpruceDynamics animation, float value,
                                                  float velocity) {
                        if (animation == anim1) {
                            position1 = value;
                            if (position1 == 800) {
                                // first frame
                                assertEquals(position1, position2, 0);
                                assertEquals(position1, position3, 0);
                            } else {
                                assertTrue(position2 > position1);
                                assertTrue(position3 > position2);
                                assertTrue(800 > position3);
                            }
                        } else if (animation == anim2) {
                            position2 = value;
                        } else {
                            position3 = value;
                        }
                    }
                };
        final MyEndListener l1 = new MyEndListener();
        final MyEndListener l2 = new MyEndListener();
        final MyEndListener l3 = new MyEndListener();

        anim1.getSpring().setStiffness(SpringForce.STIFFNESS_HIGH).setDampingRatio(1f);
        anim2.getSpring().setStiffness(SpringForce.STIFFNESS_HIGH).setDampingRatio(1.5f);
        anim3.getSpring().setStiffness(SpringForce.STIFFNESS_HIGH).setDampingRatio(2.0f);

        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                anim2.setStartValue(800).addUpdateListener(updateListener).addEndListener(l2)
                        .start();
                anim3.setStartValue(800).addUpdateListener(updateListener).addEndListener(l3)
                        .addEndListener((animation, canceled, value, velocity) -> {
                            assertEquals(anim3.hashCode(), animation.hashCode());
                            assertTrue(!canceled);
                            assertTrue(value == 0f);
                            assertTrue(velocity == 0f);
                        }).start();
                anim1.setStartValue(800).addUpdateListener(updateListener).addEndListener(l1)
                        .start();

            }
        });
        /* waiting untill the animation ends. */
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // The spring animation with critically-damped spring should return to rest position faster.
        assertTrue(l1.endTime > 0);
        assertTrue(l2.endTime > l1.endTime);
        assertTrue(l3.endTime > l2.endTime);
    }

    /**
     * Verify that more underdamped springs are bouncier, and that critically damped springs finish
     * faster than underdamped springs.
     */
    @Test
    public void testDampingRatioUnderDamped() {
        final SpruceSpringAnimation anim1 = new SpruceSpringAnimation(mView1, SpruceDynamics.ROTATION, 0);
        final SpruceSpringAnimation anim2 = new SpruceSpringAnimation(mView2, SpruceDynamics.ROTATION_X, 0);
        final SpruceSpringAnimation anim3 = new SpruceSpringAnimation(mView2, SpruceDynamics.ROTATION_Y, 0);

        final SpruceDynamics.OnAnimationUpdateListener updateListener =
                new SpruceDynamics.OnAnimationUpdateListener() {
                    public float bounceCount1 = 0;
                    public float bounceCount2 = 0;

                    public float velocity1 = 0;
                    public float velocity2 = 0;

                    @Override
                    public void onAnimationUpdate(SpruceDynamics animation, float value,
                                                  float velocity) {
                        if (animation == anim1) {
                            if (velocity > 0 && velocity1 < 0) {
                                bounceCount1++;
                            }
                            velocity1 = velocity;
                        } else if (animation == anim2) {
                            velocity2 = velocity;
                            if (velocity > 0 && velocity2 < 0) {
                                bounceCount2++;
                                assertTrue(bounceCount1 > bounceCount2);
                            }
                        }
                    }
                };
        final MyEndListener l1 = new MyEndListener();
        final MyEndListener l2 = new MyEndListener();
        final MyEndListener l3 = new MyEndListener();


        anim1.getSpring().setStiffness(SpringForce.STIFFNESS_MEDIUM).setDampingRatio(0.3f);
        anim2.getSpring().setStiffness(SpringForce.STIFFNESS_MEDIUM).setDampingRatio(0.5f);
        anim3.getSpring().setStiffness(SpringForce.STIFFNESS_MEDIUM).setDampingRatio(1f);

        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            anim1.setStartValue(360).addUpdateListener(updateListener).addEndListener(l1)
                    .start();
            anim2.setStartValue(360).addUpdateListener(updateListener).addEndListener(l2)
                    .addEndListener((animation, canceled, value, velocity) -> {
                        assertTrue(anim2.hashCode() == animation.hashCode());
                        assertTrue(!canceled);
                        assertTrue(value == 0f);
                        assertTrue(velocity == 0f);
                    }).start();
            anim3.setStartValue(360).addEndListener(l3).start();
        });

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // The spring animation with critically-damped spring should return to rest position faster.
        assertFalse(anim3.isRunning());
        assertTrue(l3.endTime > 0);
        assertTrue(l2.endTime > l3.endTime);

        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            if (anim1.isRunning()) {
                anim1.cancel();
            } else {
                assertTrue(l1.endTime > l2.endTime);
            }
        });
    }

    /**
     * Verify that stiffer spring animations finish sooner than less stiff spring animations. Run
     * the same verification on different damping ratios.
     */
    @LargeTest
    @Test
    public void testStiffness() {
        float[] dampingRatios = {0.3f, 0.5f, 1f, 5f};
        final float[] stiffness = {50f, 500f, 1500f, 5000f};
        SpruceDynamics.ViewProperty[] viewProperties =
                {SpruceDynamics.SCROLL_X, SpruceDynamics.TRANSLATION_X,
                        SpruceDynamics.TRANSLATION_Y, SpruceDynamics.TRANSLATION_Z};
        assertEquals(viewProperties.length, stiffness.length);

        final SpruceSpringAnimation[] springAnims = new SpruceSpringAnimation[stiffness.length];
        SpringForce[] springs = new SpringForce[stiffness.length];
        MyEndListener[] listeners = new MyEndListener[stiffness.length];

        // Sets stiffness
        for (int i = 0; i < stiffness.length; i++) {
            springs[i] = new SpringForce(0).setStiffness(stiffness[i]);
            listeners[i] = new MyEndListener();
            springAnims[i] = new SpruceSpringAnimation(mView1, viewProperties[i]).setSpring(springs[i])
                    .addEndListener(listeners[i]);
        }

        for (int i = 0; i < dampingRatios.length; i++) {
            for (int j = 0; j < stiffness.length; j++) {
                springs[j].setDampingRatio(dampingRatios[i]);
                springAnims[j].setStartValue(0).setStartVelocity(500);
                listeners[j].endTime = -1;
            }

            springAnims[1].addEndListener((animation, canceled, value, velocity) -> {
                assertTrue(animation.hashCode() == springAnims[1].hashCode());
                assertTrue(!canceled);
                assertTrue(0f == value);
                assertTrue(0f == velocity);
            });
            InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < stiffness.length; j++) {
                        springAnims[j].start();
                    }
                }
            });

            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (springAnims[0].isRunning()) {
                InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> springAnims[0].cancel());
            }
            for (int j = 1; j < stiffness.length; j++) {
                // The stiffer spring should finish no later than the less stiff spring.
                assertTrue(listeners[j - 1].endTime > listeners[j].endTime);
            }
        }
    }

    /**
     * Test negative stiffness and expect exception.
     */
    @Test
    public void testInvalidStiffness() {
        SpringForce spring = new SpringForce();
        mExpectedException.expect(IllegalArgumentException.class);
        spring.setStiffness(-5f);
    }

    /**
     * Test negative dampingRatio and expect exception.
     */
    @Test
    public void testInvalidDampingRatio() {
        SpringForce spring = new SpringForce();
        mExpectedException.expect(IllegalArgumentException.class);
        spring.setDampingRatio(-5f);
    }


    /**
     * Verifies stiffness getter returns the right value.
     */
    @Test
    public void testGetStiffness() {
        SpringForce spring = new SpringForce();
        spring.setStiffness(1.0f);
        assertEquals(1.0f, spring.getStiffness(), 0.0f);
        spring.setStiffness(2.0f);
        assertEquals(2.0f, spring.getStiffness(), 0.0f);
    }

    /**
     * Verifies damping ratio getter returns the right value.
     */
    @Test
    public void testGetDampingRatio() {
        SpringForce spring = new SpringForce();
        spring.setDampingRatio(1.0f);
        assertEquals(1.0f, spring.getDampingRatio(), 0.0f);
        spring.setDampingRatio(2.0f);
        assertEquals(2.0f, spring.getDampingRatio(), 0.0f);
    }

    /**
     * Verifies that once min and max value threshold does apply to the values in animation.
     */
    @Test
    public void testSetMinMax() {
        updateCount = 0;
        final SpruceSpringAnimation anim = new SpruceSpringAnimation(mView1, SpruceDynamics.SCALE_X, 0.0f);
        anim.setMinValue(0.0f);
        anim.setMaxValue(1.0f);
        anim.getSpring().setStiffness(SpringForce.STIFFNESS_HIGH).setDampingRatio(
                SpringForce.DAMPING_RATIO_HIGH_BOUNCY);

        final SpruceDynamics.OnAnimationUpdateListener mockUpdateListener = (animation, value, velocity) -> updateCount += 1;
        final SpruceDynamics.OnAnimationEndListener mockEndListener = (animation, canceled, value, velocity) -> {
            assertTrue(anim.hashCode() == animation.hashCode());
            assertTrue(!canceled);
            assertTrue(value == 0f);
            assertTrue(velocity == 0f);
        };
        final SpruceDynamics.OnAnimationUpdateListener updateListener =
                (animation, value, velocity) -> {
                    assertTrue(value >= 0.0f);
                    assertTrue(value <= 1.0f);
                };

        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> anim.setStartValue(1.0f).setStartVelocity(8000f)
                .addEndListener(mockEndListener).addUpdateListener(mockUpdateListener)
                .addUpdateListener(updateListener).start());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertTrue(updateCount >= 2);
    }

    /**
     * Verifies animateToFinalPosition works both when the anim hasn't started and when it's
     * running.
     */
    @Test
    public void testAnimateToFinalPosition() throws InterruptedException {
        final SpruceSpringAnimation anim = new SpruceSpringAnimation(mView1, SpruceDynamics.SCALE_Y, 0.0f);
        final SpruceDynamics.OnAnimationEndListener mockEndListener = (animation, canceled, value, velocity) -> {
            assertTrue(anim.hashCode() == animation.hashCode());
            assertTrue(!canceled);
            assertTrue(1.0f == value);
            assertTrue(0.0f == velocity);
        };
        anim.addEndListener(mockEndListener);
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> anim.animateToFinalPosition(0.0f));
        assertTrue(anim.isRunning());
        Thread.sleep(100);
        assertTrue(anim.isRunning());
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> anim.animateToFinalPosition(1.0f));

        assertTrue(anim.isRunning());

    }

    /**
     * Check that the min visible change does affect how soon spring animations end.
     */
    public void testScaleMinChange() {
        FloatValueHolder valueHolder = new FloatValueHolder(0.5f);
        final SpruceSpringAnimation anim = new SpruceSpringAnimation(valueHolder);
        SpruceDynamics.OnAnimationUpdateListener mockListener =
                (animation, value, velocity) -> assertTrue(anim.hashCode() == animation.hashCode());
        anim.addUpdateListener(mockListener);

        final SpruceDynamics.OnAnimationEndListener endListener =
                (animation, canceled, value, velocity) -> {
                    assertTrue(anim.hashCode() == animation.hashCode());
                    assertTrue(!canceled);
                    assertTrue(value == 0f);
                    assertTrue(velocity == 0f);
                };
        anim.addEndListener(endListener);

        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> anim.animateToFinalPosition(1f));

        assertEquals(SpruceDynamics.MIN_VISIBLE_CHANGE_PIXELS, anim.getMinimumVisibleChange(),
                0.01f);

        // Set the right threshold and start again.
        anim.setMinimumVisibleChange(SpruceDynamics.MIN_VISIBLE_CHANGE_SCALE);
        anim.setStartValue(0.5f);

        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> anim.animateToFinalPosition(1f));
    }

    /**
     * Makes sure all the properties getter works.
     */
    @Test
    public void testAllProperties() {
        final SpruceDynamics.ViewProperty[] properties = {
                SpruceDynamics.ALPHA, SpruceDynamics.TRANSLATION_X,
                SpruceDynamics.TRANSLATION_Y, SpruceDynamics.TRANSLATION_Z,
                SpruceDynamics.SCALE_X, SpruceDynamics.SCALE_Y, SpruceDynamics.ROTATION,
                SpruceDynamics.ROTATION_X, SpruceDynamics.ROTATION_Y,
                SpruceDynamics.X, SpruceDynamics.Y, SpruceDynamics.Z,
                SpruceDynamics.SCROLL_X, SpruceDynamics.SCROLL_Y,
        };

        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            mView1.setAlpha(0f);
            mView1.setTranslationX(0f);
            mView1.setTranslationY(0f);
            ViewCompat.setTranslationZ(mView1, 0f);

            mView1.setScaleX(0f);
            mView1.setScaleY(0f);

            mView1.setRotation(0f);
            mView1.setRotationX(0f);
            mView1.setRotationY(0f);

            mView1.setX(0f);
            mView1.setY(0f);
            ViewCompat.setZ(mView1, 0f);

            mView1.setScrollX(0);
            mView1.setScrollY(0);
        });

        final SpruceSpringAnimation[] anims = new SpruceSpringAnimation[properties.length];
        final SpruceDynamics.OnAnimationUpdateListener[] mockListeners =
                new SpruceDynamics.OnAnimationUpdateListener[properties.length];
        for (int i = 0; i < properties.length; i++) {
            anims[i] = new SpruceSpringAnimation(mView1, properties[i], 1);
            final int finalI = i;
            anims[i].addUpdateListener(
                    new SpruceDynamics.OnAnimationUpdateListener() {
                        boolean mIsFirstFrame = true;

                        @Override
                        public void onAnimationUpdate(SpruceDynamics animation, float value,
                                                      float velocity) {
                            if (mIsFirstFrame) {
                                assertEquals(value, 0f, 0f);
                            }
                            mIsFirstFrame = false;
                        }
                    });
            mockListeners[i] = (animation, value, velocity) -> assertTrue(true);
            anims[i].addUpdateListener(mockListeners[i]);
        }

        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            for (int i = properties.length - 1; i >= 0; i--) {
                anims[i].start();
            }
        });

        for (int i = 0; i < properties.length; i++) {
            int timeout = i == 0 ? 100 : 0;
            try {
                Thread.sleep(timeout);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            for (int i = 0; i < properties.length; i++) {
                anims[i].cancel();
            }
        });
    }

    /**
     * Test start() on a test thread.
     */
    @Test
    public void testStartOnNonAnimationHandlerThread() throws InterruptedException {
        mExpectedException.expect(AndroidRuntimeException.class);
        SpruceSpringAnimation anim = new SpruceSpringAnimation(mView1, SpruceDynamics.ALPHA, 0f);
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            anim.setAnimationHandler(anim.getAnimationHandler());
        });
        runRunnableOnNewThread(() -> {
            anim.start();
        });
    }

    /**
     * Test cancel() on a test thread.
     */
    @Test
    public void testCancelOnNonAnimationHandlerThread() throws InterruptedException {
        mExpectedException.expect(AndroidRuntimeException.class);
        SpruceSpringAnimation anim = new SpruceSpringAnimation(mView1, SpruceDynamics.ALPHA, 0f);
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            anim.setAnimationHandler(anim.getAnimationHandler());
        });
        runRunnableOnNewThread(() -> {
            anim.cancel();
        });
    }

    /**
     * Test skipToEnd() on a test thread.
     */
    @Test
    public void testSkipToEndOnNonAnimationHandlerThread() throws InterruptedException {
        mExpectedException.expect(AndroidRuntimeException.class);
        SpruceSpringAnimation anim = new SpruceSpringAnimation(mView1, SpruceDynamics.ALPHA, 0f);
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            anim.setAnimationHandler(anim.getAnimationHandler());
        });
        runRunnableOnNewThread(() -> {
            anim.skipToEnd();
        });
    }

    /**
     * Runs {@param r} on a new looper thread, and propagates any runtime exceptions thrown while
     * {@param r} is running.
     */
    private void runRunnableOnNewThread(Runnable r) throws InterruptedException, RuntimeException {
        RuntimeException[] exceptions = new RuntimeException[1];
        CountDownLatch latch = new CountDownLatch(1);
        HandlerThread t = new HandlerThread("SpringTestsThread") {
            @Override
            public void run() {
                Looper.prepare();
                try {
                    r.run();
                } catch (RuntimeException e) {
                    exceptions[0] = e;
                }
                latch.countDown();
            }
        };
        t.start();
        latch.await(5, TimeUnit.SECONDS);
        if (exceptions[0] != null) {
            throw exceptions[0];
        }
    }

    /**
     * Test invalid start condition: no spring position specified, final position > max value,
     * and final position < min. Expect exception in all these cases.
     */
    @Test
    public void testInvalidStartingCondition() {
        final SpruceSpringAnimation anim = new SpruceSpringAnimation(mView1, SpruceDynamics.X);
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            // Expect exception from not setting spring final position before calling start.
            try {
                anim.start();
                fail("No exception is thrown when calling start() from non-main thread.");
            } catch (UnsupportedOperationException e) {
            }

            // Expect exception from having a final position < min value
            try {
                anim.setMinValue(50);
                // Final position < min value, expect exception.
                anim.setStartValue(50).animateToFinalPosition(40);
                fail("No exception is thrown when spring position is less than min value.");
            } catch (UnsupportedOperationException e) {
            }

            // Expect exception from not setting spring final position before calling start.
            try {
                anim.setMaxValue(60);
                // Final position < min value, expect exception.
                anim.setStartValue(60).animateToFinalPosition(70);
                fail("No exception is thrown when spring position is greater than max value.");
            } catch (UnsupportedOperationException e) {
            }
        });
    }

    /**
     * Try skipToEnd() on an undamped spring, and expect exception.
     */
    @Test
    public void testUndampedSpring() {
        final SpruceSpringAnimation anim = new SpruceSpringAnimation(mView1, SpruceDynamics.Y);
        anim.setSpring(new SpringForce(10).setDampingRatio(0));
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            // Expect exception for ending an undamped spring.
            try {
                anim.skipToEnd();
                fail("No exception is thrown when calling skipToEnd() on an undamped spring");
            } catch (UnsupportedOperationException e) {
            }
        });

    }

    @Test
    public void testCustomHandler() {
        final SpruceSpringAnimation anim = new SpruceSpringAnimation(mView1, SpruceDynamics.Y, 0f);
        MyAnimationFrameCallbackScheduler scheduler =
                new MyAnimationFrameCallbackScheduler();
        AnimationHandler handler = new AnimationHandler(scheduler);

        anim.setAnimationHandler(handler);

        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> anim.start());

        assertTrue(scheduler.mCallback);
        assertEquals(handler, anim.getAnimationHandler());
    }

    static class MyAnimationFrameCallbackScheduler implements
            AnimationHandler.FrameCallbackScheduler {

        boolean mCallback;

        @Override
        public void postFrameCallback(Runnable frameCallback) {
            mCallback = true;
        }

        @Override
        public boolean isCurrentThread() {
            return true;
        }
    }

    static class MyEndListener implements SpruceDynamics.OnAnimationEndListener {
        public long endTime = -1;

        @Override
        public void onAnimationEnd(SpruceDynamics animation, boolean canceled, float value,
                                   float velocity) {
            endTime = SystemClock.uptimeMillis();
        }
    }
}
