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

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(shadows = {ShadowPointF.class})
public class RadialSortTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private ViewGroup mockParent = Mockito.mock(ViewGroup.class);
    private List<View> mockChildren = new ArrayList<>();
    private RadialSort radialSort;

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
        radialSort = new RadialSort(/*interObjectDelay=*/0,
                /*reversed=*/false,
                RadialSort.Position.TOP_LEFT);

        PointF resultPoint = radialSort.getDistancePoint(mockParent, mockChildren);
        assertEquals(new PointF(0f, 0f), resultPoint);
    }

    @Test
    public void test_get_distance_point_for_top_middle() {
        radialSort = new RadialSort(/*interObjectDelay=*/0,
                /*reversed=*/false,
                RadialSort.Position.TOP_MIDDLE);
        // fake a width of 20, this will be double the expected value
        Mockito.when(mockParent.getWidth()).thenReturn(20);
        // distance view to point calculation before it gets to DistancedSort.translate() will be half the width of the parent
        setupMockChildrenForPoint(/*widthOfParent=*/20 / 2, 0);

        PointF resultPoint = radialSort.getDistancePoint(mockParent, mockChildren);
        assertEquals(new PointF(10F, 0), resultPoint);
    }

    @Test
    public void test_get_distance_point_for_top_right() {
        radialSort = new RadialSort(/*interObjectDelay=*/0,
                /*reversed=*/false,
                RadialSort.Position.TOP_RIGHT);
        // fake a width of 20, this will be the expected value
        Mockito.when(mockParent.getWidth()).thenReturn(20);
        // distance view to point calculation before it gets to DistancedSort.translate(). The point's x will be the parent's width
        setupMockChildrenForPoint(/*widthOfParent=*/20, 0);

        PointF resultPoint = radialSort.getDistancePoint(mockParent, mockChildren);
        assertEquals(new PointF(20F, 0), resultPoint);
    }

    @Test
    public void test_get_distance_point_for_left() {
        radialSort = new RadialSort(/*interObjectDelay=*/0,
                /*reversed=*/false,
                RadialSort.Position.LEFT);
        // fake a height of 20, this will be double the expected value
        Mockito.when(mockParent.getHeight()).thenReturn(20);
        // distance view to point calculation before it gets to DistancedSort.translate(). The point's y will be the parent's height divided by 2
        setupMockChildrenForPoint(0, /*heightOfParent=*/20 / 2);

        PointF resultPoint = radialSort.getDistancePoint(mockParent, mockChildren);
        assertEquals(new PointF(0, 10F), resultPoint);
    }

    @Test
    public void test_get_distance_point_for_middle() {
        radialSort = new RadialSort(/*interObjectDelay=*/0,
                /*reversed=*/false,
                RadialSort.Position.MIDDLE);
        // fake a width and height of 20, this will be double the expected value
        Mockito.when(mockParent.getHeight()).thenReturn(20);
        Mockito.when(mockParent.getWidth()).thenReturn(20);
        // distance view to point calculation before it gets to DistancedSort.translate() will be the parent's height and width divided by 2
        setupMockChildrenForPoint(/*widthOfParent=*/20 / 2, /*heightOfParent=*/20 / 2);

        PointF resultPoint = radialSort.getDistancePoint(mockParent, mockChildren);
        assertEquals(new PointF(10F, 10F), resultPoint);
    }

    @Test
    public void test_get_distance_point_for_right() {
        radialSort = new RadialSort(/*interObjectDelay=*/0,
                /*reversed=*/false,
                RadialSort.Position.RIGHT);
        // fake a width and height of 20, the height will be double the expected value and the width will be the expected value
        Mockito.when(mockParent.getHeight()).thenReturn(20);
        Mockito.when(mockParent.getWidth()).thenReturn(20);
        // distance view to point calculation before it gets to DistancedSort.translate() will be the parent's height and width divided by 2
        setupMockChildrenForPoint(/*widthOfParent=*/20, /*heightOfParent=*/20 / 2);

        PointF resultPoint = radialSort.getDistancePoint(mockParent, mockChildren);
        assertEquals(new PointF(20F, 10F), resultPoint);
    }

    @Test
    public void test_get_distance_point_for_bottom_left() {
        radialSort = new RadialSort(/*interObjectDelay=*/0,
                /*reversed=*/false,
                RadialSort.Position.BOTTOM_LEFT);
        // fake a height of 20, this will be the expected value
        Mockito.when(mockParent.getHeight()).thenReturn(20);
        // distance view to point calculation before it gets to DistancedSort.translate(). The return will be the parent's height for the points y
        setupMockChildrenForPoint(0, /*heightOfParent=*/20);

        PointF resultPoint = radialSort.getDistancePoint(mockParent, mockChildren);
        assertEquals(new PointF(0F, 20F), resultPoint);
    }

    @Test
    public void test_get_distance_point_for_bottom_middle() {
        radialSort = new RadialSort(/*interObjectDelay=*/0,
                /*reversed=*/false,
                RadialSort.Position.BOTTOM_MIDDLE);
        // fake a height of 20, this will be the expected value
        Mockito.when(mockParent.getHeight()).thenReturn(20);
        Mockito.when(mockParent.getWidth()).thenReturn(20);
        // distance view to point calculation before it gets to DistancedSort.translate(). The return will be the parent's height for the point's y
        // and half the parent's width for the point's x
        setupMockChildrenForPoint(/*widthOfParent=*/20 / 2, /*heightOfParent=*/20);

        PointF resultPoint = radialSort.getDistancePoint(mockParent, mockChildren);
        assertEquals(new PointF(10F, 20F), resultPoint);
    }

    @Test
    public void test_get_distance_point_for_bottom_right() {
        radialSort = new RadialSort(/*interObjectDelay=*/0,
                /*reversed=*/false,
                RadialSort.Position.BOTTOM_RIGHT);
        // fake a width of 20, this will be the expected value
        Mockito.when(mockParent.getWidth()).thenReturn(20);
        // fake a height of 20, this will be the expected value
        Mockito.when(mockParent.getHeight()).thenReturn(20);
        setupMockChildrenForPoint(/*widthOfParent=*/20, /*heightOfParent=*/20);

        PointF resultPoint = radialSort.getDistancePoint(mockParent, mockChildren);
        assertEquals(new PointF(20F, 20F), resultPoint);
    }

    @Test
    public void test_radial_sort_constructor_throws_illegal_argument_exception_for_invalid_position() throws IllegalArgumentException {
        expectedException.expect(IllegalArgumentException.class);
        radialSort = new RadialSort(/*interObjectDelay=*/0,
                /*reversed=*/false,
                RadialSort.Position.valueOf("invalid"));
    }

    @Test
    public void test_radial_sort_constructor_throws_npe_for_null_position() throws NullPointerException {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("Position can't be null and must be a valid type");
        radialSort = new RadialSort(/*interObjectDelay=*/0,
                /*reversed=*/false,
                null);
    }

    @Test
    public void test_positive_inter_object_delay() {
        List<SpruceTimedView> resultViews = new RadialSort(/*interObjectDelay=*/1,
                /*reversed=*/false,
                RadialSort.Position.TOP_LEFT)
                .getViewListWithTimeOffsets(mockParent, mockChildren);
        assertEquals(0, resultViews.get(0).getTimeOffset());
        assertEquals(1, resultViews.get(1).getTimeOffset());
        assertEquals(2, resultViews.get(2).getTimeOffset());
    }

    @Test
    public void test_inter_object_delay_of_zero() {
        List<SpruceTimedView> resultViews = new RadialSort(/*interObjectDelay=*/0,
                /*reversed=*/true,
                RadialSort.Position.TOP_RIGHT)
                .getViewListWithTimeOffsets(mockParent, mockChildren);
        assertEquals(0, resultViews.get(0).getTimeOffset());
        assertEquals(0, resultViews.get(1).getTimeOffset());
        assertEquals(0, resultViews.get(2).getTimeOffset());
    }

    @Test
    public void test_negative_inter_object_delay() {
        List<SpruceTimedView> resultViews = new RadialSort(/*interObjectDelay=*/-1,
                /*reversed=*/false,
                RadialSort.Position.TOP_MIDDLE)
                .getViewListWithTimeOffsets(mockParent, mockChildren);
        assertEquals(0, resultViews.get(0).getTimeOffset());
        assertEquals(-1, resultViews.get(1).getTimeOffset());
        assertEquals(-2, resultViews.get(2).getTimeOffset());
    }

    private void setupMockChildrenForPoint(float xReturn, float yReturn) {
        for (int i = 0; i < 3; i++) {
            View mockView = Mockito.mock(View.class);
            Mockito.when(mockView.getX()).thenReturn(xReturn);
            Mockito.when(mockView.getY()).thenReturn(yReturn);
            mockChildren.add(mockView);
        }
    }

}
