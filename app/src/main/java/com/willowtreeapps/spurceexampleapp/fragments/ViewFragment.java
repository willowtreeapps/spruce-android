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

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.GridLayout;

import com.willowtreeapps.spurceexampleapp.R;
import com.willowtreeapps.spurceexampleapp.widgets.CardLayout;

import java.util.ArrayList;
import java.util.List;


public class ViewFragment extends Fragment {

    OnParentAndChildCreationListener listener;

    private GridLayout parent;
    private List<View> children = new ArrayList<>();

    public static ViewFragment newInstance(){
        return new ViewFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.view_fragment, container, false);
        parent = (GridLayout) rootView.findViewById(R.id.view_group_to_animate);

        final int CHILD_VIEW_COUNT = parent.getColumnCount() * parent.getRowCount();

        for (int i = 0; i < CHILD_VIEW_COUNT; i++) {
            CardLayout childView = new CardLayout(getContext());
            childView.setAlpha(0F);
            parent.addView(childView);
            children.add(childView);
        }

        parent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Resources res = getResources();
                int tileMargins = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, res.getDisplayMetrics()));
                final int childWidth = parent.getWidth() / parent.getColumnCount() - (tileMargins * 2);
                final int childHeight = parent.getHeight() / parent.getRowCount() - (tileMargins * 2);

                for (int i = 0; i < parent.getChildCount(); i++) {
                    View childView = parent.getChildAt(i);
                    GridLayout.LayoutParams params = (GridLayout.LayoutParams) childView.getLayoutParams();
                    params.width = childWidth;
                    params.height = childHeight;
                    params.setMargins(tileMargins, tileMargins, tileMargins, tileMargins);
                    childView.setLayoutParams(params);
                }
                parent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        listener.onParentAndChildrenPrepared(parent, children);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (OnParentAndChildCreationListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnParentAndChildCreationListener");
        }
    }

    public interface OnParentAndChildCreationListener {
        void onParentAndChildrenPrepared(ViewGroup parent, List<View> children);
    }

}