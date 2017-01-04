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

import java.util.Locale;

/**
 * アラーム通知情報クラス
 */

public class Alarm {

    /**
     * 設定時刻：時
     */
    public int hour;

    /**
     * 設定時刻：分
     */
    public int minute;

    /**
     * 有効無効
     */
    public boolean enable;

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "%1$02d:%2$02d", hour, minute);
    }
}
