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

import com.willowtreeapps.spurceexampleapp.fragments.RecyclerFragment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


public class RecyclerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.recycler_fragment);

        FragmentManager fm = getSupportFragmentManager();
        Fragment recyclerFragment = fm.findFragmentById(R.id.recycler_fragment);
        if (recyclerFragment == null) {
            recyclerFragment = RecyclerFragment.newInstance();
            fm.beginTransaction()
                    .replace(R.id.recycler_fragment, recyclerFragment)
                    .commit();
        }

        Toolbar toolBar = (Toolbar) findViewById(R.id.recycler_tool_bar);
        setSupportActionBar(toolBar);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.recycler_name);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
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
                startActivity(new Intent(this, SpruceActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
                break;
            case R.id.recycler_option:
                break;
            case R.id.list_view_option:
                startActivity(new Intent(this, ListViewActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
                break;
            default:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
