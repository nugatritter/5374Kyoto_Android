package com.kubotaku.android.code4kyoto5374.data;

/**
 * Created by kubotaku1119 on 2016/12/24.
 */

public class GarbageData {

    /**
     * 禁止：京都市では回収していないもの
     */
    public static final int TYPE_NO = 0;

    /**
     * 燃やす：燃やすゴミ収集
     */
    public static final int TYPE_BURNABLE = 1;

    /**
     * 缶等：缶・ビン・ペットボトル分別収集
     */
    public static final int TYPE_BIN = 2;

    /**
     * プラ：プラスチック製の容器と包装分別収集
     */
    public static final int TYPE_PLASTIC = 3;

    /**
     * 小金：小型金属類分別収集
     */
    public static final int TYPE_SMALL = 4;

    /**
     * 大型：大型ごみ
     */
    public static final int TYPE_BIG = 5;

    /**
     * 拠回：拠点回収
     */
    public static final int TYPE_PLACE = 6;

    /**
     * 雑がみ：新聞・段ボール・紙パック以外のリサイクル可能な紙類
     */
    public static final int TYPE_PAPER = 7;

    /**
     * リ法：法律でリサイクルが定められているもの
     */
    public static final int TYPE_RECYCLE = 8;

    /**
     * 店頭：販売店等自主回収ルートあり
     */
    public static final int TYPE_SHOP = 9;

    /**
     * 新聞紙
     */
    public static final int TYPE_PAPER_NEWS_PAPER = 10;

    /**
     * 段ボール
     */
    public static final int TYPE_PAPER_CARDBOARD = 11;

    /**
     * 紙パック
     */
    public static final int TYPE_PAPER_PACK = 12;

    private static final int TYPE_FIRST = TYPE_NO;

    private static final int TYPE_LAST = TYPE_PAPER_PACK;

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
    public int type;

    public static GarbageData creator(String name, String reading, String type, String note) {
        final GarbageData data = new GarbageData();

        data.name = name;
        data.reading = reading;
        data.note = note;

        data.type = getType(type);

        return data;
    }

    public static int getType(String src) {
        int type = TYPE_NO;

        try {
            type = Integer.parseInt(src);

            if ((type < TYPE_FIRST) || (type > TYPE_LAST)) {
                type = TYPE_NO;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return type;
    }

}
