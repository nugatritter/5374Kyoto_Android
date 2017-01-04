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

import com.kubotaku.android.code4kyoto5374.data.GarbageType;

/**
 * ごみタイプクラス（分別表示画面用）
 */

public class GarbageSegregationType {

    public int type;

    public static GarbageSegregationType newInstance(GarbageType src) {
        GarbageSegregationType instance = new GarbageSegregationType();
        instance.type = src.type;
        return instance;
    }
}
