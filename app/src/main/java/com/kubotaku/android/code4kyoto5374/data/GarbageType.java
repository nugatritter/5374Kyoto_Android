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

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.content.res.AppCompatResources;

import com.kubotaku.android.code4kyoto5374.R;

import io.realm.RealmObject;

/**
 * ごみ区分クラス.
 */

public class GarbageType extends RealmObject {

    /**
     * 禁止：京都市では回収していないもの
     */
    public static final int TYPE_NO = 0;

    /**
     * 禁止：京都市では回収していないもの
     */
    public static final String TEXT_TYPE_NO = "禁止";

    /**
     * 燃やす：燃やすゴミ収集
     */
    public static final int TYPE_BURNABLE = 1;

    /**
     * 燃やす：燃やすゴミ収集
     */
    public static final String TEXT_TYPE_BURNABLE = "燃やす";

    /**
     * 缶等：缶・ビン・ペットボトル分別収集
     */
    public static final int TYPE_BIN = 2;

    /**
     * 缶等：缶・ビン・ペットボトル分別収集
     */
    public static final String TEXT_TYPE_BIN = "缶等";

    /**
     * プラ：プラスチック製の容器と包装分別収集
     */
    public static final int TYPE_PLASTIC = 3;

    /**
     * プラ：プラスチック製の容器と包装分別収集
     */
    public static final String TEXT_TYPE_PLASTIC = "プラ";

    /**
     * 小金：小型金属類分別収集
     */
    public static final int TYPE_SMALL = 4;

    /**
     * 小金：小型金属類分別収集
     */
    public static final String TEXT_TYPE_SMALL = "小金";

    /**
     * 大型：大型ごみ
     */
    public static final int TYPE_BIG = 5;

    /**
     * 大型：大型ごみ
     */
    public static final String TEXT_TYPE_BIG = "大型";

    /**
     * 拠回：拠点回収
     */
    public static final int TYPE_PLACE = 6;

    /**
     * 拠回：拠点回収
     */
    public static final String TEXT_TYPE_PLACE = "拠回";

    /**
     * 雑がみ：新聞・段ボール・紙パック以外のリサイクル可能な紙類
     */
    public static final int TYPE_PAPER = 7;

    /**
     * 雑がみ：新聞・段ボール・紙パック以外のリサイクル可能な紙類
     */
    public static final String TEXT_TYPE_PAPER = "雑がみ";

    /**
     * リ法：法律でリサイクルが定められているもの
     */
    public static final int TYPE_RECYCLE = 8;

    /**
     * リ法：法律でリサイクルが定められているもの
     */
    public static final String TEXT_TYPE_RECYCLE = "リ法";

    /**
     * 店頭：販売店等自主回収ルートあり
     */
    public static final int TYPE_SHOP = 9;

    /**
     * 店頭：販売店等自主回収ルートあり
     */
    public static final String TEXT_TYPE_SHOP = "店頭";

    /**
     * 新聞紙
     */
    public static final int TYPE_PAPER_NEWS_PAPER = 10;

    /**
     * 新聞紙
     */
    public static final String TEXT_TYPE_PAPER_NEWS_PAPER = "新聞";

    /**
     * 段ボール
     */
    public static final int TYPE_PAPER_CARDBOARD = 11;

    /**
     * 段ボール
     */
    public static final String TEXT_TYPE_PAPER_CARDBOARD = "ダンボール";

    /**
     * ごみ区別
     */
    public int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /**
     * タイプ別の文字列を取得する
     * <p>
     * 燃える, プラなどの短縮された文字列
     * </p>
     *
     * @param type タイプ
     * @return 文字列
     */
    public static String getTypeText(final int type) {
        switch (type) {
            case TYPE_NO:
                return TEXT_TYPE_NO;
            case TYPE_BURNABLE:
                return TEXT_TYPE_BURNABLE;
            case TYPE_BIN:
                return TEXT_TYPE_BIN;
            case TYPE_PLASTIC:
                return TEXT_TYPE_PLASTIC;
            case TYPE_SMALL:
                return TEXT_TYPE_SMALL;
            case TYPE_BIG:
                return TEXT_TYPE_BIG;
            case TYPE_PLACE:
                return TEXT_TYPE_PLACE;
            case TYPE_PAPER:
                return TEXT_TYPE_PAPER;
            case TYPE_RECYCLE:
                return TEXT_TYPE_RECYCLE;
            case TYPE_SHOP:
                return TEXT_TYPE_SHOP;
            case TYPE_PAPER_NEWS_PAPER:
                return TEXT_TYPE_PAPER_NEWS_PAPER;
            case TYPE_PAPER_CARDBOARD:
                return TEXT_TYPE_PAPER_CARDBOARD;
            default:
                return "";
        }
    }

