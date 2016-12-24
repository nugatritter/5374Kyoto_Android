package com.kubotaku.android.code4kyoto5374.data;

import io.realm.RealmObject;

/**
 * 区域マスター情報クラス
 */
public class AreaMaster extends RealmObject {

    public int areaID;

    public String areaName;

    public static AreaMaster create(String src) {
        final AreaMaster areaMaster = new AreaMaster();

        String[] sepEntity = src.split(",");
        areaMaster.areaID = Integer.parseInt(sepEntity[0]);
        areaMaster.areaName = sepEntity[1];

        return areaMaster;
    }
}
