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
                String reading = sepForElements[1];
                String type = sepForElements[2];
                String note = "";
                if (sepForElements.length >= 4) {
                    note = sepForElements[3];
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
