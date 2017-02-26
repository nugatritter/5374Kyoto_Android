package com.kubotaku.android.code4kyoto5374.util.github;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


/**
 * GitHub上のデータファイルにアクセスする
 */

public class DataSourceAccessor {

    interface GitHubCommit {
        @GET("/repos/{owner}/{repo}/commits")
        Call<List<CommitHistory>> getCommits(@Path("owner") String owner, @Path("repo") String repo, @Query("since") String since);
    }

    /**
     * GitHubのコミット履歴を取得する
     *
     * @param owner リポジトリオーナー名
     * @param repo  リポジトリ名
     * @param since 取得する履歴の開始日時（ISO8601形式）
     * @return コミット履歴
     * @throws Exception
     */
    public static List<CommitHistory> getCommitHistory(String owner, String repo, String since) throws Exception {

        final Retrofit retrofit
                = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final GitHubCommit service = retrofit.create(GitHubCommit.class);

        final Call<List<CommitHistory>> call = service.getCommits(owner, repo, since);

        final Response<List<CommitHistory>> response = call.execute();

        if (!response.isSuccessful()) {
            throw new Exception();
        }

        return response.body();
    }


    interface GitHubUserContent {
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
}
