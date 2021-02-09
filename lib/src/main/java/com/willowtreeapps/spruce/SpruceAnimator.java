package com.willowtreeapps.spruce;

import android.animation.AnimatorSet;

import com.willowtreeapps.spruce.dynamics.DynamicAnimatorSet;

/**
 * This is a wrapper class for holding animation set and {@link DynamicAnimatorSet}
 */
public class SpruceAnimator {
    private AnimatorSet animatorSet = new AnimatorSet();
    private DynamicAnimatorSet dynamicAnimatorSet = new DynamicAnimatorSet();

    public AnimatorSet getAnimatorSet() {
        return animatorSet;
    }

    public void setAnimatorSet(AnimatorSet animatorSet) {
        this.animatorSet = animatorSet;
    }

    public DynamicAnimatorSet getDynamicAnimatorSet() {
        return dynamicAnimatorSet;
    }

    public void setDynamicAnimatorSet(DynamicAnimatorSet dynamicAnimatorSet) {
        this.dynamicAnimatorSet = dynamicAnimatorSet;
    }

    /**
     * cancels all the animation that are presently performed.
     */
    public void cancel() {
        animatorSet.cancel();
        dynamicAnimatorSet.cancel();
    }

    /**
     * starts all the animations that are queued.
     */
    public void start() {
        animatorSet.start();
        dynamicAnimatorSet.start();
    }
}
