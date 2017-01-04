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
package com.kubotaku.android.code4kyoto5374.segregation;

import com.kubotaku.android.code4kyoto5374.data.GarbageData;
import com.kubotaku.android.code4kyoto5374.data.GarbageType;

import java.util.ArrayList;
import java.util.List;

/**
 * ごみデータクラス（分別表示画面用）.
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
