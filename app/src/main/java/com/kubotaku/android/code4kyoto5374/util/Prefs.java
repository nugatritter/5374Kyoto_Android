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
