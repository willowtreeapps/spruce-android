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

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.willowtreeapps.spruce.Spruce;
import com.willowtreeapps.spruce.animation.DefaultAnimations;
import com.willowtreeapps.spruce.sort.LinearSort;
import com.willowtreeapps.spurceexampleapp.R;

import java.util.ArrayList;
import java.util.List;


public class RecyclerFragment extends Fragment {

    public static RecyclerFragment newInstance() {
        return new RecyclerFragment();
    }

    private RecyclerAdapter adapter;
    private RecyclerView.ItemAnimator itemAnimator;

    private List<View> placeHolderList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final RecyclerView recyclerView = (RecyclerView) container.findViewById(R.id.recycler);
        final View placeholder = container.findViewById(R.id.placeholder_view);
        FloatingActionButton fab = (FloatingActionButton) container.findViewById(R.id.fab);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        for (int i = 0; i < 5; i++) {
            placeHolderList.add(placeholder);
        }
        adapter = new RecyclerAdapter(placeHolderList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(linearLayoutManager);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeHolderList.add(placeholder);
                adapter.notifyItemInserted(placeHolderList.size());
            }
        });

        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){
            @Override
            public void onGlobalLayout() {
                itemAnimator = new Spruce.SpruceBuilder(recyclerView)
                        .sortWith(new LinearSort(150, false, LinearSort.Direction.BOTTOM_TO_TOP))
                        .animateWith(DefaultAnimations.shrinkAnimator(recyclerView, 800),
                                ObjectAnimator.ofFloat(recyclerView, View.TRANSLATION_X, -recyclerView.getWidth(), 0)
                                        .setDuration(800))
                        .getItemAnimator();

                recyclerView.setItemAnimator(itemAnimator);
                recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        return inflater.inflate(R.layout.recycler_fragment, container, false);
    }

    private class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

        List<View> recyclerList;

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            View placeholderView;

            ViewHolder(View itemView) {
                super(itemView);
                placeholderView = itemView.findViewById(R.id.placeholder_view);
                placeholderView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                itemAnimator.runPendingAnimations();
            }
        }

        RecyclerAdapter(List<View> placeholderList) {
            this.recyclerList = placeholderList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_placeholder, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.placeholderView = recyclerList.get(position);
        }

        @Override
        public int getItemCount() {
            return recyclerList.size();
        }

    }
}
