package com.filerecovery.photorecovery.helpers.additions;

import android.content.Context;
import android.content.SharedPreferences;

public class MyPreferenceManager {
    private static final String PREF_NAME = "com.designsgate.zawagorg";

    public static void putString(Context context, String str, String str2) {
        context.getSharedPreferences(PREF_NAME, 0).edit().putString(str, str2).apply();
    }

    public static String getString(Context context, String str) {
        return context.getSharedPreferences(PREF_NAME, 0).getString(str, (String) null);
    }

    public static void removeAllSharedPreferences(Context context) {
        SharedPreferences.Editor edit = context.getSharedPreferences(PREF_NAME, 0).edit();
        edit.clear();
        edit.commit();
    }

    public static void remove(Context context, String str) {
        context.getSharedPreferences(PREF_NAME, 0).edit().remove(str).commit();
    }

    public static boolean exists(Context context, String str) {
        return context.getSharedPreferences(PREF_NAME, 0).contains(str);
    }
}
