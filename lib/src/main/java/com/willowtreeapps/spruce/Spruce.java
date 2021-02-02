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

package com.willowtreeapps.spruce;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;

import com.willowtreeapps.spruce.dynamics.DynamicAnimation;
import com.willowtreeapps.spruce.dynamics.DynamicAnimatorSet;
import com.willowtreeapps.spruce.dynamics.FlingAnimation;
import com.willowtreeapps.spruce.dynamics.SpringAnimation;
import com.willowtreeapps.spruce.exclusion.ExclusionHelper;
import com.willowtreeapps.spruce.sort.SortFunction;
import com.willowtreeapps.spruce.sort.SpruceTimedView;

import java.util.ArrayList;
import java.util.List;

public class Spruce {

    private final ViewGroup viewGroup;
    private SpruceAnimator animator;

    private Spruce(SpruceBuilder builder) throws IllegalArgumentException {
        this.viewGroup = builder.viewGroup;
        Object[] animators = builder.animators;
        SortFunction sortFunction = builder.sortFunction;

        if (animators == null) {
            throw new IllegalArgumentException("Animator array must not be null");
        } else if (sortFunction == null) {
            throw new IllegalArgumentException("SortFunction must not be null");
        }

        getAnimatorSetForSort(
                animators,
                sortFunction,
                builder.exclusionHelper,
                builder.interpolator);
    }

    private void getAnimatorSetForSort(Object[] animators, SortFunction sortFunction,
                                       ExclusionHelper exclusionHelper,
                                       Interpolator interpolator) {
        List<SpruceTimedView> childrenWithTime;

        // starts the filtering process
        List<View> children = exclusionHelper.filterViews(viewGroup);

        sortFunction.sortChildren(viewGroup, children);
        childrenWithTime = sortFunction.getViewListWithTimeOffsets(viewGroup, children);
        AnimatorSet animatorSet = new AnimatorSet();
        DynamicAnimatorSet dynamicAnimatorSet = new DynamicAnimatorSet();
        animator = new SpruceAnimator();
        List<Animator> animatorsList = new ArrayList<>();
        List<DynamicAnimation<?>> dynamicAnimatorsList = new ArrayList<>();

        //This max value is used to the time of interpolation.
        float maxTimeOffset = childrenWithTime.get(childrenWithTime.size() - 1).getTimeOffset();

        for (SpruceTimedView childView : childrenWithTime) {
            for (Object animatorChild : animators) {
                sanityCheck(animatorChild);
                if (animatorChild instanceof Animator) {
                    Animator animatorCopy = ((Animator) animatorChild).clone();
                    animatorCopy.setTarget(childView.getView());
                    // Seeks to the initial position
                    animatorCopy.end();
                    // Core logic of the interpolation.
                    animatorCopy.setStartDelay((long) (maxTimeOffset
                            * interpolator.getInterpolation(childView.getTimeOffset() / maxTimeOffset)));
                    animatorCopy.setDuration(((Animator) animatorChild).getDuration());
                    animatorsList.add(animatorCopy);
                } else if (animatorChild instanceof SpringAnimation) {
                    SpringAnimation animation = ((SpringAnimation) animatorChild);
                    // Cloning Spring Animation.
                    SpringAnimation animationClone = new SpringAnimation(childView.getView(), animation.mProperty,
                            animation.getSpring().getFinalPosition()).setStartValue(animation.mValue);
                    animationClone.setSpring(animation.getSpring());
                    animationClone.setMinValue(animation.mMinValue);
                    animationClone.setMaxValue(animation.mMaxValue);
                    // Setting start delay
                    animationClone.setStartDelay((long) (maxTimeOffset
                            * interpolator.getInterpolation(childView.getTimeOffset() / maxTimeOffset)));
                    dynamicAnimatorsList.add(animationClone);
                    // seeking the animation to first frame
                    animation.mProperty.setValue(childView.getView(), animation.mValue);
                } else if (animatorChild instanceof FlingAnimation) {
                    FlingAnimation animation = ((FlingAnimation) animatorChild);
                    // Cloning Spring Animation.
                    FlingAnimation animationClone = new FlingAnimation(childView.getView(), animation.mProperty)
                            .setStartValue(animation.mValue);
                    animationClone.setMaxValue(animation.mMaxValue);
                    animationClone.setMinValue(animation.mMinValue);
                    animationClone.setFriction(animation.getFriction());
                    animationClone.setStartVelocity(animation.mVelocity);
                    // Setting start delay
                    animationClone.setStartDelay((long) (maxTimeOffset
                            * interpolator.getInterpolation(childView.getTimeOffset() / maxTimeOffset)));
                    dynamicAnimatorsList.add(animationClone);
                    // seeking the animation to first frame
                    animation.mProperty.setValue(childView.getView(), animation.mValue);
                }
            }

        }

        // Queueing Animations.
        dynamicAnimatorSet.playTogether(dynamicAnimatorsList);
        animatorSet.playTogether(animatorsList);

        //Playing Animations.
        dynamicAnimatorSet.start();
        animatorSet.start();

        //Providing the user all the animations for the ease of cancelling and starting.
        animator.setAnimatorSet(animatorSet);
        animator.setDynamicAnimatorSet(dynamicAnimatorSet);
    }

