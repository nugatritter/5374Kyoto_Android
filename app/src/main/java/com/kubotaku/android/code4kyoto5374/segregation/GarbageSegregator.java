package com.kubotaku.android.code4kyoto5374.segregation;

import android.util.Log;

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
