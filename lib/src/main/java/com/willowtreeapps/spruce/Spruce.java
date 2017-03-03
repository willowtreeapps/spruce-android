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

import com.willowtreeapps.spruce.sort.SortFunction;
import com.willowtreeapps.spruce.sort.SpruceTimedView;

import java.util.ArrayList;
import java.util.List;

public class Spruce {

    private final ViewGroup viewGroup;
    private final List<Animator> animatorList;
    private final SortFunction sortFunction;

    private Spruce(SpruceBuilder builder) {
        this.viewGroup = builder.viewGroup;
        this.animatorList = builder.animatorList;
        this.sortFunction = builder.sortFunction;

        if (sortFunction != null) {
            if (animatorList != null) {
                setupSortWithAnimators();
            }
        }
    }

    private void setupSortWithAnimators() {
        List<SpruceTimedView> childrenWithTime;
        List<View> children = new ArrayList<>();

        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            children.add(viewGroup.getChildAt(i));
        }

        childrenWithTime = sortFunction.getViewListWithTimeOffsets(children);
        AnimatorSet animatorSet = new AnimatorSet();
        List<Animator> animators = new ArrayList<>();

        for (SpruceTimedView childView : childrenWithTime) {
            for (Animator animatorChild : animatorList) {
                Animator animatorCopy = animatorChild.clone();
                animatorCopy.setTarget(childView.getView());
                animatorCopy.setStartDelay(childView.getTimeOffset());
                animators.add(animatorCopy);
            }
        }

        animatorSet.playTogether(animators);
        animatorSet.start();
    }

    public ViewGroup getViewGroup() {
        return viewGroup;
    }

    public List<Animator> getAnimatorList() {
        return animatorList;
    }

    public SortFunction getSortFunction() {
        return sortFunction;
    }


    public static class SpruceBuilder {

        private final ViewGroup viewGroup;
        private List<Animator> animatorList;
        private SortFunction sortFunction;

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
         * Apply one to many animations to the ViewGroup
         * @param animators List of animators to apply
         * @return SpruceBuilder object
         */
        public SpruceBuilder animateWith(List<Animator> animators) {
            this.animatorList = animators;
            return this;
        }

        /**
         * Creates a Spruce instance and starts the sequence of animations
         *
         * @return Spruce The Spruce object to apply operations to.
         */
        public Spruce start() {
            return new Spruce(this);
        }

    }
}
