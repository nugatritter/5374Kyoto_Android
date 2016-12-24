package com.kubotaku.android.code4kyoto5374.data;

import com.kubotaku.android.code4kyoto5374.util.AppUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * ごみの収集日の地区別情報クラス
 */
public class AreaDays {

    /**
     * ごみ収集日クラス
     * <p>
     * 曜日と月の何周目かを表す
     * </p>
     */
    public static class GarbageDays {
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

    /**
     * エリアマスタのID
     */
    public int masterAreaID;

    /**
     * 地区名
     */
    public String areaName;

    /**
     * 収集センター名称
     */
    public String centerName;

    /**
     * 燃やすごみの日
     */
    public List<GarbageDays> burnableDays;

    /**
     * 缶、ビン、ペットボトルの日
     */
    public List<GarbageDays> binDays;

    /**
     * プラごみの日
     */
    public List<GarbageDays> plasticDays;

    /**
     * 資源ごみの日
     */
    public List<GarbageDays> recycleDays;


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append(areaName);
        sb.append("\n");
        sb.append("  マスターID : " + masterAreaID);
        sb.append("\n");
        sb.append("  収集センター : " + centerName);
        sb.append("\n");
        sb.append("  燃やすごみ : ");
        for (GarbageDays d : burnableDays) {
            sb.append(d.toString() + " ");
        }
        sb.append("\n");

        sb.append("  缶、ビン、ペットボトル : ");
        for (GarbageDays d : binDays) {
            sb.append(d.toString() + " ");
        }
        sb.append("\n");

        sb.append("  プラごみ : ");
        for (GarbageDays d : plasticDays) {
            sb.append(d.toString() + " ");
        }
        sb.append("\n");

        sb.append("  資源ごみ : ");
        for (GarbageDays d : recycleDays) {
            sb.append(d.toString() + " ");
        }
        sb.append("\n");

        return sb.toString();
    }

    /**
     * ごみ収集の地区情報クラスを生成する
     *
     * @param src 元データ
     * @return
     */
    public static AreaDays create(String src) {
        final AreaDays areaDays = new AreaDays();

        String[] sepEntity = src.split(",");

        areaDays.masterAreaID = Integer.parseInt(sepEntity[0]);
        areaDays.areaName = sepEntity[1];
        areaDays.centerName = sepEntity[2];

        areaDays.burnableDays = parseGarbageDays(sepEntity[3]);
        areaDays.binDays = parseGarbageDays(sepEntity[4]);
        areaDays.plasticDays = parseGarbageDays(sepEntity[5]);
        areaDays.recycleDays = parseGarbageDays(sepEntity[6]);

        return areaDays;
    }

    /**
     * 各ごみの収集日をパースする
     * <p>
     * 元データは「月1 木3」のような形式
     * </p>
     *
     * @param src 元データ
     * @return
     */
    private static List<GarbageDays> parseGarbageDays(String src) {
        List<GarbageDays> garbageDays = new ArrayList<>();

        String[] sepEntity = src.split(" ");
        for (String entity : sepEntity) {
            entity = entity.replace("\r", "");
            final int length = entity.length();
            switch (length) {
                case 1: {
                    final GarbageDays days = new GarbageDays();
                    days.week = -1;
                    days.day = AppUtil.parseDayOfWeek(entity.substring(0, 1));
                    garbageDays.add(days);
                }
                break;

                case 2: {
                    final GarbageDays days = new GarbageDays();
                    days.week = Integer.parseInt(entity.substring(1));
                    days.day = AppUtil.parseDayOfWeek(entity.substring(0, 1));
                    garbageDays.add(days);
                }
                break;

                default:
                    break;
            }
        }

        return garbageDays;
    }
}
