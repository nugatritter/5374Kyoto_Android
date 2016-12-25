package com.kubotaku.android.code4kyoto5374.util;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.kubotaku.android.code4kyoto5374.data.GarbageDays;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;

import static java.util.Calendar.DAY_OF_WEEK;
import static java.util.Calendar.DAY_OF_WEEK_IN_MONTH;

/**
 * Utilities.
 */

public class AppUtil {

    private static final String TAG = AppUtil.class.getSimpleName();

    /**
     * Assets下の指定されたテキストファイルを読み込む
     *
     * @param context  コンテキスト.
     * @param fileName 対象ファイル名
     * @return テキストの内容
     */
    @NonNull
    public static String readFileFromAssets(Context context, String fileName) {

        InputStream is = null;
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        try {
            try {
                is = context.getAssets().open(fileName);
                br = new BufferedReader(new InputStreamReader(is));

                String s;
                while ((s = br.readLine()) != null) {
                    sb.append(s);
                    sb.append("\n");
                }
            } finally {
                if (is != null) {
                    is.close();
                }
                if (br != null) {
                    br.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    public interface GitHubUserContent {
        @GET("/{owner}/{repo}/{branch}/{path}")
        Call<String> getRawData(@Path("owner") String owner, @Path("repo") String repo, @Path("branch") String branch, @Path("path") String path);
    }

    /**
     * GitHubからファイルのRawデータを取得する
     *
     * @param owner  リポジトリオーナー名
     * @param repo   リポジトリ名
     * @param branch ブランチ名
     * @param path   ファイルへのパス
     * @return Rawデータ文字列
     * @throws Exception
     */
    public static String readFileFromGitHub(String owner, String repo, String branch, String path) throws Exception {

        final Retrofit retrofit
                = new Retrofit.Builder()
                .baseUrl("https://raw.githubusercontent.com")
                .addConverterFactory(new ToStringConverterFactory())
                .build();

        final GitHubUserContent service = retrofit.create(GitHubUserContent.class);

        final Call<String> rawData = service.getRawData(owner, repo, branch, path);

        final Response<String> response = rawData.execute();
        if (!response.isSuccessful()) {
            throw new Exception();
        }

        return response.body();
    }

    /**
     * 曜日文字列から{@link Calendar#DAY_OF_WEEK}を返す
     *
     * @param src 曜日文字列
     * @return
     */
    public static int parseDayOfWeek(@NonNull String src) {
        switch (src) {
            case "日":
                return Calendar.SUNDAY;
            case "月":
                return Calendar.MONDAY;
            case "火":
                return Calendar.TUESDAY;
            case "水":
                return Calendar.WEDNESDAY;
            case "木":
                return Calendar.THURSDAY;
            case "金":
                return Calendar.FRIDAY;
            case "土":
                return Calendar.SATURDAY;
        }
        return 0; // error
    }

    /**
     * {@link Calendar#DAY_OF_WEEK}から文字列を返す
     *
     * @param dayOfWeek
     * @return
     */
    public static String convertDayOfWeekText(final int dayOfWeek) {
        switch (dayOfWeek) {
            case 1:
                return "日";
            case 2:
                return "月";
            case 3:
                return "火";
            case 4:
                return "水";
            case 5:
                return "木";
            case 6:
                return "金";
            case 7:
                return "土";
        }
        return ""; // error
    }

    public static String convertDaysAfterText(int daysAfter) {
        switch (daysAfter) {
            case 0:
                return "今日";

            case 1:
                return "明日";

            case 2:
                return "明後日";
        }
        return "" + daysAfter + "日後";
    }

    public static int calcNearestDaysAfter(List<GarbageDays> daysList) {

        final Calendar calendar = Calendar.getInstance();
        final int todayDayOfWeekInMonth = calendar.get(DAY_OF_WEEK_IN_MONTH);
        final int todayDayOfWeek = calendar.get(DAY_OF_WEEK);

        final int hour = calendar.get(Calendar.HOUR_OF_DAY);

        int nearestDaysAfter = 31;

        for (GarbageDays days : daysList) {

            int diffWeek = 0;
            if (days.week != -1) {
                if (todayDayOfWeekInMonth <= days.week) {
                    diffWeek = (days.week - todayDayOfWeekInMonth);
                } else {
                    final int lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                    calendar.set(Calendar.DAY_OF_MONTH, lastDayOfMonth);
                    final int maxWeekInMonth = calendar.getActualMaximum(DAY_OF_WEEK_IN_MONTH);
                    diffWeek = (maxWeekInMonth - todayDayOfWeekInMonth) + days.week - 1;
                }
            }

            if (((todayDayOfWeek < days.day) && (days.week == 0)) ||
                    ((todayDayOfWeek == days.day) && (diffWeek == 0) && (hour < 8))) {
                final int diff = days.day - todayDayOfWeek;
                if (nearestDaysAfter > diff) {
                    nearestDaysAfter = diff;
                }
            } else {
                final int diff = (7 * diffWeek) + days.day - todayDayOfWeek;
                if (nearestDaysAfter > diff) {
                    nearestDaysAfter = diff;
                }
            }
        }

        return nearestDaysAfter;
    }

    public static String createGarbageCollectDaysText(List<GarbageDays> daysList, int nearestDaysAfter) {
        String text = "";
        for (GarbageDays days : daysList) {
            text += days.toViewString();
            text += " ";
        }
        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, nearestDaysAfter);
        text += calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DAY_OF_MONTH);
        return text;
    }

}
