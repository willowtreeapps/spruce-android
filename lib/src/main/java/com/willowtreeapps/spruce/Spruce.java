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
import android.view.ViewGroup;

import com.willowtreeapps.spruce.SortFunctions.SortFunction;

public class Spruce {

    private final ViewGroup viewGroup;
    private final Animator animator;
    private final SortFunction sortFunction;

    private Spruce(SpruceBuilder builder) {
        this.viewGroup = builder.viewGroup;
        this.animator = builder.animator;
        this.sortFunction = builder.sortFunction;

        if (animator != null) {
            animator.setTarget(viewGroup);
            animator.start();
        }
    }

    public ViewGroup getViewGroup() {
        return viewGroup;
    }

    public Animator getAnimator() {
        return animator;
    }

    public SortFunction getSortFunction() {
        return sortFunction;
    }


    public static class SpruceBuilder {

        private final ViewGroup viewGroup;
        private Animator animator;
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
         * Apply an animator for animating the ViewGroup
         *
         * @param animator to apply to the ViewGroup
         * @return SpruceBuilder object
         */
        public SpruceBuilder animateWith(Animator animator) {
            this.animator = animator;
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
