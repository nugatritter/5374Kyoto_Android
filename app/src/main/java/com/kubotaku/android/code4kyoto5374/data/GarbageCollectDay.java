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
package com.kubotaku.android.code4kyoto5374.data;

import com.kubotaku.android.code4kyoto5374.util.AppUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kubotaku1119 on 2017/01/21.
 */

public class GarbageCollectDay {

    public int type;

    public List<GarbageDaysForViews> days;

    public Alarm alarm;

    /**
     * {@link GarbageDays}クラスのクローン。RealmObjectに非依存にしたもの
     * <p>
     * {@link GarbageDays}からの変換には{@link #newInstance(GarbageDays)}を利用する<br>
     * </p>
     */
    public static class GarbageDaysForViews {
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

        /**
         * 表示用の文字列に変換する
         * <p>
         * 毎週水曜日, 第３金曜日
         * </p>
         *
         * @return
         */
        public String toViewString() {
            final String dayOfWeek = AppUtil.convertDayOfWeekText(day) + "曜日";
            if (week == -1) {
                return "毎週" + dayOfWeek;
            } else {
                return "第" + week + dayOfWeek;
            }
        }

        @Override
        public String toString() {
            String s = AppUtil.convertDayOfWeekText(day);
            if (week != -1) {
                s += week;
            }
            return s;
        }

        public static GarbageDaysForViews newInstance(GarbageDays src) {
            GarbageDaysForViews instance = new GarbageDaysForViews();
            instance.week = src.week;
            instance.day = src.day;
            return instance;
        }

        public static List<GarbageDaysForViews> newList(List<GarbageDays> src) {
            List<GarbageDaysForViews> list = new ArrayList<>();
            for (GarbageDays s : src) {
                list.add(newInstance(s));
            }
            return list;
        }
    }
}
