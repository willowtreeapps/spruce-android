package com.willowtreeapps.spruce.interpolators;

import android.os.Build;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class SpruceInterpolators {

    /**
     * Ease Interpolator
     */
    public static final Interpolator EASE = new PathInterpolator(
            0.250f,
            0.100f,
            0.250f,
            1.000f);

    /**
     * Ease-In Interpolator
     */
    public static final Interpolator EASE_IN = new PathInterpolator(
            0.420f,
            0.000f,
            1.000f,
            1.000f);

    /**
     * Ease-In Interpolator
     */
    public static final Interpolator EASE_OUT = new PathInterpolator(
            0.000f,
            0.000f,
            0.580f,
            1.000f);

    /**
     * Ease-In-Out Interpolator
     */
    public static final Interpolator EASE_IN_OUT = new PathInterpolator(
            0.420f,
            0.000f,
            0.580f,
            1.000f);

    // PENNER EQUATIONS - Approximated. please refer to this for more details-> http://robertpenner.com/easing/

    /**
     * Ease-In-Quad Interpolator
     */
    public static final Interpolator EASE_IN_QUAD = new PathInterpolator(
            0.550f,
            0.085f,
            0.680f,
            0.530f);

    /**
     * Ease-In-Cubic Interpolator
     */
    public static final Interpolator EASE_IN_CUBIC = new PathInterpolator(
            0.550f,
            0.055f,
            0.675f,
            0.190f);

    /**
     * Ease-In-Quart Interpolator
     */
    public static final Interpolator EASE_IN_QUART = new PathInterpolator(
            0.895f,
            0.030f,
            0.685f,
            0.220f);

    /**
     * Ease-In-Quint Interpolator
     */
    public static final Interpolator EASE_IN_QUINT = new PathInterpolator(
            0.755f,
            0.050f,
            0.855f,
            0.060f);

    /**
     * Ease-In-Sine Interpolator
     */
    public static final Interpolator EASE_IN_SINE = new PathInterpolator(
            0.470f,
            0.000f,
            0.745f,
            0.715f);

    /**
     * Ease-In-Exop Interpolator
     */
    public static final Interpolator EASE_IN_EXPO = new PathInterpolator(
            0.950f,
            0.050f,
            0.795f,
            0.035f);

    /**
     * Ease-In-Circ Interpolator
     */
    public static final Interpolator EASE_IN_CIRC = new PathInterpolator(
            0.600f,
            0.040f,
            0.980f,
            0.335f);

    /**
     * Ease-In-Back Interpolator
     */
    public static final Interpolator EASE_IN_BACK = new PathInterpolator(
            0.600f,
            -0.280f,
            0.735f,
            0.045f);

    /**
     * Ease-Out-Quad Interpolator
     */
    public static final Interpolator EASE_OUT_QUAD = new PathInterpolator(
            0.250f,
            0.460f,
            0.450f,
            0.940f);

    /**
     * Ease-Out-Cubic Interpolator
     */
    public static final Interpolator EASE_OUT_CUBIC = new PathInterpolator(
            0.215f,
            0.610f,
            0.355f,
            1.000f);

    /**
     * Ease-Out-Quart Interpolator
     */
    public static final Interpolator EASE_OUT_QUART = new PathInterpolator(
            0.165f,
            0.840f,
            0.440f,
            1.000f);

    /**
     * Ease-Out-Quint Interpolator
     */
    public static final Interpolator EASE_OUT_QUINT = new PathInterpolator(
            0.230f,
            1.000f,
            0.320f,
            1.000f);

    /**
     * Ease-Out-Sine Interpolator
     */
    public static final Interpolator EASE_OUT_SINE = new PathInterpolator(
            0.390f,
            0.575f,
            0.565f,
            1.000f);

    /**
     * Ease-Out-Exop Interpolator
     */
    public static final Interpolator EASE_OUT_EXPO = new PathInterpolator(
            0.190f,
            1.000f,
            0.220f,
            1.000f);


    /**
     * Ease-Out-Circ Interpolator
     */
    public static final Interpolator EASE_OUT_CIRC = new PathInterpolator(
            0.075f,
            0.820f,
            0.165f,
            1.000f);

    /**
     * Ease-Out-Back Interpolator
     */
    public static final Interpolator EASE_OUT_BACK = new PathInterpolator(
            0.175f,
            0.885f,
            0.320f,
            1.275f);

    /**
     * Ease-In-Out-Quad Interpolator
     */
    public static final Interpolator EASE_IN_OUT_QUAD = new PathInterpolator(
            0.455f,
            0.030f,
            0.515f,
            0.955f);

    /**
     * Ease-In-Out-Cubic Interpolator
     */
    public static final Interpolator EASE_IN_OUT_CUBIC = new PathInterpolator(
            0.645f,
            0.045f,
            0.355f,
            1.000f);

    /**
     * Ease-In-Out-Quart Interpolator
     */
    public static final Interpolator EASE_IN_OUT_QUART = new PathInterpolator(
            0.770f,
            0.000f,
            0.175f,
            1.000f);

    /**
     * Ease-In-Out-Quint Interpolator
     */
    public static final Interpolator EASE_IN_OUT_QUINT = new PathInterpolator(
            0.770f,
            0.000f,
            0.175f,
            1.000f);

    /**
     * Ease-In-Out-Sine Interpolator
     */
    public static final Interpolator EASE_IN_OUT_SINE = new PathInterpolator(
            0.445f,
            0.050f,
            0.550f,
            0.950f);

    /**
     * Ease-In-Out-Expo Interpolator
     */
    public static final Interpolator EASE_IN_OUT_EXPO = new PathInterpolator(
            1.000f,
            0.000f,
            0.000f,
            1.000f);

    /**
     * Ease-In-Out-Circ Interpolator
     */
    public static final Interpolator EASE_IN_OUT_CIRC = new PathInterpolator(
            0.785f,
            0.135f,
            0.150f,
            0.860f);

    /**
     * Ease-In-Out-Back Interpolator
     */
    public static final Interpolator EASE_IN_OUT_BACK = new PathInterpolator(
            0.680f,
            -0.550f,
            0.265f,
            1.550f);


}
