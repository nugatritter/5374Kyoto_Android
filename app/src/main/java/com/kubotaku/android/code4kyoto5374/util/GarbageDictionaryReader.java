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
package com.kubotaku.android.code4kyoto5374.util;

import android.content.Context;

import com.kubotaku.android.code4kyoto5374.data.GarbageData;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * ごみ分別辞典読み込みクラス
 */

public class GarbageDictionaryReader {

    private List<GarbageData> garbageDataList;

    public boolean importDictionary(Context context) {
        boolean ret = true;

        try {
            String dictionarySrc = AppUtil.readFileFromAssets(context, "garbage_dictionary.csv");

            String[] sepForLine = dictionarySrc.split("\n");

            garbageDataList = new ArrayList<>();

            for (int index = 1; index < sepForLine.length; index++) { // 1行目はヘッダー
                String[] sepForElements = sepForLine[index].split(",");

                String name = sepForElements[0];
                String reading = sepForElements[2];
                String type = sepForElements[3];
                String note = "";
                if (sepForElements.length >= 5) {
                    note = sepForElements[4];
                }

                GarbageData garbageData = GarbageData.creator(name, reading, type, note);
                garbageDataList.add(garbageData);
            }

        } catch (Exception e) {
            e.printStackTrace();
            ret = false;
        }

        return ret;
    }

    public boolean saveGarbageDictionary() {
        boolean ret = true;

        final Realm realm = Realm.getDefaultInstance();

        try {
            realm.beginTransaction();
            realm.copyToRealm(garbageDataList);
            realm.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            ret = false;
        } finally {
            realm.close();
        }

        return ret;
    }
}
