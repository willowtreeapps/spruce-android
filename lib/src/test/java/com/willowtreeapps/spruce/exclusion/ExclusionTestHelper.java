package com.willowtreeapps.spruce.exclusion;

import android.view.View;
import android.view.ViewGroup;

import org.mockito.Mockito;

public class ExclusionTestHelper {
    /**
     * This function creates a view groups according to the ids passed.
     *
     * @param viewGroup parent view group
     * @param ids       id array.
     */
    static void addViews(ViewGroup viewGroup, int... ids) {
        Mockito.when(viewGroup.getChildCount()).thenReturn(ids.length);

        for (int i = 0; i < ids.length; i++) {
            View mockView = Mockito.mock(View.class);
            Mockito.when(mockView.getId()).thenReturn(ids[i]);
            Mockito.when(viewGroup.getChildAt(i)).thenReturn(mockView);
        }
    }
}
