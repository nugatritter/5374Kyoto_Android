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

import io.realm.RealmObject;

/**
 * 区域マスター情報クラス
 */
public class AreaMaster extends RealmObject {

    /**
     * マスターエリアID
     */
    public int areaID;

    /**
     * マスターエリア名
     */
    public String areaName;

    /**
     * 区域マスター情報クラスを生成する
     *
     * @param src 生成元情報
     * @return
     */
    public static AreaMaster create(String src) {
        final AreaMaster areaMaster = new AreaMaster();

        String[] sepEntity = src.split(",");
        areaMaster.areaID = Integer.parseInt(sepEntity[0]);
        areaMaster.areaName = sepEntity[1];

        return areaMaster;
    }
}
