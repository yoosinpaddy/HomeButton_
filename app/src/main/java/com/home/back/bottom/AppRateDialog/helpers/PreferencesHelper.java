package com.home.back.bottom.AppRateDialog.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.home.back.bottom.AppRateDialog.utils.Constants;

public class PreferencesHelper {
    private static final String PACKAGE_NAME = "com.lagache.sylvain.library.ratedialog";

    private PreferencesHelper() {
    }

    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PACKAGE_NAME, 0);
    }

    public static Editor getPreferencesEditor(Context context) {
        return getPreferences(context).edit();
    }

    public static void saveFirstShow(Context context, int i) {
        getPreferencesEditor(context).putInt(Constants.PREF_FIRST_SHOW, i).apply();
    }

    public static void saveShowInterval(Context context, int i) {
        getPreferencesEditor(context).putInt(Constants.PREF_SHOW_INTERVAL, i).apply();
    }

    public static void saveCalculatedInterval(Context context, int i) {
        getPreferencesEditor(context).putInt(Constants.PREF_CALCULATED_INTERVAL, i).apply();
    }

    public static void saveShowMultiplier(Context context, float f) {
        getPreferencesEditor(context).putFloat(Constants.PREF_SHOW_MULTIPLIER, f).apply();
    }

    public static void saveIncrement(Context context, int i) {
        getPreferencesEditor(context).putInt(Constants.PREF_INCREMENT, i).apply();
    }

    public static void saveNextTimeOpen(Context context, int i) {
        getPreferencesEditor(context).putInt(Constants.PREF_NEXT_TIME_OPEN, i).apply();
    }

    public static void saveNeverShowAgain(Context context, boolean z) {
        getPreferencesEditor(context).putBoolean(Constants.PREF_NEVER_SHOW_AGAIN, z).apply();
    }

    public static int getFirstShow(Context context) {
        return getPreferences(context).getInt(Constants.PREF_FIRST_SHOW, 3);
    }

    public static int getShowInterval(Context context) {
        return getPreferences(context).getInt(Constants.PREF_SHOW_INTERVAL, 3);
    }

    public static int getCalculatedInterval(Context context) {
        return getPreferences(context).getInt(Constants.PREF_CALCULATED_INTERVAL, -1);
    }

    public static float getShowMultiplier(Context context) {
        return getPreferences(context).getFloat(Constants.PREF_SHOW_MULTIPLIER, 1.0f);
    }

    public static int getIncrement(Context context) {
        return getPreferences(context).getInt(Constants.PREF_INCREMENT, 1);
    }

    public static int getNextTimeOpen(Context context) {
        return getPreferences(context).getInt(Constants.PREF_NEXT_TIME_OPEN, -1);
    }

    public static boolean getNeverShowAgain(Context context) {
        return getPreferences(context).getBoolean(Constants.PREF_NEVER_SHOW_AGAIN, false);
    }
}
