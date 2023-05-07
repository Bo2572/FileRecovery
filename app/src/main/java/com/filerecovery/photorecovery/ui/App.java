package com.filerecovery.photorecovery.ui;

import android.content.Context;

import androidx.multidex.MultiDexApplication;

public class App extends MultiDexApplication {

    public static Context mContext;

    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();

    }
}
