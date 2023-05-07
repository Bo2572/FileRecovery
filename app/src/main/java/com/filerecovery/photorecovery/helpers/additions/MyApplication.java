package com.filerecovery.photorecovery.helpers.additions;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;

public class MyApplication extends Application {
    private static Context mContext;
    private String TAG = "MyApplication";


    public void attachBaseContext(Context context) {
        super.attachBaseContext(context);
    }

    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        Log.i(this.TAG, "OnCleate");
        CookieHandler.setDefault(new CookieManager((CookieStore) null, CookiePolicy.ACCEPT_ALL));
    }

    public static Context getContext() {
        return mContext;
    }
}
