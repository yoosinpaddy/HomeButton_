package com.home.back.bottom.service;

import android.app.admin.DevicePolicyManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.home.back.bottom.R;
import com.home.back.bottom.activity.EnableAccessibilityActivity;
import com.home.back.bottom.activity.EnableAdminActivity;
import com.home.back.bottom.activity.ScreenShotActivity;
import com.home.back.bottom.broadcast.reciever.LockScreenAdmin;

import com.home.back.bottom.util.PackageUtils;

import java.lang.reflect.Method;

public class ActionManager {
    private static final String TAG = "ActionManager";

    public static void startHome(Context context) {
        try {
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.HOME");
            intent.setFlags(268435456);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Log.w(TAG, e);
            Toast.makeText(context, "Home not found...", 0).show();
        }
    }

    public static void startMultitask(Context context) {
        if (VERSION.SDK_INT < 24) {
            try {
                Class cls = Class.forName("android.os.ServiceManager");
                IBinder iBinder = (IBinder) cls.getMethod("getService", new Class[]{String.class}).invoke(cls, new Object[]{"statusbar"});
                Class cls2 = Class.forName(iBinder.getInterfaceDescriptor());
                Object invoke = cls2.getClasses()[0].getMethod("asInterface", new Class[]{IBinder.class}).invoke(null, new Object[]{iBinder});
                Method method = cls2.getMethod("toggleRecentApps", new Class[0]);
                method.setAccessible(true);
                method.invoke(invoke, new Object[0]);
            } catch (Exception e) {
                Log.w(TAG, e);
            }
        } else if (PackageUtils.isAccessibilitySettingsOn(context, AccessibilityActionService.class)) {
            Intent intent = new Intent(context, AccessibilityActionService.class);
            intent.putExtra(AccessibilityActionService.EXTRA_ACTION, 1);
            context.startService(intent);
        } else {
            startAccessibilityActivity(context);
        }
    }

    public static void openNotifications(Context context) {
        Method method;
        if (VERSION.SDK_INT < 21 || (!Build.MANUFACTURER.equals("Samsung") && !Build.MANUFACTURER.equals("samsung"))) {
            try {
                Object systemService = context.getSystemService("statusbar");
                Class cls = Class.forName("android.app.StatusBarManager");
                if (VERSION.SDK_INT >= 17) {
                    method = cls.getMethod("expandNotificationsPanel", new Class[0]);
                } else {
                    method = cls.getMethod("expand", new Class[0]);
                }
                method.invoke(systemService, new Object[0]);
            } catch (Exception e) {
                Log.w(TAG, e);
            }
        } else if (PackageUtils.isAccessibilitySettingsOn(context, AccessibilityActionService.class)) {
            Intent intent = new Intent(context, AccessibilityActionService.class);
            intent.putExtra(AccessibilityActionService.EXTRA_ACTION, 2);
            context.startService(intent);
        } else {
            startAccessibilityActivity(context);
        }
    }

    public static void startApplicationEvent(Context context, String str) {
        try {
            context.startActivity(context.getPackageManager().getLaunchIntentForPackage(str));
        } catch (Exception e) {
            Toast.makeText(context, "Activity not found...", 0).show();
            Log.w(TAG, e);
        }
    }

    public static void goToSettings(Context context) {
        Intent intent = new Intent("android.settings.SETTINGS");
        intent.setFlags(1073741824);
        intent.setFlags(268435456);
        context.startActivity(intent);
    }

    public static void startBackEvent(Context context) {
        if (PackageUtils.isAccessibilitySettingsOn(context, AccessibilityActionService.class)) {
            Intent intent = new Intent(context, AccessibilityActionService.class);
            intent.putExtra(AccessibilityActionService.EXTRA_ACTION, 0);
            context.startService(intent);
            return;
        }
        startAccessibilityActivity(context);
    }

    public static void lockScreen(Context context) {
        if (isAdminGranted(context)) {
            ((DevicePolicyManager) context.getSystemService("device_policy")).lockNow();
        } else {
            startAdminActivity(context);
        }
    }

    public static void takeScreenShot(Context context) {
        Intent intent = new Intent(context, ScreenShotActivity.class);
        intent.setFlags(1073741824);
        intent.setFlags(268435456);
        context.startActivity(intent);
    }

    public static void showQuickSettings(Context context) {
        if (PackageUtils.isAccessibilitySettingsOn(context, AccessibilityActionService.class)) {
            Intent intent = new Intent(context, AccessibilityActionService.class);
            intent.putExtra(AccessibilityActionService.EXTRA_ACTION, 3);
            context.startService(intent);
            return;
        }
        startAccessibilityActivity(context);
    }

    public static void showPowerMenu(Context context) {
        if (PackageUtils.isAccessibilitySettingsOn(context, AccessibilityActionService.class)) {
            Intent intent = new Intent(context, AccessibilityActionService.class);
            intent.putExtra(AccessibilityActionService.EXTRA_ACTION, 5);
            context.startService(intent);
            return;
        }
        startAccessibilityActivity(context);
    }

    public static void startSplitScreen(Context context) {
        if (PackageUtils.isAccessibilitySettingsOn(context, AccessibilityActionService.class)) {
            Intent intent = new Intent(context, AccessibilityActionService.class);
            intent.putExtra(AccessibilityActionService.EXTRA_ACTION, 6);
            context.startService(intent);
            return;
        }
        startAccessibilityActivity(context);
    }

    public static void openGoogleAssistant(Context context) {
        try {
            context.startActivity(new Intent("android.intent.action.VOICE_COMMAND").setFlags(268435456));
        } catch (Exception unused) {
            Toast.makeText(context, R.string.google_assistant_not_found, 0).show();
        }
    }

    public static void startTaskManager2X(final Context context) {
        startMultitask(context);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                ActionManager.startMultitask(context);
            }
        }, 250);
    }

    private static void startAccessibilityActivity(Context context) {
        Intent intent = new Intent(context, EnableAccessibilityActivity.class);
        intent.addFlags(268435456);
        context.startActivity(intent);
    }

    private static void startAdminActivity(Context context) {
        Intent intent = new Intent(context, EnableAdminActivity.class);
        intent.addFlags(268435456);
        context.startActivity(intent);
    }
    private static boolean isAdminGranted(Context context) {

        return ((DevicePolicyManager) context.getSystemService("device_policy")).isAdminActive(new ComponentName(context, LockScreenAdmin.class));
    }
}
