package com.example.jerusalemnewsapp;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class RootApplication extends Application {

    public static Realm dbRealm = null;

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);

        String realmName = "jerusalem_news";
        RealmConfiguration config = new RealmConfiguration.Builder().name(realmName).allowWritesOnUiThread(true).build();
        dbRealm = Realm.getInstance(config);

//        Realm.getInstanceAsync(config, new Realm.Callback() {
//            @Override
//            public void onSuccess(Realm realm) {
//            }
//        });

    }
}
