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
package com.kubotaku.android.code4kyoto5374.util;

/**
 * 地域情報ユーティリティクラス
 */

public class AreaUtil {

    private static final String TARGET_CITY = "京都市";

    /**
     * 指定された市が有効か判定する
     *
     * @param city 市名
     * @return
     */
    public static boolean isValidCity(String city) {
        if (city.equals(TARGET_CITY)) {
            return true;
        }
        return false;
    }

    public static String parseCity(String address) {
        final int prefPos = findPrefecturePos(address);
        final int cityPos = findCityPos(address);

        final String city = address.substring(prefPos, cityPos);
        return city;
    }

    public static String parseWard(String address) {
        final int cityPos = findCityPos(address);
        final int wardPos = findWardPos(address);

        final String ward = address.substring(cityPos, wardPos);
        return ward;
    }

    public static String parseTown(String address) {
        final int wardPos = findWardPos(address);
        final int townPos = findTownPos(address);

        final String town = address.substring(wardPos, townPos);
        return town;
    }

    private static int findPrefecturePos(String address) {
        int position = address.indexOf("府");
        if (position == -1) {
            position = address.indexOf("都");
        }
        if (position == -1) {
            position = address.indexOf("県");
        }
        if (position == -1) {
            position = address.indexOf("道");
        }

        return (position + 1);
    }

    private static int findCityPos(String address) {
        return (address.indexOf("市") + 1);
    }

    private static int findWardPos(String address) {
        return (address.indexOf("区") + 1);
    }

    private static int findTownPos(String address) {
        int index = address.indexOf("町");
        if (index == -1) {
            index = address.length();
        } else {
            index = index + 1;
        }
        return index;
    }
}
