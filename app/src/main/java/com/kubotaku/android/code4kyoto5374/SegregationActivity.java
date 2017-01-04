/**
 * Copyright 2017 kubotaku1119 <kubotaku1119@gmail.com>
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kubotaku.android.code4kyoto5374;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.kubotaku.android.code4kyoto5374.fragments.SegregationFragment;

/**
 * ごみの分別判定用Activity
 */
public class SegregationActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segragation);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setupViews();
    }

    private void setupViews() {
        showSegregationView();
    }

    private void showSegregationView() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentByTag(SegregationFragment.TAG) == null) {
            SegregationFragment fragment = SegregationFragment.newInstance();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.view_holder, fragment, SegregationFragment.TAG);
            ft.commitAllowingStateLoss();
        }
    }
}
