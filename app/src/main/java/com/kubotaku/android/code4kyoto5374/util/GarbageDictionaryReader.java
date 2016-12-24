package com.kubotaku.android.code4kyoto5374.util;

import android.content.Context;

import com.kubotaku.android.code4kyoto5374.data.GarbageData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kubotaku1119 on 2016/12/24.
 */

public class GarbageDictionaryReader {


    public List<GarbageData> importDictionary(Context context) {
        List<GarbageData> garbageDataList = new ArrayList<>();

        try {
            String dictionarySrc = AppUtil.readFileFromAssets(context, "garbage_dictionary.csv");

            String[] sepForLine = dictionarySrc.split("\n");

            for (String line : sepForLine) {
                String[] sepForElements = line.split(",");

                String name = sepForElements[1];
                String reading = sepForElements[2];
                String type = sepForElements[3];
                String note = sepForElements[4];

                GarbageData garbageData = GarbageData.creator(name, reading, type, note);
                garbageDataList.add(garbageData);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return garbageDataList;
    }
}
