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
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.willowtreeapps.spruce.Spruce;
import com.willowtreeapps.spruce.animation.DefaultAnimations;
import com.willowtreeapps.spruce.sort.ContinuousSort;
import com.willowtreeapps.spruce.sort.ContinuousWeightedSort;
import com.willowtreeapps.spruce.sort.CorneredSort;
import com.willowtreeapps.spruce.sort.DefaultSort;
import com.willowtreeapps.spruce.sort.InlineSort;
import com.willowtreeapps.spruce.sort.LinearSort;
import com.willowtreeapps.spruce.sort.RadialSort;
import com.willowtreeapps.spruce.sort.RandomSort;
import com.willowtreeapps.spruce.sort.SnakeSort;
import com.willowtreeapps.spruce.sort.SortFunction;
import com.willowtreeapps.spurceexampleapp.R;
import com.willowtreeapps.spurceexampleapp.SpruceActivity;
import com.willowtreeapps.spurceexampleapp.widgets.RadioGroupGridLayout;

import java.util.List;


public class ControlsFragment extends Fragment implements RadioGroupGridLayout.OnChangedListener {

    private static final int DEFAULT_SORT = 0;
    private static final int CORNERED_SORT = 1;
    private static final int CONTINUOUS_SORT = 2;
    private static final int CONTINUOUS_WEIGHTED_SORT = 3;
    private static final int INLINE_SORT = 4;
    private static final int LINEAR_SORT = 5;
    private static final int RADIAL_SORT = 6;
    private static final int RANDOM_SORT = 7;
    private static final int SNAKE_SORT = 8;

    private Animator spruceAnimator;
    private SeekBar seekBar;
    private Spinner sortDropDown;
    private RadioGroup linearRadioGroup;
    private RadioGroup corneredRadioGroup;
    private RadioGroupGridLayout positionalRadioGroup;
    private double verticalWeight;
    private double horizontalWeight;
    private CheckBox linearReversed;
    private LinearLayout verticalWeightLayout;
    private LinearLayout horizontalWeightLayout;
    private TextView animationEndText;
    private TextView seekBarTitle;
    private EditText codeSample;

    private Animator[] animators;
    private LinearSort.Direction direction;
    private CorneredSort.Corner corner;

    private GridLayout parent;
    private List<View> children;

    public static ControlsFragment newInstance() {
        return new ControlsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.controls_fragment,
                        container,
                        false);

        parent = (GridLayout) ((SpruceActivity) getActivity()).parent;
        children = ((SpruceActivity) getActivity()).children;
        sortDropDown = ((SpruceActivity) getActivity()).sortDropDown;

        linearRadioGroup = (RadioGroup) rootView.findViewById(R.id.directional_radio_group);
        corneredRadioGroup = (RadioGroup) rootView.findViewById(R.id.cornered_radio_group);
        positionalRadioGroup = (RadioGroupGridLayout) rootView.findViewById(R.id.positional_radio_group);
        RadioGroup verticalWeightedRadioGroup = (RadioGroup) rootView.findViewById(R.id.vertical_weighted_radio_group);
        RadioGroup horizontalWeightedRadioGroup = (RadioGroup) rootView.findViewById(R.id.horizontal_weighted_radio_group);
        verticalWeightLayout = (LinearLayout) rootView.findViewById(R.id.vertical_weight);
        horizontalWeightLayout = (LinearLayout) rootView.findViewById(R.id.horizontal_weight);
        linearReversed = (CheckBox) rootView.findViewById(R.id.linear_reversed);
        seekBar = (SeekBar) rootView.findViewById(R.id.animation_seek);
        animationEndText = (TextView) rootView.findViewById(R.id.animation_end);
        seekBarTitle = (TextView) rootView.findViewById(R.id.seek_bar_title);
        codeSample = (EditText) rootView.findViewById(R.id.code_sample);

