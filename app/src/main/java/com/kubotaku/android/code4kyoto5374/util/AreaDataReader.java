package com.kubotaku.android.code4kyoto5374.util;

import com.kubotaku.android.code4kyoto5374.data.AreaDays;
import com.kubotaku.android.code4kyoto5374.data.AreaMaster;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * 地域別ごみ収集日情報のリーダークラス
 * <p>
 * GitHub上のCSVファイルからデータ取得する
 * </p>
 */
public class AreaDataReader {

    private static final String GITHUB_OWNER = "ofuku3f";

    private static final String GITHUB_REPO = "5374osaka.github.com";

    private static final String GITHUB_BRANCH = "gh-pages";

    private static final String GITHUB_AREA_DAYS = "data/area_days.csv";

    private static final String GITHUB_AREA_MASTER = "data/area_master.csv";

    private List<AreaMaster> areaMasterList;

    private List<AreaDays> areaDaysList;

    public boolean importAreaData() {

        boolean ret = true;

        try {
            // 地域マスター情報
            String areaMasterContent = AppUtil.readFileFromGitHub(GITHUB_OWNER, GITHUB_REPO, GITHUB_BRANCH, GITHUB_AREA_MASTER);
            String[] sepAreaMaster = areaMasterContent.split("\n");

            areaMasterList = new ArrayList<>();
            for (int index = 1; index < sepAreaMaster.length; index++) { // 1行目はヘッダー
                final AreaMaster areaMaster = AreaMaster.create(sepAreaMaster[index]);
                areaMasterList.add(areaMaster);
            }

            // 区域別ごみ収集日情報
            String areaDaysContent = AppUtil.readFileFromGitHub(GITHUB_OWNER, GITHUB_REPO, GITHUB_BRANCH, GITHUB_AREA_DAYS);
            String[] sepAreaDays = areaDaysContent.split("\n");

            areaDaysList = new ArrayList<>();
            for (int index = 1; index < sepAreaDays.length; index++) { // 1行目はヘッダー
                final AreaDays areaDays = AreaDays.create(sepAreaDays[index]);
                areaDaysList.add(areaDays);
            }

        } catch (Exception e) {
            e.printStackTrace();
            ret = false;
        }

        return ret;
    }

    public boolean saveAreaData() {
        boolean ret = true;

        final Realm realm = Realm.getDefaultInstance();

        try {
            realm.beginTransaction();
            realm.copyToRealm(areaMasterList);
            realm.copyToRealm(areaDaysList);
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
