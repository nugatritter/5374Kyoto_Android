package com.kubotaku.android.code4kyoto5374;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.atilika.kuromoji.TokenizerBase;
import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;
import com.kubotaku.android.code4kyoto5374.data.AreaDays;
import com.kubotaku.android.code4kyoto5374.data.AreaMaster;
import com.kubotaku.android.code4kyoto5374.data.GarbageData;
import com.kubotaku.android.code4kyoto5374.fragments.GarbageCollectDaysFragment;
import com.kubotaku.android.code4kyoto5374.fragments.HomeSelectFragment;
import com.kubotaku.android.code4kyoto5374.fragments.OnCloseFragmentListener;
import com.kubotaku.android.code4kyoto5374.util.DatabaseCreator;

import java.util.List;

import io.realm.Realm;

/**
 * MainActivity
 */
public class MainActivity extends BaseActivity implements OnCloseFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Tokenizer tokenizer = new Tokenizer.Builder().mode(TokenizerBase.Mode.NORMAL).build();
        final List<Token> rets = tokenizer.tokenize("DVDケース");
        for (Token ret : rets) {
            Log.d("test2", ret.getReading());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        initialize();
    }

    @Override
    protected void onStop() {
        super.onStop();
        removeSelectHomeView();
        removeGarbageDaysView();
    }

    private void initialize() {
        enabledProgress(true);
        new InitDatabaseTask().execute();
    }

    private void showSelectHomeViewIfNeeded() {

        // TODO:check saved home information

        showSelectHomeView();
    }

    private void showSelectHomeView() {
        final FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentByTag(HomeSelectFragment.TAG) == null) {
            final FragmentTransaction trans = fm.beginTransaction();
            final HomeSelectFragment fragment = HomeSelectFragment.newInstance(HomeSelectFragment.MODE_INITIALIZE);
            trans.add(R.id.view_holder, fragment, HomeSelectFragment.TAG);
            trans.commitAllowingStateLoss();
        }
    }

    private void removeSelectHomeView() {
        final FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentByTag(HomeSelectFragment.TAG) != null) {
            final FragmentTransaction trans = fm.beginTransaction();
            trans.remove(fm.findFragmentByTag(HomeSelectFragment.TAG));
            trans.commitAllowingStateLoss();
        }
    }

    private void showGarbageDaysView() {
        final FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentByTag(GarbageCollectDaysFragment.TAG) == null) {
            final FragmentTransaction trans = fm.beginTransaction();
            final GarbageCollectDaysFragment fragment = GarbageCollectDaysFragment.newInstance();
            trans.add(R.id.view_holder, fragment, GarbageCollectDaysFragment.TAG);
            trans.commitAllowingStateLoss();
        }
    }

    private void removeGarbageDaysView() {
        final FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentByTag(GarbageCollectDaysFragment.TAG) != null) {
            final FragmentTransaction trans = fm.beginTransaction();
            trans.remove(fm.findFragmentByTag(GarbageCollectDaysFragment.TAG));
            trans.commitAllowingStateLoss();
        }
    }

    @Override
    public void onCloseFragment(Fragment fragment) {
        if (fragment instanceof HomeSelectFragment) {
            removeSelectHomeView();
            showGarbageDaysView();
        }
    }


    // -----------------------------

    private void enabledProgress(boolean enabled) {
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress);
        if (enabled) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    // --------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.menu_select_home:
                removeGarbageDaysView();
                showSelectHomeView();
                return true;

            case R.id.menu_license:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // --------------------------------------


    private class InitDatabaseTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {

            // for test
            {
//                final Realm realm = Realm.getDefaultInstance();
//
//                // check saved size
//                final long areaMasterCount = realm.where(AreaMaster.class).count();
//                final long areaDaysCount = realm.where(AreaDays.class).count();
//                final long garbageDataCount = realm.where(GarbageData.class).count();
//
//                // initialize all data
//                realm.beginTransaction();
//                realm.deleteAll();
//                realm.commitTransaction();
//                realm.close();
            }

            final DatabaseCreator databaseCreator = new DatabaseCreator(MainActivity.this);
            return databaseCreator.createDatabase();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            enabledProgress(false);
            if (result) {
                showSelectHomeViewIfNeeded();
            } else {
                Toast toast = Toast.makeText(MainActivity.this, "データの読み込みに失敗しました...。再起動して下さい", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

                finish();
            }
        }
    }
}
