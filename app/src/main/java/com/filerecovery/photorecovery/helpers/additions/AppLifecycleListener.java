package com.filerecovery.photorecovery.helpers.additions;

import android.content.Context;
import android.util.Log;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import com.filerecovery.photorecovery.conf.MyProperties;

public class AppLifecycleListener implements LifecycleObserver {
    private boolean CallDoneFromOnCreate = false;
    private Context ctx;

    public AppLifecycleListener(Context context) {
        this.ctx = context;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate() {
        Log.i("AppCy", "onCreate");
        this.CallDoneFromOnCreate = true;
        ActionONStartOrComeFromBackground();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onMoveToForeground() {
        Log.i("AppCy", "Go Foreground");
        MyProperties.getInstance().AppInForeground = true;
        if (!this.CallDoneFromOnCreate) {
            ActionONStartOrComeFromBackground();
        } else {
            this.CallDoneFromOnCreate = false;
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onMoveToBackground() {
        Log.i("AppCy", "Go Background");
        MyProperties.getInstance().AppInForeground = false;
        MyPreferenceManager.putString(this.ctx, "TimeForGoBackground", Long.valueOf(System.currentTimeMillis() / 1000).toString());
    }

    private void DailyFunctions() {
        if (MyPreferenceManager.getString(this.ctx, "DailyFunctions") == null || MyPreferenceManager.getString(this.ctx, "DailyFunctions").isEmpty()) {
            MyPreferenceManager.putString(this.ctx, "DailyFunctions", String.valueOf(System.currentTimeMillis() / 1000));
            return;
        }
        Long valueOf = Long.valueOf(Long.parseLong(MyPreferenceManager.getString(this.ctx, "DailyFunctions")));
        Long valueOf2 = Long.valueOf(System.currentTimeMillis() / 1000);
        if (valueOf2.longValue() - valueOf.longValue() > 86400) {
            MyPreferenceManager.putString(this.ctx, "DailyFunctions", String.valueOf(valueOf2));
            CheckCurrentVersion();
            CheckForActionsAfter7Days(valueOf2.longValue() - valueOf.longValue() > 604800 ? 7 : 0);
            RefreshNotificationDeviceToken();
        }
    }

    private void CheckCurrentVersion() {
        MyPreferenceManager.putString(this.ctx, "CheckCurrentVersion", "1");
    }

    private void CheckForActionsAfter7Days(int i) {
        if (MyPreferenceManager.getString(this.ctx, "ActionsAfter7DaysTimer") == null) {
            MyPreferenceManager.putString(this.ctx, "ActionsAfter7DaysTimer", "0");
        }
        if (i == 0) {
            i = Integer.parseInt(MyPreferenceManager.getString(this.ctx, "ActionsAfter7DaysTimer"));
        }
        if (i >= 7) {
            MyPreferenceManager.putString(this.ctx, "ShowRateTheAPP", "1");
            MyPreferenceManager.putString(this.ctx, "ActionsAfter7DaysTimer", "0");
            return;
        }
        MyPreferenceManager.putString(this.ctx, "ShowRateTheAPP", "0");
        MyPreferenceManager.putString(this.ctx, "ActionsAfter7DaysTimer", String.valueOf(i + 1));
    }

    private void RefreshNotificationDeviceToken() {
        MyPreferenceManager.putString(this.ctx, "RefreshNotificationDeviceToken", "1");
    }

    private void ActionONStartOrComeFromBackground() {
        DailyFunctions();
    }
}
