package io.lazluiz.challengesocialbasenyt.application;

import android.app.Application;

import io.lazluiz.challengesocialbasenyt.data.NetworkQueue;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by luiz on 06/07/16.
 * E-mail: lf.lazzarin@gmail.com
 * GitHub: github.com/luizfelippe
 */

public class CustomApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Volley
        NetworkQueue.getInstance().init(this);

        // Realm
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .name("realm-nyt.realm")
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(realmConfiguration);
    }
}