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
    private AnimatorSet animatorSet;

    private Spruce(SpruceBuilder builder) throws IllegalArgumentException {
        this.viewGroup = builder.viewGroup;
        Animator[] animators = builder.animators;
        SortFunction sortFunction = builder.sortFunction;

        if (animators == null) {
            throw new IllegalArgumentException("Animator array must not be null");
        } else if (sortFunction == null) {
            throw new IllegalArgumentException("SortFunction must not be null");
        }

        getAnimatorSetForSort(animators, sortFunction).start();
    }

    private AnimatorSet getAnimatorSetForSort(Animator[] animators, SortFunction sortFunction) {
        List<SpruceTimedView> childrenWithTime;
        List<View> children = new ArrayList<>();

        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            children.add(viewGroup.getChildAt(i));
        }

        sortFunction.sortChildren(viewGroup, children);
        childrenWithTime = sortFunction.getViewListWithTimeOffsets(viewGroup, children);
        animatorSet = new AnimatorSet();
        List<Animator> animatorsList = new ArrayList<>();

        for (SpruceTimedView childView : childrenWithTime) {
            for (Animator animatorChild : animators) {
                Animator animatorCopy = animatorChild.clone();
                animatorCopy.setTarget(childView.getView());
                animatorCopy.start();
                animatorCopy.cancel();
                animatorCopy.setStartDelay(childView.getTimeOffset());
                animatorsList.add(animatorCopy);
            }
        }
        animatorSet.playTogether(animatorsList);

        return animatorSet;
    }

    public static class SpruceBuilder {

        private final ViewGroup viewGroup;
        private Animator[] animators;
        private SortFunction sortFunction;
        private Spruce spruce;

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
         * @param animators Animator array to apply to the ViewGroup children
         * @return SpruceBuilder object
         */
        public SpruceBuilder animateWith(Animator... animators) {
            this.animators = animators;
            return this;
        }

        /**
         * Creates a Spruce instance and starts the sequence of animations
         *
         * @return Spruce The Spruce object to apply operations to.
         */
        public Animator start() {
            spruce = new Spruce(this);
            return spruce.animatorSet;
        }
    }
}
