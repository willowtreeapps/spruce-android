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
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class DefaultSortTest {

    @Test
    public void test_inter_object_delay_of_views_is_set_on_default_sort() {
        testTimedViews(/*interObjectDelay=*/1);
    }

    @Test
    public void test_inter_object_delay_of_zero() {
        testTimedViews(/*interObjectDelay=*/0);
    }

    @Test
    public void test_negative_inter_object_delay() {
        testTimedViews(/*interObjectDelay=*/-1);
    }

    private void testTimedViews(long interObjectDelay) {
        ViewGroup mockParent = Mockito.mock(ViewGroup.class);
        List<View> mockChildren = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            mockChildren.add(Mockito.mock(View.class));
        }
        long comparisonValue = 0;
        List<SpruceTimedView> resultViews = new DefaultSort(interObjectDelay).getViewListWithTimeOffsets(mockParent, mockChildren);
        for (SpruceTimedView resultView : resultViews) {
            Assert.assertEquals(comparisonValue, resultView.getTimeOffset());
            comparisonValue += interObjectDelay;
        }
    }

}
