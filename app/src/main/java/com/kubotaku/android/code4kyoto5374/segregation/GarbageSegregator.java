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
package com.kubotaku.android.code4kyoto5374.segregation;

import com.atilika.kuromoji.TokenizerBase;
import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;
import com.kubotaku.android.code4kyoto5374.data.GarbageData;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * ごみの分別処理クラス
 */

public class GarbageSegregator {

    private Tokenizer tokenizer = new Tokenizer.Builder().mode(TokenizerBase.Mode.NORMAL).build();

    public GarbageSegregator() {
    }

    public List<GarbageSegregationData> findCandidates(final String inputText) {
        StringBuilder sb = new StringBuilder();

        final List<Token> rets = tokenizer.tokenize(inputText);
        for (Token ret : rets) {
            String reading = ret.getReading();
            String baseForm = ret.getBaseForm();
            String surface = ret.getSurface();

            if (!reading.equals("*")) {
                sb.append(reading);
            } else {
                sb.append(surface);
            }
        }

        List<GarbageSegregationData> segregationDataList = new ArrayList<>();

        Realm realm = Realm.getDefaultInstance();
        RealmResults<GarbageData> results = realm.where(GarbageData.class)
                .contains("reading", sb.toString()).findAll();

        if (results.size() > 0) {
            for (GarbageData data : results) {
                segregationDataList.add(GarbageSegregationData.newInstance(data));
            }
        } else {
            segregationDataList = null;
        }
        realm.close();

        return segregationDataList;
    }
}