    /**
     * Sanity check is important, this will restrict the user to use only {@link Animator} and
     * {@link DynamicAnimation}
     *
     * @param animatorChild current object from the loop.
     */
    private void sanityCheck(Object animatorChild) {
        if (!(animatorChild instanceof DynamicAnimation<?>) &&
                !(animatorChild instanceof Animator)) {
            throw new UnsupportedOperationException("Error: Items added for animation should be the subtype of"
                    + "DynamicAnimation or Animator.");
        }
    }

    public static class SpruceBuilder {

        private final ViewGroup viewGroup;
        private final ExclusionHelper exclusionHelper = new ExclusionHelper();
        private Object[] animators;
        private SortFunction sortFunction;
        private Interpolator interpolator = new LinearInterpolator();

        /**
         * SpruceBuilder constructor that takes a ViewGroup
         *
         * @param viewGroup to apply animations and other operations to
         */
        public SpruceBuilder(ViewGroup viewGroup) {
            this.viewGroup = viewGroup;
        }


        /**
         * SortFunction to animate the ViewGroup
         *
         * @param function subclass of SortFunction to be applied to the ViewGroup
         * @return SpruceBuilder object
         */
        public SpruceBuilder sortWith(SortFunction function) {
            this.sortFunction = function;
            return this;
        }

        /**
         * excludeViews to exclude the view with Ids as a list of integers
         *
         * @param exclusionList list of ids that are excluded from the choreographed spruce animation.
         * @param mode          there are two modes for exclusion
         *                      1. R_L_MODE : In this mode, you can set the positions of the list view
         *                      / recycler view that is to be excluded/
         *                      2. NORMAL_MODE : This mode is used to exclude the views from view groups
         *                      other than recycler view/ list view.
         * @return SpruceBuilder object
         */
        public SpruceBuilder excludeViews(@NonNull List<Integer> exclusionList, int mode) {
            exclusionHelper.initialize(exclusionList, mode);
            return this;
        }

        /**
         * addInterpolator adds the interpolator to the {@link AnimatorSet}, This gives the user
         * complete control over the overall flow of the animation.
         * <p>
         * A {@link LinearInterpolator} is substituted of the user doesn't add an interpolator.
         *
         * @param interpolator interpolator for the animation set.
         * @return SpruceBuilder object
         */
        public SpruceBuilder addInterpolator(Interpolator interpolator) {
            this.interpolator = interpolator;
            return this;
        }

        /**
         * Apply one to many animations to the ViewGroup
         *
         * @param animators Object array to apply to the ViewGroup children animations
         * @return SpruceBuilder object
         */
        public SpruceBuilder animateWith(Object... animators) {
            this.animators = animators;
            return this;
        }

        /**
         * Creates a Spruce instance and starts the sequence of animations
         *
         * @return SpruceAnimator The object is a wrapper that contains
         * both native and {@link SpringAnimation}
         */
        public SpruceAnimator start() {
            Spruce spruce = new Spruce(this);
            return spruce.animator;
        }
    }
}
