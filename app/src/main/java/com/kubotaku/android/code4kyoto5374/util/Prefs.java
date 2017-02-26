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
import android.content.SharedPreferences;

import com.kubotaku.android.code4kyoto5374.data.Alarm;
import com.kubotaku.android.code4kyoto5374.data.HomePlace;

/**
 * SharedPreferences Wrapper class.
 */

public class Prefs {

    private static final String PREFS_NAME = "app_prefs";

    private static final String KEY_HOME_ADDRESS = "key_home_address";

    private static final String KEY_HOME_LAT = "key_home_lat";

    private static final String KEY_HOME_LON = "key_home_lon";

    private static final String KEY_HOME_AREA_MASTER_ID = "key_home_area_master_id";

    private static final String KEY_HOME_AREA_NAME = "key_home_area_name";

    private static final String KEY_ALARM_HOUR = "key_alarm_hour_";

    private static final String KEY_ALARM_MINUTE = "key_alarm_minute_";

    private static final String KEY_ALARM_ENABLE = "key_alarm_enable_";

    private static final String KEY_GITHUB_LATEST_UPDATE = "key_github_latest_update";

    private static final String KEY_GITHUB_LATEST_SHA = "key_github_latest_sha";

    /**
     * GitHub（エリア情報）の最終更新コミットのSHAを保存する
     *
     * @param context コンテキスト
     * @param sha     最終更新コミットのSHA
     */
    public static void saveGitHubLatestSHA(Context context, final String sha) {
        final SharedPreferences prefs = getPrefs(context, PREFS_NAME);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_GITHUB_LATEST_SHA, sha);
        editor.apply();
    }

    /**
     * GitHub(エリア情報)の最終更新コミットのSHAを読み込む
     *
     * @param context コンテキスト
     * @return SHA
     */
    public static String loadGitHubLatestSHA(Context context) {
        final SharedPreferences prefs = getPrefs(context, PREFS_NAME);
        return prefs.getString(KEY_GITHUB_LATEST_SHA, "");
    }

    /**
     * GitHub（エリア情報）の最終更新日時を保存する
     *
     * @param context コンテキスト
     * @param date    最終更新日時(ISO 8601形式)
     */
    public static void saveGitHubLatestUpdate(Context context, final String date) {
        final SharedPreferences prefs = getPrefs(context, PREFS_NAME);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_GITHUB_LATEST_UPDATE, date);
        editor.apply();
    }

    /**
     * GitHub(エリア情報)の最終更新日時を読み込む
     *
     * @param context コンテキスト
     * @return 最終更新日時
     */
    public static String loadGitHubLatestUpdate(Context context) {
        final SharedPreferences prefs = getPrefs(context, PREFS_NAME);
        return prefs.getString(KEY_GITHUB_LATEST_UPDATE, "2017-02-01T00:00");
    }


    /**
     * 通知設定を保存する
     *
     * @param context     コンテキスト
     * @param garbageType ごみ種別
     * @param alarm       アラート設定
     */
    public static void saveAlarm(Context context, final int garbageType, final Alarm alarm) {
        final SharedPreferences prefs = getPrefs(context, PREFS_NAME);
        final SharedPreferences.Editor editor = prefs.edit();

        editor.putInt(KEY_ALARM_HOUR + garbageType, alarm.hour);
        editor.putInt(KEY_ALARM_MINUTE + garbageType, alarm.minute);
        editor.putBoolean(KEY_ALARM_ENABLE + garbageType, alarm.enable);

        editor.apply();
    }

    /**
     * 通知設定を読み込む
     *
     * @param context     コンテキスト
     * @param garbageType ごみ種別
     * @return アラート設定
     */
    public static Alarm loadAlarm(Context context, final int garbageType) {
        final SharedPreferences prefs = getPrefs(context, PREFS_NAME);

        final Alarm alarm = new Alarm();
        alarm.hour = prefs.getInt(KEY_ALARM_HOUR + garbageType, 6);
        alarm.minute = prefs.getInt(KEY_ALARM_MINUTE + garbageType, 0);
        alarm.enable = prefs.getBoolean(KEY_ALARM_ENABLE + garbageType, false);

        return alarm;
    }

    /**
     * 自宅所在地情報を保存する
     * <p>
     * 各値は暗号化して文字列で保存する
     * </p>
     *
     * @param context   コンテキスト
     * @param homePlace 自宅情報
     */
    public static void saveHomePlace(Context context, HomePlace homePlace) {
        final SharedPreferences prefs = getPrefs(context, PREFS_NAME);
        final SharedPreferences.Editor editor = prefs.edit();

        final String encryptAddress = EncryptUtil.encryptString(context, homePlace.address);
        editor.putString(KEY_HOME_ADDRESS, encryptAddress);

        final String encryptLat = EncryptUtil.encryptString(context, "" + homePlace.latitude);
        editor.putString(KEY_HOME_LAT, encryptLat);

        final String encryptLon = EncryptUtil.encryptString(context, "" + homePlace.longitude);
        editor.putString(KEY_HOME_LON, encryptLon);

        final String encryptAreaMasterID = EncryptUtil.encryptString(context, "" + homePlace.areaMasterID);
        editor.putString(KEY_HOME_AREA_MASTER_ID, encryptAreaMasterID);

        final String encryptAreaName = EncryptUtil.encryptString(context, "" + homePlace.areaName);
        editor.putString(KEY_HOME_AREA_NAME, encryptAreaName);

        editor.apply();
    }

    /**
     * 自宅所在地情報を読み込む
     *
     * @param context コンテキスト
     * @return 自宅所在地情報
     */
    public static HomePlace loadHomePlace(Context context) {
        final SharedPreferences prefs = getPrefs(context, PREFS_NAME);

        final HomePlace homePlace = new HomePlace();

        final String encryptAddress = prefs.getString(KEY_HOME_ADDRESS, "-");
        homePlace.address = EncryptUtil.decryptString(context, encryptAddress);

        final String encryptLat = prefs.getString(KEY_HOME_LAT, "0");
        final String latText = EncryptUtil.decryptString(context, encryptLat);

        final String encryptLon = prefs.getString(KEY_HOME_LON, "0");
        final String lonText = EncryptUtil.decryptString(context, encryptLon);

        final String encryptAreaMasterID = prefs.getString(KEY_HOME_AREA_MASTER_ID, "-1");
        final String areaMasterIDText = EncryptUtil.decryptString(context, encryptAreaMasterID);

        final String encryptAreaName = prefs.getString(KEY_HOME_AREA_NAME, "-");
        homePlace.areaName = EncryptUtil.decryptString(context, encryptAreaName);

        try {
            homePlace.latitude = Double.parseDouble(latText);
            homePlace.longitude = Double.parseDouble(lonText);
            homePlace.areaMasterID = Integer.parseInt(areaMasterIDText);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return homePlace;
    }


    private static SharedPreferences getPrefs(Context context, String prefName) {
        return context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
    }
}
