package com.home.back.bottom.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import androidx.annotation.Nullable;

import com.home.back.bottom.util.PreferencesUtils;

public class DeactivateService extends Service {
    public static final String BUNDLE_CENTER_ACTIVE = "BUNDLE_CENTER_ACTIVE";
    public static final String BUNDLE_LEFT_ACTIVE = "BUNDLE_LEFT_ACTIVE";
    public static final String BUNDLE_RIGHT_ACTIVE = "BUNDLE_RIGHT_ACTIVE";
    public static final int DEACTIVATE_10_SEC = 1;
    public static final String EXTRA_ORDER = "EXTRA_ORDER";
    public static final int FULL_DEACTIVATE = 0;
    private static final String TAG = "DeactivateService";
    private Handler handler;
    private Intent overlayService;

    public SharedPreferences prefs;

    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        if (intent != null) {
            init();
            if (intent.getIntExtra(EXTRA_ORDER, 0) == 1) {
                deactivateWithTime(10);
            } else {
                fullyDeactivate();
            }
        }
        return super.onStartCommand(intent, i, i2);
    }

    private void init() {
        prefs = getSharedPreferences("com.home.button.bottom", 0);
        overlayService = new Intent(this, ButtonOverlayService.class);
        handler = new Handler() {
            public void handleMessage(Message message) {
                super.handleMessage(message);
                Bundle data = message.getData();
                prefs.edit().putBoolean(PreferencesUtils.PREF_SERVICE_ACTIVE, data.getBoolean(DeactivateService.BUNDLE_CENTER_ACTIVE)).apply();
                prefs.edit().putBoolean("left_serviceActive", data.getBoolean(DeactivateService.BUNDLE_LEFT_ACTIVE)).apply();
                prefs.edit().putBoolean("right_serviceActive", data.getBoolean(DeactivateService.BUNDLE_RIGHT_ACTIVE)).apply();
                startService();
            }
        };
    }

    private void deactivateWithTime(int i) {
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putBoolean(BUNDLE_LEFT_ACTIVE, prefs.getBoolean("left_serviceActive", false));
        bundle.putBoolean(BUNDLE_CENTER_ACTIVE, prefs.getBoolean(PreferencesUtils.PREF_SERVICE_ACTIVE, true));
        bundle.putBoolean(BUNDLE_RIGHT_ACTIVE, prefs.getBoolean("right_serviceActive", false));
        message.setData(bundle);
        prefs.edit().putBoolean(PreferencesUtils.PREF_SERVICE_ACTIVE, false).apply();
        prefs.edit().putBoolean("left_serviceActive", false).apply();
        prefs.edit().putBoolean("right_serviceActive", false).apply();
        handler.sendMessageDelayed(message, (long) (i * 1000));
        stopService();
    }

    private void stopService() {
        try {
            stopService(overlayService);
        } catch (Exception e) {
            Log.w(TAG, e);
        }
    }


    public void startService() {
        Log.d(TAG, "startService");
        startService(overlayService);
    }

    private void fullyDeactivate() {
        prefs.edit().putBoolean(PreferencesUtils.PREF_SERVICE_ACTIVE, false).apply();
        prefs.edit().putBoolean("left_serviceActive", false).apply();
        prefs.edit().putBoolean("right_serviceActive", false).apply();
        stopService();
    }
}
