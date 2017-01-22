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
package com.kubotaku.android.code4kyoto5374;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.kubotaku.android.code4kyoto5374.data.HomePlace;
import com.kubotaku.android.code4kyoto5374.fragments.GarbageCollectDaysFragment;
import com.kubotaku.android.code4kyoto5374.fragments.HomeSelectFragment;
import com.kubotaku.android.code4kyoto5374.fragments.OnCloseFragmentListener;
import com.kubotaku.android.code4kyoto5374.fragments.OpenSourceLicenseDialog;
import com.kubotaku.android.code4kyoto5374.util.DatabaseCreator;
import com.kubotaku.android.code4kyoto5374.util.Prefs;

import io.realm.Realm;

/**
 * MainActivity
 */
public class MainActivity extends BaseActivity implements OnCloseFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        // check saved home information
        HomePlace savedHomePlace = Prefs.loadHomePlace(this);
        if ((savedHomePlace.address == null) ||
                savedHomePlace.address.isEmpty() ||
                savedHomePlace.address.equals("-")) {
            showSelectHomeView();
        } else {
            showGarbageDaysView();
        }
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
                showOpenSourceLicense();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // --------------------------------------

    private void showOpenSourceLicense() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentByTag(OpenSourceLicenseDialog.TAG) == null) {
            OpenSourceLicenseDialog dialog = OpenSourceLicenseDialog.newInstance();
            dialog.show(fm, OpenSourceLicenseDialog.TAG);
        }
    }

    // --------------------------------------


    private class InitDatabaseTask extends AsyncTask<Void, Void, Boolean> {

        public InitDatabaseTask() {
            TextView initText = (TextView) findViewById(R.id.text_init);
            initText.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            // for test
            {
                final Realm realm = Realm.getDefaultInstance();

                // initialize all data
                realm.beginTransaction();
                realm.deleteAll();
                realm.commitTransaction();
                realm.close();
            }

            final DatabaseCreator databaseCreator = new DatabaseCreator(MainActivity.this);
            return databaseCreator.createDatabase();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            enabledProgress(false);

            TextView initText = (TextView) findViewById(R.id.text_init);
            initText.setVisibility(View.GONE);

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
