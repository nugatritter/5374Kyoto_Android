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

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.kubotaku.android.code4kyoto5374.fragments.OnFragmentTaskListener;

/**
 * Common abstract activity class.
 */

public abstract class BaseActivity extends AppCompatActivity
        implements OnPermissionRequestListener, OnFragmentTaskListener {

    // --------------------------------------

    private int requestedCode;

    private Fragment requestedFragment;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == this.requestedCode) {
            if (requestedFragment instanceof OnRequestPermissionResultListener) {
                ((OnRequestPermissionResultListener) requestedFragment).onPermissionRequested(permissions, grantResults);
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void requestPermission(Fragment fragment, String[] permissions, int requestCode) {
        this.requestedFragment = fragment;
        this.requestedCode = requestCode;
        ActivityCompat.requestPermissions(
                this,
                permissions,
                requestCode);
    }

    // -----------------------------


    @Override
    public void onTaskStart() {
        enabledProgress(true);
    }

    @Override
    public void onTaskStop() {
        enabledProgress(false);
    }

    protected void enabledProgress(boolean enabled) {
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress);
        if (enabled) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}
