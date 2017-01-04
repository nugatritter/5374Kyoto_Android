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
package com.kubotaku.android.code4kyoto5374.data;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * ごみ情報クラス
 */
public class GarbageData extends RealmObject {

    /**
     * 品目名
     */
    public String name;

    /**
     * 読み
     */
    public String reading;

    /**
     * 出し方のポイント
     */
    public String note;

    /**
     * 区分
     */
    public RealmList<GarbageType> typeList;

    /**
     * ごみ情報クラスを生成する
     *
     * @param name    品目名
     * @param reading 読み
     * @param type    区分
     * @param note    ポイント
     * @return
     */
    public static GarbageData creator(String name, String reading, String type, String note) {
        final GarbageData data = new GarbageData();

        data.name = name;
        data.reading = reading;
        data.note = note;
        data.typeList = getType(type);

        return data;
    }

    /**
     * 区分情報を取得する
     *
     * @param src 元データ
     * @return
     */
    public static RealmList<GarbageType> getType(String src) {
        RealmList<GarbageType> garbageTypeList = new RealmList<>();
        String[] sepEntity = src.split(" ");
        for (String entity : sepEntity) {
            final GarbageType garbageType = GarbageType.parse(entity);
            garbageTypeList.add(garbageType);
        }
        return garbageTypeList;
    }


}
