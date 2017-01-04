package com.kubotaku.android.code4kyoto5374.segregation;

import com.kubotaku.android.code4kyoto5374.data.GarbageData;
import com.kubotaku.android.code4kyoto5374.data.GarbageType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kubotaku1119 on 2017/01/04.
 */

public class GarbageSegregationData {

    public String name;

    public String note;

    public List<GarbageSegregationType> typeList;


    /**
     * 指定indexの区分を返す。
     * <p>
     * 指定indexにデータが無い場合はnullを返す
     * </p>
     *
     * @param position index.
     * @return 区分
     */
    public GarbageSegregationType getType(int position) {
        if (position < typeList.size()) {
            return typeList.get(position);
        }
        return null;
    }

    public static GarbageSegregationData newInstance(GarbageData src) {
        GarbageSegregationData instance = new GarbageSegregationData();
        instance.name = src.name;
        instance.note = src.note;
        instance.typeList = new ArrayList<>();
        for (GarbageType type : src.typeList) {
            instance.typeList.add(GarbageSegregationType.newInstance(type));
        }
        return instance;
    }

}
