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

package com.willowtreeapps.spruce.exclusion;

import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class takes care of the exclusion functionality
 */
public class ExclusionHelper {

    private final HashMap<Integer, Boolean> exclusionMap = new HashMap<>();
    private int mode;
    public static final int R_L_MODE = 0;
    public static final int NORMAL_MODE = 1;

    /**
     * This method is used to initialize the exclusion module.
     *
     * @param exclusionList list of items to be excluded.
     * @param mode          mode in which the exclusion works.
     */
    public void initialize(List<Integer> exclusionList, int mode) {
        /*
         * making hash-map from the given list.
         * This is to cut down the complexity in the future.
         */
        this.mode = mode;
        for (Integer id : exclusionList) {
            exclusionMap.put(id, true);
        }
    }

    /**
     * isToBeIncluded function works in 2 modes,
     * 1. ID mode : exclude if the ID is present.
     * 2. Index mode: exclude a specific index.
     *
     * @param view  the view to be checked for exclusion.
     * @param index index of the view/
     * @return true of the view is to be included else excluded.
     */
    public boolean isToBeIncluded(View view, int index) {
        if (mode == NORMAL_MODE) {
            return view != null && !exclusionMap.containsKey(view.getId());
        } else if (mode == R_L_MODE) {
            return view != null && !exclusionMap.containsKey(index);
        }
        return false;
    }

    /**
     * filterViews function will filter out the {@link HashMap} exclusionMap from the parent view group.
     *
     * @param viewGroup parent view group
     * @return returns filtered list of type view
     */
    public List<View> filterViews(ViewGroup viewGroup) {
        List<View> children = new ArrayList<>();

        /*
         * The reason why we use hash map for the comparison is because
         * HashMap has complexity of O(1) for insertion and lookup.
         * this will keep the overall complexity as O(n).
         */
        for (int index = 0; index < viewGroup.getChildCount(); index++) {
            View view = viewGroup.getChildAt(index);
            if (isToBeIncluded(view, index))
                children.add(view);
        }
        return children;
    }
}