    /**
     * ごみタイプ情報を文字列からパースする.
     *
     * @param src 元文字列
     * @return
     */
    public static GarbageType parse(String src) {

        final GarbageType garbageType = new GarbageType();

        switch (src) {
            case TEXT_TYPE_NO:
                garbageType.type = TYPE_NO;
                break;
            case TEXT_TYPE_BURNABLE:
                garbageType.type = TYPE_BURNABLE;
                break;
            case TEXT_TYPE_BIN:
                garbageType.type = TYPE_BIN;
                break;
            case TEXT_TYPE_PLASTIC:
                garbageType.type = TYPE_PLASTIC;
                break;
            case TEXT_TYPE_SMALL:
                garbageType.type = TYPE_SMALL;
                break;
            case TEXT_TYPE_BIG:
                garbageType.type = TYPE_BIG;
                break;
            case TEXT_TYPE_PLACE:
                garbageType.type = TYPE_PLACE;
                break;
            case TEXT_TYPE_PAPER:
                garbageType.type = TYPE_PAPER;
                break;
            case TEXT_TYPE_RECYCLE:
                garbageType.type = TYPE_RECYCLE;
                break;
            case TEXT_TYPE_SHOP:
                garbageType.type = TYPE_SHOP;
                break;
            case TEXT_TYPE_PAPER_NEWS_PAPER:
                garbageType.type = TYPE_PAPER_NEWS_PAPER;
                break;
            case TEXT_TYPE_PAPER_CARDBOARD:
                garbageType.type = TYPE_PAPER_CARDBOARD;
                break;
        }

        return garbageType;
    }

    /**
     * タイプから表示用の長い文字列を取得する.
     *
     * @param type タイプ
     * @return 文字列
     */
    public static String getViewText(final int type) {
        switch (type) {
            case TYPE_BURNABLE:
                return "燃やすごみ";

            case TYPE_BIN:
                return "缶 ビン ペットボトル";

            case TYPE_SMALL:
                return "小型金属";

            case TYPE_PLASTIC:
                return "プラスチック製容器包装";
        }
        return "";
    }

    /**
     * タイプから表示用の背景色を取得する
     *
     * @param context コンテキスト
     * @param type    タイプ
     * @return resource/colorのID
     */
    public static int getViewColor(final Context context, final int type) {
        switch (type) {
            case TYPE_BURNABLE:
                return ContextCompat.getColor(context, R.color.color_burnable);

            case TYPE_BIN:
                return ContextCompat.getColor(context, R.color.color_bin);

            case TYPE_SMALL:
                return ContextCompat.getColor(context, R.color.color_small);

            case TYPE_PLASTIC:
                return ContextCompat.getColor(context, R.color.color_plastic);

            case TYPE_PLACE:
                return ContextCompat.getColor(context, R.color.color_place);

            case TYPE_PAPER:
                return ContextCompat.getColor(context, R.color.color_paper);

            case TYPE_BIG:
                return ContextCompat.getColor(context, R.color.color_big);

            case TYPE_NO:
                return ContextCompat.getColor(context, R.color.color_no);

            case TYPE_SHOP:
                return ContextCompat.getColor(context, R.color.color_shop);
        }
        return 0;
    }

    /**
     * タイプから表示用の文字色を取得する
     *
     * @param context コンテキスト
     * @param type    タイプ
     * @return
     */
    public static int getViewTextColor(final Context context, final int type) {
        switch (type) {
            case TYPE_BURNABLE:
                return ContextCompat.getColor(context, R.color.text_garbage_type_inverse);

            case TYPE_BIN:
            case TYPE_SMALL:
            case TYPE_PLASTIC:
            case TYPE_PLACE:
            case TYPE_PAPER:
            case TYPE_BIG:
            case TYPE_NO:
            case TYPE_SHOP:
                return ContextCompat.getColor(context, R.color.text_garbage_type);
        }
        return 0;
    }

    /**
     * タイプからアラート設定アイコンを取得する
     *
     * @param context コンテキスト
     * @param type    タイプ
     * @return
     */
    public static Drawable getAlarmClockDrawable(final Context context, final int type) {
        int id = getAlarmClockResourceId(context, type);
        if (id == -1) {
            return null;
        }
        return ContextCompat.getDrawable(context, id);
    }

    /**
     * タイプからアラート設定アイコンを取得する
     *
     * @param context コンテキスト
     * @param type    タイプ
     * @return
     */
    public static int getAlarmClockResourceId(final Context context, final int type) {
        switch (type) {
            case TYPE_BURNABLE:
                return R.drawable.ic_alarm_inverse;

            case TYPE_BIN:
            case TYPE_SMALL:
            case TYPE_PLASTIC:
            case TYPE_PLACE:
            case TYPE_PAPER:
            case TYPE_BIG:
            case TYPE_NO:
            case TYPE_SHOP:
                return R.drawable.ic_alarm;
        }
        return -1;
    }
}
