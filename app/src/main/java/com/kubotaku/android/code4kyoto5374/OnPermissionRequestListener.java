package com.kubotaku.android.code4kyoto5374;

import android.support.v4.app.Fragment;

/**
 * Created by kubotaku1119 on 2016/12/25.
 */

public interface OnPermissionRequestListener {

    void requestPermission(Fragment fragment, String[] permissions, int requestCode);

}
