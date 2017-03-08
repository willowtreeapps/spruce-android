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
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;

import com.willowtreeapps.spruce.Spruce;
import com.willowtreeapps.spruce.animation.DefaultAnimations;
import com.willowtreeapps.spruce.sort.DefaultSort;
import com.willowtreeapps.spruce.sort.LinearSort;
import com.willowtreeapps.spruce.sort.SortFunction;
import com.willowtreeapps.spurceexampleapp.R;

import java.util.ArrayList;
import java.util.List;


public class ViewFragment extends Fragment {

    private Animator spruceAnimator;
    private GridLayout parent;
    private SeekBar seekBar;
    private Spinner sortDropDown;
    private RadioGroup linearRadioGroup;
    private CheckBox linearReversed;

    private List<View> children = new ArrayList<>();
    private Animator[] animators;
    private LinearSort.SpruceDirection direction;

    public static ViewFragment newInstance(){
        return new ViewFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        parent = (GridLayout) container.findViewById(R.id.view_group_to_animate);
        linearRadioGroup = (RadioGroup) container.findViewById(R.id.directional_radio_group);
        linearReversed = (CheckBox) container.findViewById(R.id.linear_reversed);
        seekBar = (SeekBar) container.findViewById(R.id.animation_seek);

        Resources res = getResources();
        int tileWidthAndHeight = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, res.getDisplayMetrics()));
        int tileMargins = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, res.getDisplayMetrics()));

        for (int i = 0; i < 100; i++) {
            View childView = new View(getContext());
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(tileWidthAndHeight, tileWidthAndHeight);
            params.setMargins(tileMargins, tileMargins, tileMargins, tileMargins);
            childView.setLayoutParams(params);
            childView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.spruceViewColor));
            childView.setAlpha(0F);
            parent.addView(childView);
            children.add(childView);
        }

        animators = new Animator[] {
                DefaultAnimations.shrinkAnimator(parent, /*duration=*/800),
                DefaultAnimations.fadeInAnimator(parent, /*duration=*/800)
        };

        View.OnClickListener click = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetChildViewsAndStartSort();
            }
        };
        container.setOnClickListener(click);
        parent.setOnClickListener(click);

        sortDropDown = (Spinner) container.findViewById(R.id.sort_selection);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.sort_functions,
                R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        sortDropDown.setAdapter(adapter);

        sortDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                resetChildViewsAndStartSort();
                if (position == 1) {
                    linearRadioGroup.setVisibility(View.VISIBLE);
                    linearReversed.setVisibility(View.VISIBLE);
                } else {
                    linearRadioGroup.setVisibility(View.GONE);
                    linearReversed.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                resetChildViewsAndStartSort();
            }
        });

        // set default direction
        direction = LinearSort.SpruceDirection.BOTTOM_TO_TOP;
        linearRadioGroup.check(R.id.bottom_to_top);
        linearRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId)
                {
                    case R.id.bottom_to_top:
                        direction = LinearSort.SpruceDirection.BOTTOM_TO_TOP;
                        break;
                    case R.id.top_to_bottom:
                        direction = LinearSort.SpruceDirection.TOP_TO_BOTTOM;
                        break;
                    case R.id.left_to_right:
                        direction = LinearSort.SpruceDirection.LEFT_TO_RIGHT;
                        break;
                    case R.id.right_to_left:
                        direction = LinearSort.SpruceDirection.RIGHT_TO_LEFT;
                        break;
                }
                resetChildViewsAndStartSort();
            }
        });

        linearReversed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                resetChildViewsAndStartSort();
            }
        });

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
        if (spruceAnimator != null) {
            spruceAnimator.cancel();
        }
        for (View view : children) {
            view.setAlpha(0);
        }
        setupSort();
    }

    private void setupSort() {
        SortFunction sortFunction;
        switch (sortDropDown.getSelectedItemPosition()) {
            case 0:
                sortFunction = new DefaultSort(seekBar.getProgress());
                break;
            case 1:
                sortFunction = new LinearSort(seekBar.getProgress(), linearReversed.isChecked(), direction);
                break;
            default:
                sortFunction = new DefaultSort(seekBar.getProgress());
                break;
        }

        spruceAnimator = new Spruce.SpruceBuilder(parent).sortWith(sortFunction)
                .animateWith(animators)
                .start();

    }

}
