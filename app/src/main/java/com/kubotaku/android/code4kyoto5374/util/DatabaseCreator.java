package com.kubotaku.android.code4kyoto5374.util;

import android.content.Context;

import com.kubotaku.android.code4kyoto5374.data.AreaDays;
import com.kubotaku.android.code4kyoto5374.data.AreaMaster;
import com.kubotaku.android.code4kyoto5374.data.GarbageData;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * ごみの収集日と分別情報のデータベースを作成する
 */

public class DatabaseCreator {

    private Context context;

    private Realm realm;

    /**
     * コンストラクタ
     *
     * @param context コンテキスト
     */
    public DatabaseCreator(final Context context) {
        this.context = context;
    }

    /**
     * データベースを生成する.
     *
     * @return 成否
     */
    public boolean createDatabase() {
        return createDatabaseIfNeeded();
    }

    /**
     * データベースを生成する.
     * <p>
     * データが保存されていない場合
     * </p>
     *
     * @return 成否
     */
    private boolean createDatabaseIfNeeded() {
        boolean ret = true;

        realm = Realm.getDefaultInstance();

        if (isExistDatabase()) {
            return ret;
        }

        try {
            // ごみ収集日情報の取得と保存
            final AreaDataReader areaDataReader = new AreaDataReader();
            if (areaDataReader.importAreaData()) {
                if (!areaDataReader.saveAreaData()) {
                    throw new Exception();
                }
            }

            // ごみ分別辞典の取得と保存
            final GarbageDictionaryReader dictionaryReader = new GarbageDictionaryReader();
            if (dictionaryReader.importDictionary(context)) {
                if (!dictionaryReader.saveGarbageDictionary()) {
                    throw new Exception();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 処理に失敗したらすべて削除する
            deleteAllData();
        } finally {
            realm.close();
        }

        return ret;
    }

    /**
     * データベースにデータが存在するかチェック
     *
     * @return
     */
    private boolean isExistDatabase() {
        final long areaDaysCount = realm.where(AreaDays.class).count();
        final long areaMasterCount = realm.where(AreaMaster.class).count();
        final long garbageDataCount = realm.where(GarbageData.class).count();

        if ((areaDaysCount == 0) || (areaMasterCount == 0) || (garbageDataCount == 0)) {
            return false;
        }

        return true;
    }

    /**
     * データをすべて削除する
     */
    public void deleteAllData() {
        boolean createInstance = false;
        if (this.realm == null) {
            realm = Realm.getDefaultInstance();
            createInstance = true;
        }

        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();

        if (createInstance) {
            realm.close();
        }
    }

}
