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

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
public class DistancedSortTest {

    @Test
    public void test_positive_inter_object_delay() {
        testTimedViews(/*interObjectDelay=*/1, /*reversed=*/false);
    }

    @Test
    public void test_inter_object_delay_of_zero() {
        testTimedViews(/*interObjectDelay=*/0, /*reversed=*/false);
    }

    @Test
    public void test_negative_inter_object_delay() {
        testTimedViews(/*interObjectDelay=*/-1, /*reversed=*/false);
    }

    private void testTimedViews(long interObjectDelay, boolean reversed) {
        ViewGroup mockParent = Mockito.mock(ViewGroup.class);
        List<View> mockChildren = new ArrayList<>();
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
        long comparisonValue = 0;
        List<SpruceTimedView> resultViews = new DistancedSort(interObjectDelay, reversed).getViewListWithTimeOffsets(mockParent, mockChildren);
        for (SpruceTimedView resultView : resultViews) {
            Assert.assertEquals(comparisonValue, resultView.getTimeOffset());
            comparisonValue += interObjectDelay;
        }
    }

    @Test
    public void test_get_distance_point_returns_a_point() {
        ViewGroup mockParent = Mockito.mock(ViewGroup.class);
        List<View> mockChildren = new ArrayList<>();
        mockChildren.add(Mockito.mock(View.class));

        DistancedSort distancedSort = new DistancedSort(/*interObjectDelay=*/0, /*reversed=*/false);
        Assert.assertThat(distancedSort.getDistancePoint(mockParent, mockChildren), CoreMatchers.instanceOf(PointF.class));
    }

    @Test
    public void test_translate_returns_a_point() {
        List<View> mockChildren = new ArrayList<>();
        mockChildren.add(Mockito.mock(View.class));

        DistancedSort distancedSort = new DistancedSort(/*interObjectDelay=*/0, /*reversed=*/false);
        Assert.assertThat(distancedSort.translate(new PointF(0, 0), mockChildren), CoreMatchers.instanceOf(PointF.class));
    }

    @Test
    public void test_get_distance_between_points_returns_a_double() {
        DistancedSort distancedSort = new DistancedSort(/*interObjectDelay=*/0, /*reversed=*/false);
        PointF firstPoint = Mockito.mock(PointF.class);
        PointF secondPoint = Mockito.mock(PointF.class);

        Assert.assertThat(distancedSort.getDistanceBetweenPoints(firstPoint, secondPoint), CoreMatchers.instanceOf(Double.class));
    }

    @Test
    public void test_get_view_list_with_time_offsets_returns_correct_number_of_children() {
        ViewGroup mockParent = Mockito.mock(ViewGroup.class);
        List<View> mockChildren = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            mockChildren.add(Mockito.mock(View.class));
        }
        List<SpruceTimedView> resultViews = new DistancedSort(/*interObjectDelay=*/0, /*reversed=*/false).getViewListWithTimeOffsets(mockParent, mockChildren);
        Assert.assertEquals(mockChildren.size(), resultViews.size());
    }

}