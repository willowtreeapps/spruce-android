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

import android.view.View;
import android.view.ViewGroup;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

@RunWith(RobolectricTestRunner.class)
public class ContinuousWeightedSortTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private ViewGroup mockParent = Mockito.mock(ViewGroup.class);
    private List<View> mockChildren;

    @Before
    public void setup() {
        mockChildren = TestHelper.setupMockChildren();
    }

    @Test
    public void test_positive_inter_object_delay() {
        List<SpruceTimedView> resultViews = new ContinuousWeightedSort(/*interObjectDelay=*/1,
                /*reversed=*/false,
                RadialSort.Position.TOP_LEFT,
                ContinuousWeightedSort.MEDIUM_WEIGHT,
                ContinuousWeightedSort.MEDIUM_WEIGHT)
                .getViewListWithTimeOffsets(mockParent, mockChildren);
        Assert.assertEquals(0, resultViews.get(0).getTimeOffset());
        Assert.assertEquals(1, resultViews.get(1).getTimeOffset());
        Assert.assertEquals(2, resultViews.get(2).getTimeOffset());
    }

    @Test
    public void test_positive_inter_object_delay_with_reversed() {
        List<SpruceTimedView> resultViews = new ContinuousWeightedSort(/*interObjectDelay=*/1,
                /*reversed=*/true,
                RadialSort.Position.TOP_LEFT,
                ContinuousWeightedSort.MEDIUM_WEIGHT,
                ContinuousWeightedSort.MEDIUM_WEIGHT)
                .getViewListWithTimeOffsets(mockParent, mockChildren);
        Assert.assertEquals(1, resultViews.get(0).getTimeOffset());
        Assert.assertEquals(1, resultViews.get(1).getTimeOffset());
        Assert.assertEquals(0, resultViews.get(2).getTimeOffset());
    }

    @Test
    public void test_inter_object_delay_of_zero() {
        List<SpruceTimedView> resultViews = new ContinuousWeightedSort(/*interObjectDelay=*/0,
                /*reversed=*/false,
                RadialSort.Position.TOP_LEFT,
                ContinuousWeightedSort.MEDIUM_WEIGHT,
                ContinuousWeightedSort.MEDIUM_WEIGHT)
                .getViewListWithTimeOffsets(mockParent, mockChildren);
        Assert.assertEquals(0, resultViews.get(0).getTimeOffset());
        Assert.assertEquals(0, resultViews.get(1).getTimeOffset());
        Assert.assertEquals(0, resultViews.get(2).getTimeOffset());
    }

    @Test
    public void test_negative_inter_object_delay() {
        List<SpruceTimedView> resultViews = new ContinuousWeightedSort(/*interObjectDelay=*/-1,
                /*reversed=*/false,
                RadialSort.Position.TOP_LEFT,
                ContinuousWeightedSort.MEDIUM_WEIGHT,
                ContinuousWeightedSort.MEDIUM_WEIGHT)
                .getViewListWithTimeOffsets(mockParent, mockChildren);
        Assert.assertEquals(0, resultViews.get(0).getTimeOffset());
        Assert.assertEquals(-1, resultViews.get(1).getTimeOffset());
        Assert.assertEquals(-2, resultViews.get(2).getTimeOffset());
    }

    @Test
    public void test_negative_inter_object_delay_with_reversed() {
        List<SpruceTimedView> resultViews = new ContinuousWeightedSort(/*interObjectDelay=*/-1,
                /*reversed=*/true,
                RadialSort.Position.TOP_LEFT,
                ContinuousWeightedSort.MEDIUM_WEIGHT,
                ContinuousWeightedSort.MEDIUM_WEIGHT)
                .getViewListWithTimeOffsets(mockParent, mockChildren);
        Assert.assertEquals(-1, resultViews.get(0).getTimeOffset());
        Assert.assertEquals(-1, resultViews.get(1).getTimeOffset());
        Assert.assertEquals(0, resultViews.get(2).getTimeOffset());
    }

    @Test
    public void test_constructor_exception_for_negative_horizontal_weight() throws AssertionError {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage("horizontalWeight can not be a negative value");
        int horizontalWeight = 0;
        new ContinuousWeightedSort(/*interObjectDelay=*/-1,
                /*reversed=*/false,
                RadialSort.Position.TOP_LEFT,
                horizontalWeight-1,
                ContinuousWeightedSort.MEDIUM_WEIGHT);
    }

    @Test
    public void test_constructor_exception_for_negative_vertical_weight() throws AssertionError {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage("verticalWeight can not be a negative value");
        int verticalWeight = 0;
        new ContinuousWeightedSort(/*interObjectDelay=*/-1,
                /*reversed=*/false,
                RadialSort.Position.TOP_LEFT,
                ContinuousWeightedSort.MEDIUM_WEIGHT,
                verticalWeight-1);
    }

    @Test
    public void test_calculate_max_distance() {
        ContinuousWeightedSort continuousWeightedSort = new ContinuousWeightedSort(/*interObjectDelay=*/0,
                /*reversed=*/false,
                RadialSort.Position.TOP_LEFT,
                ContinuousWeightedSort.MEDIUM_WEIGHT,
                ContinuousWeightedSort.MEDIUM_WEIGHT);
        double max = 0;
        max = continuousWeightedSort.calculateMaxDistance(/*left=*/5, /*right*/4, max);
        Assert.assertEquals(5, max, 0);
    }

    @Test
    public void test_calculate_max_distance_with_greater_right_value() {
        ContinuousWeightedSort continuousWeightedSort = new ContinuousWeightedSort(/*interObjectDelay=*/0,
                /*reversed=*/false,
                RadialSort.Position.TOP_LEFT,
                ContinuousWeightedSort.MEDIUM_WEIGHT,
                ContinuousWeightedSort.MEDIUM_WEIGHT);
        double max = 0;
        max = continuousWeightedSort.calculateMaxDistance(/*left=*/5, /*right*/6, max);
        Assert.assertEquals(6, max, 0);
    }

    @Test
    public void test_calculate_max_distance_with_equal_left_and_right_values() {
        ContinuousWeightedSort continuousWeightedSort = new ContinuousWeightedSort(/*interObjectDelay=*/0,
                /*reversed=*/false,
                RadialSort.Position.TOP_LEFT,
                ContinuousWeightedSort.MEDIUM_WEIGHT,
                ContinuousWeightedSort.MEDIUM_WEIGHT);
        double max = 0;
        max = continuousWeightedSort.calculateMaxDistance(/*left=*/1, /*right*/1, max);
        Assert.assertEquals(0, max, 0);
    }

}
