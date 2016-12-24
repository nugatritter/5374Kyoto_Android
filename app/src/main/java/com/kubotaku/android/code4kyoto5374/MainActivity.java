package com.kubotaku.android.code4kyoto5374;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.atilika.kuromoji.TokenizerBase;
import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;
import com.kubotaku.android.code4kyoto5374.util.AreaDataReader;

import java.util.List;

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
            final AreaDataReader areaDataReader = new AreaDataReader();
            areaDataReader.importAreaData();
            return null;
        }
    }
}
