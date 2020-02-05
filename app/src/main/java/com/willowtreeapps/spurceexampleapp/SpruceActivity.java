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

package com.willowtreeapps.spurceexampleapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.willowtreeapps.spurceexampleapp.fragments.ControlsFragment;
import com.willowtreeapps.spurceexampleapp.fragments.ViewFragment;
import com.willowtreeapps.spurceexampleapp.pager.VerticalViewPager;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


public class SpruceActivity extends AppCompatActivity
        implements ViewFragment.OnParentAndChildCreationListener {

    public ViewGroup parent;
    public List<View> children = new ArrayList<>();
    public Spinner sortDropDown;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_pager);

        FragmentManager fm = getSupportFragmentManager();

        VerticalViewPager verticalPager = findViewById(R.id.vertical_pager);
        VerticalPagerAdapter adapter = new VerticalPagerAdapter(fm);
        verticalPager.setAdapter(adapter);

        Toolbar toolBar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolBar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        sortDropDown = findViewById(R.id.sort_selection);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.sort_functions,
                R.layout.spinner_item);
        spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        sortDropDown.setAdapter(spinnerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_option:
                break;
            case R.id.recycler_option:
                startActivity(new Intent(this, RecyclerActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
                break;
            case R.id.list_view_option:
                startActivity(new Intent(this, ListViewActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onParentAndChildrenPrepared(ViewGroup parent, List<View> children) {
        this.parent = parent;
        this.children = children;
    }

    private class VerticalPagerAdapter extends FragmentStatePagerAdapter {

        VerticalPagerAdapter(FragmentManager fm) {
            super(fm, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 1:
                    return ControlsFragment.newInstance();
                default:
                    return ViewFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

    }
}
