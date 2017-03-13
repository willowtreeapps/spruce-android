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
import android.widget.GridLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;

import com.willowtreeapps.spruce.Spruce;
import com.willowtreeapps.spruce.animation.DefaultAnimations;
import com.willowtreeapps.spruce.sort.ContinuousSort;
import com.willowtreeapps.spruce.sort.CorneredSort;
import com.willowtreeapps.spruce.sort.DefaultSort;
import com.willowtreeapps.spruce.sort.LinearSort;
import com.willowtreeapps.spruce.sort.RadialSort;
import com.willowtreeapps.spruce.sort.RandomSort;
import com.willowtreeapps.spruce.sort.SortFunction;
import com.willowtreeapps.spurceexampleapp.R;
import com.willowtreeapps.spurceexampleapp.widgets.RadioGroupGridLayout;

import java.util.ArrayList;
import java.util.List;


public class ViewFragment extends Fragment implements RadioGroupGridLayout.OnChangedListener {

    private Animator spruceAnimator;
    private GridLayout parent;
    private SeekBar seekBar;
    private Spinner sortDropDown;
    private RadioGroup linearRadioGroup;
    private RadioGroup corneredRadioGroup;
    private RadioGroupGridLayout positionalRadioGroup;
    private CheckBox linearReversed;

    private List<View> children = new ArrayList<>();
    private Animator[] animators;
    private LinearSort.Direction direction;
    private CorneredSort.Corner corner;

    public static ViewFragment newInstance(){
        return new ViewFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        parent = (GridLayout) container.findViewById(R.id.view_group_to_animate);
        linearRadioGroup = (RadioGroup) container.findViewById(R.id.directional_radio_group);
        corneredRadioGroup = (RadioGroup) container.findViewById(R.id.cornered_radio_group);
        positionalRadioGroup = (RadioGroupGridLayout) container.findViewById(R.id.positional_radio_group);
        linearReversed = (CheckBox) container.findViewById(R.id.linear_reversed);
        seekBar = (SeekBar) container.findViewById(R.id.animation_seek);

        Resources res = getResources();
        int tileWidthAndHeight = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, res.getDisplayMetrics()));
        int tileMargins = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, res.getDisplayMetrics()));

        for (int i = 0; i < 100; i++) {
            View childView = new View(getContext());
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.height = tileWidthAndHeight;
            params.width = tileWidthAndHeight;
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
                    linearRadioGroup.setVisibility(View.GONE);
                    linearReversed.setVisibility(View.GONE);
                    positionalRadioGroup.setVisibility(View.GONE);
                    corneredRadioGroup.setVisibility(View.VISIBLE);
                } else if (position == 3) {
                    linearRadioGroup.setVisibility(View.VISIBLE);
                    linearReversed.setVisibility(View.VISIBLE);
                    positionalRadioGroup.setVisibility(View.GONE);
                    corneredRadioGroup.setVisibility(View.GONE);
                } else if (position == 2 || position == 4) {
                    positionalRadioGroup.setVisibility(View.VISIBLE);
                    linearReversed.setVisibility(View.VISIBLE);
                    linearRadioGroup.setVisibility(View.GONE);
                    corneredRadioGroup.setVisibility(View.GONE);
                } else {
                    linearReversed.setVisibility(View.GONE);
                    positionalRadioGroup.setVisibility(View.GONE);
                    linearRadioGroup.setVisibility(View.GONE);
                    corneredRadioGroup.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                resetChildViewsAndStartSort();
            }
        });

        // set default direction
        direction = LinearSort.Direction.BOTTOM_TO_TOP;
        linearRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId)
                {
                    case R.id.bottom_to_top:
                        direction = LinearSort.Direction.BOTTOM_TO_TOP;
                        break;
                    case R.id.top_to_bottom:
                        direction = LinearSort.Direction.TOP_TO_BOTTOM;
                        break;
                    case R.id.left_to_right:
                        direction = LinearSort.Direction.LEFT_TO_RIGHT;
                        break;
                    case R.id.right_to_left:
                        direction = LinearSort.Direction.RIGHT_TO_LEFT;
                        break;
                }
                resetChildViewsAndStartSort();
            }
        });

        // set default corner
        corner = CorneredSort.Corner.TOP_LEFT;
        corneredRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId)
                {
                    case R.id.top_left:
                        corner = CorneredSort.Corner.TOP_LEFT;
                        break;
                    case R.id.top_right:
                        corner = CorneredSort.Corner.TOP_RIGHT;
                        break;
                    case R.id.bottom_left:
                        corner = CorneredSort.Corner.BOTTOM_LEFT;
                        break;
                    case R.id.bottom_right:
                        corner = CorneredSort.Corner.BOTTOM_RIGHT;
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

        positionalRadioGroup.setGroupChildChangedListener(this);
        resetChildViewsAndStartSort();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onRadioGroupChildChanged() {
        resetChildViewsAndStartSort();
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
                sortFunction = new CorneredSort(seekBar.getProgress(), linearReversed.isChecked(), corner);
                break;
            case 2:
                sortFunction = new ContinuousSort(seekBar.getProgress() * 20, linearReversed.isChecked(), positionalRadioGroup.getPosition());
                break;
            case 3:
                sortFunction = new LinearSort(seekBar.getProgress(), linearReversed.isChecked(), direction);
                break;
            case 4:
                sortFunction = new RadialSort(seekBar.getProgress(), linearReversed.isChecked(), positionalRadioGroup.getPosition());
                break;
            case 5:
                sortFunction = new RandomSort(seekBar.getProgress());
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
