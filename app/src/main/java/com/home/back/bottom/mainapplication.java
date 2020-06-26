package com.home.back.bottom;


import android.content.Context;
import android.util.Log;


import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.home.back.bottom.util.PreferencesUtils;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.fonts.MaterialCommunityModule;
import com.joanzapata.iconify.fonts.MaterialModule;

public class mainapplication extends MultiDexApplication {
    private static mainapplication singleton;
    public InterstitialAd mInterstitialAd;

    public AdRequest ins_adRequest;
    public static mainapplication application;


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    public mainapplication() {
        application = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
        application = this;
        Iconify.with(new FontAwesomeModule()).with(new MaterialCommunityModule()).with(new MaterialModule());
        PreferencesUtils.initPreferences(this);

        try{
            MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        }catch (Exception e){
            e.printStackTrace();
        }
        mInterstitialAd = new InterstitialAd(this);
    }

    public static Context getAppContext() {
        if (application == null) {
            application = new mainapplication();
        }
        return application;
    }

    public static mainapplication getInstance() {
        return singleton;
    }

    public void LoadAds(String name) {

        try {
            mInterstitialAd = new InterstitialAd(getApplicationContext());
            Log.d("CLASSNAME", "LoadAds: =======>" + name);
            mInterstitialAd.setAdUnitId(getInstance().getApplicationContext().getResources().getString(R.string.google_inter));


            ins_adRequest = new AdRequest.Builder()
                    .addTestDevice("E1C884DCA502603520BA8689C87199AC")
                    .build();

            mInterstitialAd.loadAd(ins_adRequest);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public boolean requestNewInterstitial() {

        try {
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }


    public boolean isLoaded() {

        try {
            if (mInterstitialAd.isLoaded() && mInterstitialAd != null) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public boolean isLoading() {
        if (mInterstitialAd == null) {
            return false;
        } else {
            if (mInterstitialAd.isLoading()) {
                return true;
            } else {
                return false;
            }
        }

    }


}
