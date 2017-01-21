京都市ごみの捨て方・分別方法判定アプリケーション
===================

About This Project
-----------------------------------

このアプリケーションは、京都市のごみの収集日の表示とごみの捨て方・分類を簡単に調べるためのアプリケーションです。  
  
[Code for Kanazawa](http://codeforkanazawa.org/)の
[5374プロジェクト](https://github.com/codeforkanazawa-org/5374)をベースとし、  
Code for Kyotoの活動の一環で作成しています。  
  
ごみの収集日・分別情報は、[Code for Kyotoの5374プロジェクト](https://github.com/ofuku3f/5374osaka.github.com)および、
[京都市オープンデータポータルサイト](https://data.city.kyoto.lg.jp/)のデータを利用しています。


How to localize this Application
-----------------------------------

このプロジェクトをForkし、各自治体向けのアプリケーションを作成し、公開する場合には、以下の修正を行ってください。
  
#### パッケージ名の変更

パッケージ名は必ず別のものに変更してください

#### API Keyの変更

Google Place APIを利用しています。APIキーを取得し、[AndroidManifest.xml](/app/src/main/AndroidManifest.xml)
の該当箇所に設定してください

    <!-- for Google Place API -->
    <!-- Please change your API_KEY -->
    <meta-data
        android:name="com.google.android.geo.API_KEY"
        android:value="YOUR API KEY" />

#### ごみ収集日情報の参照先を変更

ごみ取集日情報は、5374プロジェクトのデータ形式に従っています。  
このプロジェクトでは5374プロジェクトのGitHubに公開されているCSVファイルにアクセスしデータを取得します。

[AreaDataReader](/app/src/main/java/com/kubotaku/android/code4kyoto5374/util/AreaDataReader.java)の以下の定義を変更してください

    public class AreaDataReader {
        // GitHubのユーザー名
        private static final String GITHUB_OWNER = "ofuku3f";

        // GitHubのリポジトリ名
        private static final String GITHUB_REPO = "5374osaka.github.com";

        // GitHubのブランチ名
        private static final String GITHUB_BRANCH = "gh-pages";

        // 対象となる地区別収集日情報ファイル名
        private static final String GITHUB_AREA_DAYS = "data/area_days.csv";
    
        // 対象となる地区情報マスタファイル名
        private static final String GITHUB_AREA_MASTER = "data/area_master.csv";
        
        ...
    }


#### ごみの種別表記名・色の変更

ごみの種別名の表記、色を変更する際は、以下のファイルを修正してください

* 表記の変更 [GarbageType.java](/app/src/main/java/com/kubotaku/android/code4kyoto5374/data/GarbageType.java)
* 色の変更 [color.xml](/app/src/main/res/values/colors.xml)


#### ごみの分別方法辞典のデータ変更

ごみの分別方法を調べる機能は、各自治体ごとの分別方法をデータ化し、CSVファイルとして、  
assetsフォルダ下にgarbage_dictionary.csvとして配置してください
* [garbage_dictionary.csv](/app/src/main/assets/garbage_dictionary.csv)


How to get Application
-----------------------------------

Google Playからインストールしてください（現在α公開中）


Functions List
-----------------------------------

以下の機能を実装しています。  
  
* ごみの収集日表示
* ごみの収集日表示地点の選択
* ごみ収集日にアラーム通知（Notificaion）
* ごみの分別方法検索
* ごみの収集日表示ウィジェット

License
-----------------------------------
      Copyright 2017 kubotaku1119 <kubotaku1119@gmail.com>
      
      Licensed under the Apache License, Version 2.0 (the "License");
      you may not use this file except in compliance with the License.
      You may obtain a copy of the License at
      
        http://www.apache.org/licenses/LICENSE-2.0
        
      Unless required by applicable law or agreed to in writing, software
      distributed under the License is distributed on an "AS IS" BASIS,
      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
      See the License for the specific language governing permissions and
      limitations under the License.

