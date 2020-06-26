package com.home.back.bottom.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesUtils {
    public static final String PREF_ACTION_ON_CLICK = "actionOnClick";
    public static final String PREF_ACTION_ON_DOUBLE_CLICK = "actionOnDoubleClick";
    public static final String PREF_ACTION_ON_LONG_CLICK = "actionOnLongClick";
    public static final String PREF_APP_CLICK_NAME = "appClicName";
    public static final String PREF_APP_CLICK_PKG = "appClicPackage";
    public static final String PREF_APP_DOUBLE_CLICK_NAME = "appDoubleClicName";
    public static final String PREF_APP_DOUBLE_CLICK_PKG = "appDoubleClicPackage";
    public static final String PREF_APP_LONG_CLICK_NAME = "appLongClicName";
    public static final String PREF_APP_LONG_CLICK_PKG = "appLongClicPackage";
    public static final String PREF_BEHIND_KEYBOARD = "PREF_BEHIND_KEYBOARD";
    public static final String PREF_BUTTON_COLOR = "buttonColor";
    public static final String PREF_BUTTON_HEIGHT = "buttonHeight";
    public static final String PREF_BUTTON_POSITION = "PREF_BUTTON_POSITION";
    public static final String PREF_BUTTON_VISIBLE = "visible";
    public static final String PREF_BUTTON_WIDTH = "buttonWidth";
    public static final String PREF_FIRST_OPENED = "PREF_FIRST_OPENED";
    public static final String PREF_FIRST_RUN = "firstrun";
    public static final String PREF_FIRST_RUN_V2 = "first_run_V2";
    public static final String PREF_LEFT_MARGIN = "left_margin";
    public static final String PREF_NOTIFICATION_ENABLE = "notification";
    public static final String PREF_NOTIFICATION_ICON_VISIBLE = "notificationIconVisible";
    public static final String PREF_PRO_VERSION = "hello_world";
    public static final String PREF_REAL_PRO_VERSION = "PREF_REAL_PRO_VERSION";
    public static final String PREF_RIGHT_MARGIN = "right_margin";
    public static final String PREF_ROTATION_ENABLE = "follow_rotation";
    public static final String PREF_SCREENSHOT_WARNING = "screenshot_warning";
    public static final String PREF_SERVICE_ACTIVE = "serviceActive";
    public static final String PREF_VIBRATION_ENABLE = "vibration";
    public static final String PREF_VIBRATION_STRENGTH = "PREF_VIBRATION_STRENGTH";
    public static final String PREF_X_HOME_BAR_SHOWN = "PREF_X_HOME_BAR_SHOWN";
    private static SharedPreferences sharedPreferences;

    public static void initPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences("com.home.button.bottom", 0);
    }

    public static void savePref(String str, String str2) {
        sharedPreferences.edit().putString(str, str2).apply();
    }

    public static void savePref(String str, int i) {
        sharedPreferences.edit().putInt(str, i).apply();
    }

    public static void savePref(String str, boolean z) {
        sharedPreferences.edit().putBoolean(str, z).apply();
    }

    public static String getPref(String str, String str2) {
        return sharedPreferences.getString(str, str2);
    }

    public static int getPref(String str, int i) {
        return sharedPreferences.getInt(str, i);
    }

    public static boolean getPref(String str, boolean z) {
        return sharedPreferences.getBoolean(str, z);
    }
}