        animators = new Animator[]{
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

        sortDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case SNAKE_SORT:
                    case CORNERED_SORT:
                    case INLINE_SORT:
                        linearRadioGroup.setVisibility(View.GONE);
                        linearReversed.setVisibility(View.VISIBLE);
                        positionalRadioGroup.setVisibility(View.GONE);
                        verticalWeightLayout.setVisibility(View.GONE);
                        horizontalWeightLayout.setVisibility(View.GONE);
                        corneredRadioGroup.setVisibility(View.VISIBLE);
                        break;
                    case LINEAR_SORT:
                        linearRadioGroup.setVisibility(View.VISIBLE);
                        linearReversed.setVisibility(View.VISIBLE);
                        positionalRadioGroup.setVisibility(View.GONE);
                        verticalWeightLayout.setVisibility(View.GONE);
                        horizontalWeightLayout.setVisibility(View.GONE);
                        corneredRadioGroup.setVisibility(View.GONE);
                        break;
                    case CONTINUOUS_SORT:
                    case RADIAL_SORT:
                        positionalRadioGroup.setVisibility(View.VISIBLE);
                        verticalWeightLayout.setVisibility(View.GONE);
                        horizontalWeightLayout.setVisibility(View.GONE);
                        linearReversed.setVisibility(View.VISIBLE);
                        linearRadioGroup.setVisibility(View.GONE);
                        corneredRadioGroup.setVisibility(View.GONE);
                        break;
                    case CONTINUOUS_WEIGHTED_SORT:
                        positionalRadioGroup.setVisibility(View.VISIBLE);
                        verticalWeightLayout.setVisibility(View.VISIBLE);
                        horizontalWeightLayout.setVisibility(View.VISIBLE);
                        linearReversed.setVisibility(View.VISIBLE);
                        linearRadioGroup.setVisibility(View.GONE);
                        corneredRadioGroup.setVisibility(View.GONE);
                        break;
                    default:
                        linearReversed.setVisibility(View.GONE);
                        positionalRadioGroup.setVisibility(View.GONE);
                        verticalWeightLayout.setVisibility(View.GONE);
                        horizontalWeightLayout.setVisibility(View.GONE);
                        linearRadioGroup.setVisibility(View.GONE);
                        corneredRadioGroup.setVisibility(View.GONE);
                        break;
                }
                resetChildViewsAndStartSort();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // set default vertical weight
        verticalWeight = ContinuousWeightedSort.MEDIUM_WEIGHT;
        verticalWeightedRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.vertical_light:
                        verticalWeight = ContinuousWeightedSort.LIGHT_WEIGHT;
                        break;
                    case R.id.vertical_medium:
                        verticalWeight = ContinuousWeightedSort.MEDIUM_WEIGHT;
                        break;
                    case R.id.vertical_heavy:
                        verticalWeight = ContinuousWeightedSort.HEAVY_WEIGHT;
                        break;
                }
                resetChildViewsAndStartSort();
            }
        });

        // set default horizontal weight
        horizontalWeight = ContinuousWeightedSort.MEDIUM_WEIGHT;
        horizontalWeightedRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.horizontal_light:
                        horizontalWeight = ContinuousWeightedSort.LIGHT_WEIGHT;
                        break;
                    case R.id.horizontal_medium:
                        horizontalWeight = ContinuousWeightedSort.MEDIUM_WEIGHT;
                        break;
                    case R.id.horizontal_heavy:
                        horizontalWeight = ContinuousWeightedSort.HEAVY_WEIGHT;
                        break;
                }
                resetChildViewsAndStartSort();
            }
        });

        // set default direction
        direction = LinearSort.Direction.BOTTOM_TO_TOP;
        linearRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
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
                switch (checkedId) {
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

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        positionalRadioGroup.onResume();
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
            case DEFAULT_SORT:
                sortFunction = new DefaultSort(seekBar.getProgress());
                codeSample.setText(String.format(getResources().getString(R.string.default_sort_code), seekBar.getProgress()));
                break;
            case CORNERED_SORT:
                sortFunction = new CorneredSort(seekBar.getProgress(), linearReversed.isChecked(), corner);
                codeSample.setText(String.format(getResources().getString(R.string.cornered_sort_code),
                        seekBar.getProgress(),
                        String.valueOf(linearReversed.isChecked()),
                        corner));
                break;
            case CONTINUOUS_SORT:
                sortFunction = new ContinuousSort(seekBar.getProgress() * /*timePaddingOffset=*/20,
                        linearReversed.isChecked(),
                        positionalRadioGroup.getPosition());
                codeSample.setText(String.format(getResources().getString(R.string.continuous_sort_code),
                        seekBar.getProgress() * /*timePaddingOffset=*/20,
                        String.valueOf(linearReversed.isChecked()),
                        positionalRadioGroup.getPosition()));
                break;
            case CONTINUOUS_WEIGHTED_SORT:
                sortFunction = new ContinuousWeightedSort(seekBar.getProgress() * /*timePaddingOffset=*/20,
                        linearReversed.isChecked(),
                        positionalRadioGroup.getPosition(),
                        horizontalWeight,
                        verticalWeight);
                codeSample.setText(String.format(getResources().getString(R.string.continuous_weighted_sort_code),
                        seekBar.getProgress() * /*timePaddingOffset=*/20,
                        String.valueOf(linearReversed.isChecked()),
                        positionalRadioGroup.getPosition(),
                        String.valueOf(horizontalWeight),
                        String.valueOf(verticalWeight)));
                break;
            case INLINE_SORT:
                sortFunction = new InlineSort(seekBar.getProgress(), linearReversed.isChecked(), corner);
                codeSample.setText(String.format(getResources().getString(R.string.inline_sort_code),
                        seekBar.getProgress(),
                        String.valueOf(linearReversed.isChecked()),
                        corner));
                break;
            case LINEAR_SORT:
                sortFunction = new LinearSort(seekBar.getProgress(), linearReversed.isChecked(), direction);
                codeSample.setText(String.format(getResources().getString(R.string.linear_sort_code),
                        seekBar.getProgress(),
                        String.valueOf(linearReversed.isChecked()),
                        direction));
                break;
            case RADIAL_SORT:
                sortFunction = new RadialSort(seekBar.getProgress(), linearReversed.isChecked(), positionalRadioGroup.getPosition());
                codeSample.setText(String.format(getResources().getString(R.string.radial_sort_code), seekBar.getProgress(),
                        String.valueOf(linearReversed.isChecked()),
                        positionalRadioGroup.getPosition()));
                break;
            case RANDOM_SORT:
                sortFunction = new RandomSort(seekBar.getProgress());
                codeSample.setText(String.format(getResources().getString(R.string.random_sort_code), seekBar.getProgress()));
                break;
            case SNAKE_SORT:
                sortFunction = new SnakeSort(seekBar.getProgress(), linearReversed.isChecked(), corner);
                codeSample.setText(String.format(getResources().getString(R.string.snake_sort_code),
                        seekBar.getProgress(),
                        String.valueOf(linearReversed.isChecked()),
                        corner));
                break;
            default:
                sortFunction = new DefaultSort(seekBar.getProgress());
                codeSample.setText(String.format(getResources().getString(R.string.default_sort_code), seekBar.getProgress()));
                break;
        }

        if (sortDropDown.getSelectedItemPosition() == CONTINUOUS_SORT ||
                sortDropDown.getSelectedItemPosition() == CONTINUOUS_WEIGHTED_SORT) {
            animationEndText.setText(R.string.animation_end_longer_duration);
            seekBarTitle.setText(R.string.duration);
        } else {
            animationEndText.setText(R.string.animation_end_duration);
            seekBarTitle.setText(R.string.delay);
        }

        spruceAnimator = new Spruce.SpruceBuilder(parent).sortWith(sortFunction)
                .animateWith(animators)
                .start();

    }

}
