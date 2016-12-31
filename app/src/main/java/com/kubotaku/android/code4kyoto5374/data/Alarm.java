package com.kubotaku.android.code4kyoto5374.data;

import java.util.Locale;

/**
 * Created by kubotaku1119 on 2016/12/28.
 */

public class Alarm {

    public int hour;

    public int minute;

    public boolean enable;

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "%1$02d:%2$02d", hour, minute);
    }
}
