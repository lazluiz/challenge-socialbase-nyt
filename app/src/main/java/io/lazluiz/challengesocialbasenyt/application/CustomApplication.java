package io.lazluiz.challengesocialbasenyt.application;

import android.app.Application;

import io.lazluiz.challengesocialbasenyt.data.NetworkQueue;

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
    }
}