package com.home.back.bottom.service;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.os.Build.VERSION;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import com.home.back.bottom.R;


public class AccessibilityActionService extends AccessibilityService {
    public static final int ACTION_BACK = 0;
    public static final int ACTION_HOME = 4;
    public static final int ACTION_MULTITASK = 1;
    public static final int ACTION_NOTIFICATION = 2;
    public static final int ACTION_POWER_DIALOG = 5;
    public static final int ACTION_QUICK_SETTINGS = 3;
    public static final int ACTION_SPLIT_SCREEN = 6;
    public static final String EXTRA_ACTION = "EXTRA_ACTION";
    private static final String TAG = "BackActionService";

    public void onCreate() {
        Log.d(TAG, "onCreate");
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        Log.d(TAG, "onStartCommand");
        performAction(intent.getIntExtra(EXTRA_ACTION, 0));
        stopSelf();
        return super.onStartCommand(intent, i, i2);
    }


    public void onServiceConnected() {
        super.onServiceConnected();
        Log.d(TAG, "onServiceConnected");
    }

    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        Log.d(TAG, "onAccessibilityEvent");
    }

    public void onInterrupt() {
        Log.d(TAG, "onInterrupt");
    }

    private void performAction(int i) {
        if (VERSION.SDK_INT >= 16) {
            switch (i) {
                case 0:
                    performGlobalAction(1);
                    return;
                case 1:
                    performGlobalAction(3);
                    return;
                case 2:
                    performGlobalAction(4);
                    return;
                case 3:
                    if (VERSION.SDK_INT >= 17) {
                        performGlobalAction(5);
                        return;
                    } else {
                        Toast.makeText(this, getString(R.string.action_not_available_version_jb_mr1), 1).show();
                        return;
                    }
                case 4:
                    performGlobalAction(2);
                    return;
                case 5:
                    if (VERSION.SDK_INT >= 21) {
                        performGlobalAction(6);
                        return;
                    } else {
                        Toast.makeText(this, getString(R.string.action_not_available_version_lollipop), 1).show();
                        return;
                    }
                case 6:
                    if (VERSION.SDK_INT >= 24) {
                        performGlobalAction(7);
                        return;
                    } else {
                        Toast.makeText(this, getString(R.string.action_not_available_version_nougat), 1).show();
                        return;
                    }
                default:
                    return;
            }
        }
    }
}
