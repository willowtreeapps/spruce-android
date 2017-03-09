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

package com.willowtreeapps.spurceexampleapp.widgets;

import android.content.Context;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.RadioButton;

import com.willowtreeapps.spruce.sort.RadialSort;
import com.willowtreeapps.spurceexampleapp.R;


public class RadioGroupGridLayout extends GridLayout implements View.OnClickListener {

    private RadioButton activeRadioButton;
    private RadialSort.Position position;
    private OnChangedListener listener;

    public RadioGroupGridLayout(Context context) {
        super(context);
        init();
    }

    public RadioGroupGridLayout(Context context, OnChangedListener listener) {
        super(context);
        this.listener = listener;
    }

    public RadioGroupGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // set default position
        position = RadialSort.Position.TOP_LEFT;
    }

    @Override
    public void onClick(View view) {
        final RadioButton rb = (RadioButton) view;
        if ( activeRadioButton != null ) {
            activeRadioButton.setChecked(false);
        }
        rb.setChecked(true);
        activeRadioButton = rb;

        switch (getCheckedRadioButtonId())
        {
            case R.id.top_left:
                position = RadialSort.Position.TOP_LEFT;
                break;
            case R.id.top_middle:
                position = RadialSort.Position.TOP_MIDDLE;
                break;
            case R.id.top_right:
                position = RadialSort.Position.TOP_RIGHT;
                break;
            case R.id.right:
                position = RadialSort.Position.RIGHT;
                break;
            case R.id.middle:
                position = RadialSort.Position.MIDDLE;
                break;
            case R.id.left:
                position = RadialSort.Position.LEFT;
                break;
            case R.id.bottom_left:
                position = RadialSort.Position.BOTTOM_LEFT;
                break;
            case R.id.bottom_middle:
                position = RadialSort.Position.BOTTOM_MIDDLE;
                break;
            case R.id.bottom_right:
                position = RadialSort.Position.BOTTOM_RIGHT;
                break;
        }
        listener.onRadioGroupChildChanged();
    }



    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        super.addView(child, params);
        setChildrenOnClickListener((AppCompatRadioButton) child);
    }

    private void setChildrenOnClickListener(AppCompatRadioButton child) {
        GridLayout parent = (GridLayout) child.getParent();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View v = parent.getChildAt(i);
            if (v instanceof AppCompatRadioButton) {
                if (((RadioButton) v).isChecked()) {
                    activeRadioButton = (AppCompatRadioButton) v;
                }
                v.setOnClickListener(this);
            }
        }
    }

    private int getCheckedRadioButtonId() {
        if (activeRadioButton != null) {
            return activeRadioButton.getId();
        }

        return -1;
    }

    public RadialSort.Position getPosition() {
        return position;
    }

    public void setGroupChildChangedListener(OnChangedListener listener) {
        this.listener = listener;
    }

    public interface OnChangedListener {
        void onRadioGroupChildChanged();
    }

}
