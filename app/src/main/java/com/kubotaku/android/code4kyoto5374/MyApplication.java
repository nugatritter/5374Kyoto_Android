package com.kubotaku.android.code4kyoto5374;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Custom Application class.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
