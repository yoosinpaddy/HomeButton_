package com.home.back.bottom.broadcast.reciever;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.home.back.bottom.service.ButtonOverlayService;
import com.home.back.bottom.util.PreferencesUtils;


public class RestartServiceReceiver extends BroadcastReceiver {
    private static final String TAG = "RestartServiceReceiver";
    private SharedPreferences prefs;

    public void onReceive(Context context, Intent intent) {
        if (this.prefs == null) {
            this.prefs = context.getSharedPreferences("com.home.button.bottom", 0);
        }
        if ((this.prefs.getBoolean(PreferencesUtils.PREF_SERVICE_ACTIVE, true) || this.prefs.getBoolean("left_serviceActive", false) || this.prefs.getBoolean("right_serviceActive", false)) && !isMyServiceRunning(ButtonOverlayService.class, context)) {
            context.startService(new Intent(context.getApplicationContext(), ButtonOverlayService.class));
        }
    }

    private boolean isMyServiceRunning(Class<?> cls, Context context) {
        for (RunningServiceInfo runningServiceInfo : ((ActivityManager) context.getSystemService("activity")).getRunningServices(Integer.MAX_VALUE)) {
            if (cls.getName().equals(runningServiceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
