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


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;


@RunWith(RobolectricTestRunner.class)
public class ExclusionHelperTest {

    private ViewGroup mockParent;

    @Before
    public void setup() {
        mockParent = Mockito.mock(ViewGroup.class);
    }

    @Test
    public void test_normal_exclusion_with_empty_list() {
        ExclusionHelper helper = new ExclusionHelper();
        List<Integer> idList = new ArrayList<>();
        helper.initialize(idList, ExclusionHelper.NORMAL_MODE);
        Assert.assertEquals(helper.filterViews(mockParent).size(), idList.size());
        Assert.assertNotEquals(helper.filterViews(mockParent).size(), 1);

    }

    @Test
    public void test_r_l_exclusion_with_empty_list() {
        ExclusionHelper helper = new ExclusionHelper();
        List<Integer> idList = new ArrayList<>();
        helper.initialize(idList, ExclusionHelper.R_L_MODE);
        Assert.assertEquals(helper.filterViews(mockParent).size(), idList.size());
        Assert.assertNotEquals(helper.filterViews(mockParent).size(), 1);

    }


    @Test
    public void test_normal_exclusion() {
        ExclusionHelper helper = new ExclusionHelper();
        List<Integer> idList = new ArrayList<>();
        idList.add(2);
        idList.add(7);
        helper.initialize(idList, ExclusionHelper.NORMAL_MODE);
        ExclusionTestHelper.addViews(mockParent, 1, 2, 5, 7, 9);
        Assert.assertNotEquals(helper.filterViews(mockParent).size(), idList.size());
        Assert.assertEquals(helper.filterViews(mockParent).size(), idList.size() + 1);

        for (View view : helper.filterViews(mockParent)) {
            Assert.assertFalse(idList.contains(view.getId()));
        }
    }

    @Test
    public void test_r_l_exclusion() {
        ExclusionHelper helper = new ExclusionHelper();
        List<Integer> idList = new ArrayList<>();
        idList.add(5);
        idList.add(7);

        List<Integer> positionList = new ArrayList<>();
        positionList.add(2);
        positionList.add(3);


        helper.initialize(positionList, ExclusionHelper.R_L_MODE);
        ExclusionTestHelper.addViews(mockParent, 1, 2, 5, 7, 9);
        Assert.assertNotEquals(helper.filterViews(mockParent).size(), idList.size());
        Assert.assertEquals(helper.filterViews(mockParent).size(), idList.size() + 1);

        for (View view : helper.filterViews(mockParent)) {
            Assert.assertFalse(idList.contains(view.getId()));
        }
    }
}
