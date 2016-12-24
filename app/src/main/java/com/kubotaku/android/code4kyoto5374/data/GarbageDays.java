package com.kubotaku.android.code4kyoto5374.data;

import com.kubotaku.android.code4kyoto5374.util.AppUtil;

import io.realm.RealmObject;

/**
 * ごみ収集日クラス
 * <p>
 * 曜日と月の何周目かを表す
 * </p>
 */
public class GarbageDays extends RealmObject {
    /**
     * 週。-1のときは毎週
     * <p>
     * {@link java.util.Calendar#DAY_OF_WEEK_IN_MONTH}の値
     * </p>
     */
    public int week;

    /**
     * 曜日
     * <p>
     * {@link java.util.Calendar#DAY_OF_WEEK}の値
     * </p>
     */
    public int day;

    @Override
    public String toString() {
        String s = AppUtil.convertDayOfWeekText(day);
        if (week != -1) {
            s += week;
        }
        return s;
    }
}
