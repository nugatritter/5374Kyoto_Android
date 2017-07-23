package com.kubotaku.android.code4kyoto5374.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Broadcast receiver for application updated
 */
public class UpdateReceiver extends BroadcastReceiver {

    public static final String TAG = UpdateReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "---- updated my package ----");
        AlarmService.cancelAllAlarms(context);
        AlarmService.setupAllAlarms(context);
    }
}
