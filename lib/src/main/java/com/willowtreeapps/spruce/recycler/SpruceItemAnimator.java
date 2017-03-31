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

package com.willowtreeapps.spruce.recycler;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.support.v7.widget.DefaultItemAnimator;
import android.view.View;
import android.view.ViewGroup;

import com.willowtreeapps.spruce.sort.SortFunction;
import com.willowtreeapps.spruce.sort.SpruceTimedView;

import java.util.ArrayList;
import java.util.List;


public class SpruceItemAnimator extends DefaultItemAnimator {

    private ViewGroup parent;
    private SortFunction sort;
    private Animator[] animators;

    /**
     * ItemAnimator for RecyclerView item animations
     *
     * @param parent the RecyclerView
     * @param sort SortFunction for the desired sort effect
     * @param animators varargs of Animator
     */
    public SpruceItemAnimator(ViewGroup parent, SortFunction sort, Animator... animators) {
        this.parent = parent;
        this.sort = sort;
        this.animators = animators;
    }

    @Override
    public void runPendingAnimations() {
        super.runPendingAnimations();

        List<SpruceTimedView> childrenWithTime;
        List<View> children = new ArrayList<>();

        for (int i = 0; i < parent.getChildCount(); i++) {
            children.add(parent.getChildAt(i));
        }

        childrenWithTime = sort.getViewListWithTimeOffsets(parent, children);
        AnimatorSet animatorSet = new AnimatorSet();
        List<Animator> animatorsList = new ArrayList<>();

        for (SpruceTimedView childView : childrenWithTime) {
            for (Animator animatorChild : animators) {
                Animator animatorCopy = animatorChild.clone();
                animatorCopy.setTarget(childView.getView());
                animatorCopy.setStartDelay(childView.getTimeOffset());
                animatorsList.add(animatorCopy);
            }
        }
        animatorSet.playTogether(animatorsList);

        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                dispatchAnimationsFinished();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        animatorSet.start();
    }

}
