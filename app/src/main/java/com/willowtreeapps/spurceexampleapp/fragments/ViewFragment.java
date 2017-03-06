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

package com.willowtreeapps.spurceexampleapp.fragments;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.SeekBar;
import android.widget.Spinner;

import com.willowtreeapps.spruce.Spruce;
import com.willowtreeapps.spruce.sort.DefaultSort;
import com.willowtreeapps.spurceexampleapp.R;

import java.util.ArrayList;
import java.util.List;


public class ViewFragment extends Fragment {

    private Spruce.SpruceBuilder spruce;
    private SeekBar seekBar;

    private List<View> children = new ArrayList<>();
    private Animator[] animators;

    public static ViewFragment newInstance(){
        return new ViewFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        final GridLayout parent = (GridLayout) container.findViewById(R.id.view_group_to_animate);
        spruce = new Spruce.SpruceBuilder(parent);

        seekBar = (SeekBar) container.findViewById(R.id.animation_seek);

        for (int i = 0; i < 100; i++) {
            View childView = new View(getContext());
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(60, 60);
            params.setMargins(4, 4, 4, 4);
            childView.setLayoutParams(params);
            childView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.spruceViewColor));
            childView.setAlpha(0F);
            parent.addView(childView);
            children.add(childView);
        }

        // TODO - move to the default animations once they're flushed out and available
        final ObjectAnimator growAnim = ObjectAnimator.ofPropertyValuesHolder(parent,
                PropertyValuesHolder.ofFloat(View.SCALE_X, 0.1F, 1.0F),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 0.1F, 1.0F))
                .setDuration(800);
        final ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(parent, View.ALPHA, 1F)
                .setDuration(800);

        animators = new Animator[]{growAnim, alphaAnim};

        View.OnClickListener click = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetChildViewsAndStartSort();
            }
        };
        container.setOnClickListener(click);
        parent.setOnClickListener(click);

        Spinner sortDropDown = (Spinner) container.findViewById(R.id.sort_selection);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.sort_functions,
                R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        sortDropDown.setAdapter(adapter);
        sortDropDown.setEnabled(false);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                resetChildViewsAndStartSort();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        resetChildViewsAndStartSort();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void resetChildViewsAndStartSort() {
        //spruce.stop(); TODO: Once stop is available
        for (View view : children) {
            view.setAlpha(0);
        }
        spruce.sortWith(new DefaultSort(seekBar.getProgress()))
                .animateWith(animators)
                .start();
    }

}
