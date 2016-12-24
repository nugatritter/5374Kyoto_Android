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
