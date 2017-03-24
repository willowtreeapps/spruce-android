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
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.willowtreeapps.spruce.Spruce;
import com.willowtreeapps.spruce.animation.DefaultAnimations;
import com.willowtreeapps.spruce.sort.DefaultSort;
import com.willowtreeapps.spurceexampleapp.R;

import java.util.ArrayList;
import java.util.List;


public class RecyclerFragment extends Fragment {

    public static RecyclerFragment newInstance() {
        return new RecyclerFragment();
    }

    private RecyclerView recyclerView;
    private Animator spruceAnimator;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView) container.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);

        RelativeLayout placeholder = (RelativeLayout) container.findViewById(R.id.placeholder_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                super.onLayoutChildren(recycler, state);
                // Animate in the visible children
                spruceAnimator = new Spruce.SpruceBuilder(recyclerView)
                    .sortWith(new DefaultSort(100))
                    .animateWith(DefaultAnimations.shrinkAnimator(recyclerView, 800),
                            ObjectAnimator.ofFloat(recyclerView, "translationX", -recyclerView.getWidth(), 0f).setDuration(800))
                    .start();

            }
        };

        List<RelativeLayout> placeHolderList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            placeHolderList.add(placeholder);
        }

        recyclerView.setAdapter(new RecyclerAdapter(placeHolderList));
        recyclerView.setLayoutManager(linearLayoutManager);

        return inflater.inflate(R.layout.recycler_fragment, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (spruceAnimator != null) {
            spruceAnimator.start();
        }
    }

    private class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

        List<RelativeLayout> placeholderList;

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            RelativeLayout placeholderView;

            ViewHolder(View itemView) {
                super(itemView);
                placeholderView = (RelativeLayout) itemView.findViewById(R.id.placeholder_view);
                placeholderView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                if (spruceAnimator != null) {
                    spruceAnimator.start();
                }
            }
        }

        RecyclerAdapter(List<RelativeLayout> placeholderList) {
            this.placeholderList = placeholderList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RelativeLayout view = (RelativeLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_placeholder, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.placeholderView = placeholderList.get(position);
        }

        @Override
        public int getItemCount() {
            return placeholderList.size();
        }

    }
}
