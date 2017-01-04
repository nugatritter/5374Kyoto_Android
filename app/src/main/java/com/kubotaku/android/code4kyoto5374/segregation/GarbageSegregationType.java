package com.kubotaku.android.code4kyoto5374.segregation;

import com.kubotaku.android.code4kyoto5374.data.GarbageType;

/**
 * Created by kubotaku1119 on 2017/01/04.
 */

public class GarbageSegregationType {

    public int type;

    public static GarbageSegregationType newInstance(GarbageType src) {
        GarbageSegregationType instance = new GarbageSegregationType();
        instance.type = src.type;
        return instance;
    }
}
