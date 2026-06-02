package com.example.projectprm392;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.projectprm392.api.RetrofitClient;

public class MyApp extends Application {
    private static MyApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static Context getAppContext() {
        return instance.getApplicationContext();
    }
}
