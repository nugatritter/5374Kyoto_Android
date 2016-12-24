package com.kubotaku.android.code4kyoto5374;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.atilika.kuromoji.TokenizerBase;
import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;
import com.kubotaku.android.code4kyoto5374.data.AreaDays;
import com.kubotaku.android.code4kyoto5374.data.AreaMaster;
import com.kubotaku.android.code4kyoto5374.data.GarbageData;
import com.kubotaku.android.code4kyoto5374.data.GarbageDays;
import com.kubotaku.android.code4kyoto5374.util.AreaDataReader;
import com.kubotaku.android.code4kyoto5374.util.DatabaseCreator;

import java.util.List;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Tokenizer tokenizer = new Tokenizer.Builder().mode(TokenizerBase.Mode.NORMAL).build();
        final List<Token> rets = tokenizer.tokenize("DVDケース");
        for (Token ret : rets) {
            Log.d("test2", ret.getReading());
        }

        test();
    }

    private void test() {
        new TestTask().execute();
    }

    private class TestTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            // for test
            final Realm realm = Realm.getDefaultInstance();

            final long areaMasterCount = realm.where(AreaMaster.class).count();
            final long areaDaysCount = realm.where(AreaDays.class).count();
            final long garbageDataCount = realm.where(GarbageData.class).count();

            realm.beginTransaction();
            realm.deleteAll();
            realm.commitTransaction();
            realm.close();

            final DatabaseCreator databaseCreator = new DatabaseCreator(MainActivity.this);
            databaseCreator.createDatabase();
            return null;
        }
    }
}
