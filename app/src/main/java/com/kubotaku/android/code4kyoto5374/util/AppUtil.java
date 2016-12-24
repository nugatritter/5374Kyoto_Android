package com.kubotaku.android.code4kyoto5374.util;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

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

}
