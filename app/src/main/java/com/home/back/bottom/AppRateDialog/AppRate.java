package com.home.back.bottom.AppRateDialog;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.Pair;

import androidx.appcompat.app.AppCompatActivity;

import com.home.back.bottom.AppRateDialog.helpers.IntentHelper;
import com.home.back.bottom.AppRateDialog.helpers.PreferencesHelper;
import com.home.back.bottom.R;

import java.util.ArrayList;
import java.util.List;

public class AppRate {
    private static final String TAG = "AppRate";
    private static AppRate singleton;
    private String appPackage;
    private List<Pair<String, Intent>> badRateIntents;
    private final Context context;
    private List<Pair<String, Intent>> goodRateIntents;

    private AppRate(Context context2) {
        this.context = context2.getApplicationContext();
    }

    public static AppRate with(Context context2) {
        if (singleton == null) {
            synchronized (AppRate.class) {
                if (singleton == null) {
                    singleton = new AppRate(context2);
                }
            }
        }
        singleton.badRateIntents = new ArrayList();
        singleton.goodRateIntents = new ArrayList();
        return singleton;
    }

    public AppRate setFirstShow(int i) {
        PreferencesHelper.saveFirstShow(this.context, i);
        if (PreferencesHelper.getNextTimeOpen(this.context) == -1) {
            PreferencesHelper.saveNextTimeOpen(this.context, i);
        }
        return this;
    }

    public AppRate setShowInterval(int i) {
        int showInterval = PreferencesHelper.getShowInterval(this.context);
        int calculatedInterval = PreferencesHelper.getCalculatedInterval(this.context);
        if (showInterval != i || calculatedInterval == -1) {
            PreferencesHelper.saveShowInterval(this.context, i);
            PreferencesHelper.saveCalculatedInterval(this.context, i);
        }
        return this;
    }

    public AppRate setIntervalMultiplier(float f) {
        PreferencesHelper.saveShowMultiplier(this.context, f);
        return this;
    }

    public AppRate setAppPackage(String str) {
        this.appPackage = str;
        this.goodRateIntents.add(new Pair(this.context.getString(R.string.rate_dialog_playstore_button), IntentHelper.getPlayStoreIntent(str)));
        return this;
    }

    public AppRate addEmailButton(String str, String str2) {
        this.badRateIntents.add(new Pair(this.context.getString(R.string.rate_dialog_suggestion_button), IntentHelper.getEmailIntent(str, str2)));
        return this;
    }

    public AppRate addGoodRateButton(String str, Intent intent) {
        this.goodRateIntents.add(new Pair(str, intent));
        return this;
    }

    public AppRate addBadRateButton(String str, Intent intent) {
        this.badRateIntents.add(new Pair(str, intent));
        return this;
    }

    public static boolean shouldShowDialog(Context context2) {
        boolean z = false;
        if (PreferencesHelper.getNeverShowAgain(context2)) {
            return false;
        }
        if (PreferencesHelper.getIncrement(context2) >= PreferencesHelper.getNextTimeOpen(context2)) {
            z = true;
        }
        return z;
    }

    private static void incrementCurrentState(Context context2) {
        PreferencesHelper.saveIncrement(context2, PreferencesHelper.getIncrement(context2) + 1);
    }

    public static void showDialogIfNeeded(AppCompatActivity appCompatActivity, String str, String str2, String str3, String str4) {
        if (shouldShowDialog(appCompatActivity)) {
            showDialog(appCompatActivity, str, str2, str3, str4);
            calculateNextShow(appCompatActivity);
        }
        incrementCurrentState(appCompatActivity);
    }

    public static void showDialogIfNeeded(AppCompatActivity appCompatActivity) {
        logEverything(appCompatActivity);
        if (shouldShowDialog(appCompatActivity)) {
            showDialog(appCompatActivity);
            calculateNextShow(appCompatActivity);
        }
        incrementCurrentState(appCompatActivity);
    }

    private static void showDialog(AppCompatActivity appCompatActivity, String str, String str2, String str3, String str4) {
        RateDialogFragment newInstance = RateDialogFragment.newInstance(str, str2, str3, str4);
        if (appCompatActivity.getSupportFragmentManager().findFragmentByTag(RateDialogFragment.TAG) == null) {
            newInstance.show(appCompatActivity.getSupportFragmentManager(), RateDialogFragment.TAG);
        }
    }

    private static void showDialog(AppCompatActivity appCompatActivity) {
        RateDialogFragment newInstance = RateDialogFragment.newInstance();
        if (appCompatActivity.getSupportFragmentManager().findFragmentByTag(RateDialogFragment.TAG) == null) {
            newInstance.show(appCompatActivity.getSupportFragmentManager(), RateDialogFragment.TAG);
        }
    }

    private static void calculateNextShow(Context context2) {
        float showMultiplier = PreferencesHelper.getShowMultiplier(context2);
        int calculatedInterval = (int) (((float) PreferencesHelper.getCalculatedInterval(context2)) * showMultiplier);
        int increment = PreferencesHelper.getIncrement(context2) + calculatedInterval;
        PreferencesHelper.saveCalculatedInterval(context2, calculatedInterval);
        PreferencesHelper.saveNextTimeOpen(context2, increment);
    }

    public static void resetValues(Context context2) {
        int showInterval = PreferencesHelper.getShowInterval(context2);
        int firstShow = PreferencesHelper.getFirstShow(context2);
        PreferencesHelper.saveCalculatedInterval(context2, showInterval);
        PreferencesHelper.saveNextTimeOpen(context2, firstShow);
        PreferencesHelper.saveIncrement(context2, 1);
        PreferencesHelper.saveNeverShowAgain(context2, false);
    }

    private static void logEverything(Context context2) {
        float showMultiplier = PreferencesHelper.getShowMultiplier(context2);
        int increment = PreferencesHelper.getIncrement(context2);
        int showInterval = PreferencesHelper.getShowInterval(context2);
        int calculatedInterval = PreferencesHelper.getCalculatedInterval(context2);
        boolean neverShowAgain = PreferencesHelper.getNeverShowAgain(context2);
        int firstShow = PreferencesHelper.getFirstShow(context2);
        int nextTimeOpen = PreferencesHelper.getNextTimeOpen(context2);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("firstShow = ");
        sb.append(firstShow);
        Log.d(str, sb.toString());
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("currentState = ");
        sb2.append(increment);
        Log.d(str2, sb2.toString());
        String str3 = TAG;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("interval = ");
        sb3.append(showInterval);
        Log.d(str3, sb3.toString());
        String str4 = TAG;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("calculatedInterval = ");
        sb4.append(calculatedInterval);
        Log.d(str4, sb4.toString());
        String str5 = TAG;
        StringBuilder sb5 = new StringBuilder();
        sb5.append("nextTimeOpen = ");
        sb5.append(nextTimeOpen);
        Log.d(str5, sb5.toString());
        String str6 = TAG;
        StringBuilder sb6 = new StringBuilder();
        sb6.append("multiplier = ");
        sb6.append(showMultiplier);
        Log.d(str6, sb6.toString());
        String str7 = TAG;
        StringBuilder sb7 = new StringBuilder();
        sb7.append("neverShowAgain = ");
        sb7.append(neverShowAgain);
        Log.d(str7, sb7.toString());
    }

    static void saveNeverShowAgain(boolean z) {
        PreferencesHelper.saveNeverShowAgain(singleton.context, z);
    }

    static List<Pair<String, Intent>> getGoodRateIntents() {
        return singleton.goodRateIntents;
    }

    static List<Pair<String, Intent>> getBadRateIntents() {
        return singleton.badRateIntents;
    }
}
