package com.kubotaku.android.code4kyoto5374;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by kubotaku1119 on 2016/12/25.
 */

public abstract class BaseActivity extends AppCompatActivity implements OnPermissionRequestListener {

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

}
