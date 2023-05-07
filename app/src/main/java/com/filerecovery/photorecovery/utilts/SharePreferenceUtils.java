package com.filerecovery.photorecovery.utilts;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferenceUtils {
    private static SharePreferenceUtils instance;
    private SharedPreferences.Editor editor = this.pre.edit();
    private Context mContext;
    private SharedPreferences pre;

    private SharePreferenceUtils(Context context) {
        this.mContext = context;
        this.pre = context.getSharedPreferences("data_app", 0);
    }

    public static SharePreferenceUtils getInstance(Context context) {
        if (instance == null) {
            instance = new SharePreferenceUtils(context);
        }
        return instance;
    }

    public boolean getFirstRun() {
        boolean z = this.pre.getBoolean("first_run_app", true);
        if (z) {
            this.editor.putBoolean("first_run_app", false);
            this.editor.commit();
        }
        return z;
    }

    public String getLanguage() {
        return this.pre.getString("language", "en");
    }

    public void saveLanguage(String str) {
        this.editor.putString("language", str);
        this.editor.commit();
    }

    public int getLanguageIndex() {
        return this.pre.getInt("languageindex", 0);
    }

    public void saveLanguageIndex(int i) {
        this.editor.putInt("languageindex", i);
        this.editor.commit();
    }
}
