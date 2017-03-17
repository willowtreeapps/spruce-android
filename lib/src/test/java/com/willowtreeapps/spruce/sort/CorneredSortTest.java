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

package com.willowtreeapps.spruce.sort;

import android.graphics.PointF;
import android.view.View;
import android.view.ViewGroup;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(shadows = {ShadowPointF.class})
public class CorneredSortTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private ViewGroup mockParent = Mockito.mock(ViewGroup.class);
    private List<View> mockChildren = new ArrayList<>();
    private CorneredSort corneredSort;

    @Before
    public void setup() {
        float x = 0;
        float y = 0;
        for (int i = 0; i < 3; i++) {
            View mockView = Mockito.mock(View.class);
            Mockito.when(mockView.getX()).thenReturn(x);
            Mockito.when(mockView.getY()).thenReturn(y);
            mockChildren.add(mockView);
            x++;
            y++;
        }
    }

    @Test
    public void test_get_distance_point_for_top_left() {
        corneredSort = new CorneredSort(/*interObjectDelay=*/0,
                /*reversed=*/false,
                CorneredSort.Corner.TOP_LEFT);

        PointF resultPoint = corneredSort.getDistancePoint(mockParent, mockChildren);
        assertEquals(new PointF(0f, 0f), resultPoint);
    }

    @Test
    public void test_get_distance_point_for_top_right() {
        corneredSort = new CorneredSort(/*interObjectDelay=*/0,
                /*reversed=*/false,
                CorneredSort.Corner.TOP_RIGHT);
        // fake a width of 20, this will be the expected value
        Mockito.when(mockParent.getWidth()).thenReturn(20);

        PointF resultPoint = corneredSort.getDistancePoint(mockParent, mockChildren);
        assertEquals(new PointF(20F, 0F), resultPoint);
    }

    @Test
    public void test_get_distance_point_for_bottom_left() {
        corneredSort = new CorneredSort(/*interObjectDelay=*/0,
                /*reversed=*/false,
                CorneredSort.Corner.BOTTOM_LEFT);
        // fake a height of 20, this will be the expected value
        Mockito.when(mockParent.getHeight()).thenReturn(20);

        PointF resultPoint = corneredSort.getDistancePoint(mockParent, mockChildren);
        assertEquals(new PointF(0F, 20F), resultPoint);
    }

    @Test
    public void test_get_distance_point_for_bottom_right() {
        corneredSort = new CorneredSort(/*interObjectDelay=*/0,
                /*reversed=*/false,
                CorneredSort.Corner.BOTTOM_RIGHT);
        // fake a width of 20, this will be the expected value
        Mockito.when(mockParent.getWidth()).thenReturn(20);
        // fake a height of 20, this will be the expected value
        Mockito.when(mockParent.getHeight()).thenReturn(20);

        PointF resultPoint = corneredSort.getDistancePoint(mockParent, mockChildren);
        PointF expectedPoint = new PointF(20F, 20F);
        assertEquals(expectedPoint, resultPoint);
    }

    @Test
    public void test_corner_sort_constructor_throws_illegal_argument_exception_for_invalid_corner() throws IllegalArgumentException {
        expectedException.expect(IllegalArgumentException.class);
        corneredSort = new CorneredSort(/*interObjectDelay=*/0,
                /*reversed=*/false,
                CorneredSort.Corner.valueOf("invalid"));
    }

    @Test
    public void test_corner_sort_constructor_throws_npe_for_null_corner() throws NullPointerException {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("Corner can't be null and must be a valid type");
        corneredSort = new CorneredSort(/*interObjectDelay=*/0,
                /*reversed=*/false,
                null);
    }

    @Test
    public void test_positive_inter_object_delay() {
        List<SpruceTimedView> resultViews = new CorneredSort(/*interObjectDelay=*/1,
                /*reversed=*/false,
                CorneredSort.Corner.TOP_LEFT)
                .getViewListWithTimeOffsets(mockParent, mockChildren);
        assertEquals(0, resultViews.get(0).getTimeOffset());
        assertEquals(1, resultViews.get(1).getTimeOffset());
        assertEquals(2, resultViews.get(2).getTimeOffset());
    }

    @Test
    public void test_inter_object_delay_of_zero() {
        List<SpruceTimedView> resultViews = new CorneredSort(/*interObjectDelay=*/0,
                /*reversed=*/false,
                CorneredSort.Corner.TOP_LEFT)
                .getViewListWithTimeOffsets(mockParent, mockChildren);
        assertEquals(0, resultViews.get(0).getTimeOffset());
        assertEquals(0, resultViews.get(1).getTimeOffset());
        assertEquals(0, resultViews.get(2).getTimeOffset());
    }

    @Test
    public void test_negative_inter_object_delay() {
        List<SpruceTimedView> resultViews = new CorneredSort(/*interObjectDelay=*/-1,
                /*reversed=*/false,
                CorneredSort.Corner.TOP_LEFT)
                .getViewListWithTimeOffsets(mockParent, mockChildren);
        assertEquals(0, resultViews.get(0).getTimeOffset());
        assertEquals(-1, resultViews.get(1).getTimeOffset());
        assertEquals(-2, resultViews.get(2).getTimeOffset());
    }

}
