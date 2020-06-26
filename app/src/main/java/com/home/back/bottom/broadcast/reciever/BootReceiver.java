package com.home.back.bottom.broadcast.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.home.back.bottom.service.ButtonOverlayService;
import com.home.back.bottom.util.PreferencesUtils;

public class BootReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.home.button.bottom", 0);
        if (sharedPreferences.getBoolean(PreferencesUtils.PREF_SERVICE_ACTIVE, true) || sharedPreferences.getBoolean("left_serviceActive", false) || sharedPreferences.getBoolean("right_serviceActive", false)) {
            context.startService(new Intent(context, ButtonOverlayService.class));
        }
    }
}
