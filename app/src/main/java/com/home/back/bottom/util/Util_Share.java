package com.home.back.bottom.util;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ListView;

import com.google.android.gms.ads.AdListener;

import com.home.back.bottom.mainapplication;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Util_Share {

    public static boolean SHORTCUT_FLAG = false;
    public static Boolean remote = false;
    public static String key_tvname = "key_tv_name";
    public static String key_tvindex = "key_tv_index";
    public static String key_save_remote = "key_remote_save";
    public static int font_position = 0;
    public static int flagitem = 0;
    public static String playername;
    public static int remote_size = 0;
    public static JSONArray remote_json=new JSONArray();
    public static int screenWidth;
    public static int screenHeight;
    public static Integer remote_id=0;
    public static JSONObject currentremote;
    public static boolean isdefault = false;
    public static List<Action> actions=new ArrayList<>();
    public static int choice=-1;
    public static String position="position";
    public static String click_action="click_action";


    public static String convertProntoHexStringToIntString(String s) {
        String[] codes = s.split(" ");
        StringBuilder sb = new StringBuilder();
        sb.append(new StringBuilder(String.valueOf(getFrequency(codes[1]))).append(",").toString());
        for (int i = 4; i < codes.length; i++) {
            sb.append(new StringBuilder(String.valueOf(Integer.parseInt(codes[i], 16))).append(",").toString());


        }
        return sb.toString();
    }

    public static boolean isNeedToAdShow(Context context) {
        boolean isNeedToShow = true;

        try{
            if (!Util_SharedPrefs.contain(context, Util_SharedPrefs.IS_ADS_REMOVED)) {
                isNeedToShow = true;
            } else {
                if (!Util_SharedPrefs.getBoolean(context, Util_SharedPrefs.IS_ADS_REMOVED))
                    isNeedToShow = true;

                else
                    isNeedToShow = false;
            }
        }catch (Exception e){
            Log.e("EXCEPTION", "isNeedToAdShow: "+e.getLocalizedMessage());
        }

        return isNeedToShow;
    }


    public static String getFrequency(String s) {
        return Integer.valueOf((int) (1000000.0d / (((double) Integer.parseInt(s, 16)) * 0.241246d))).toString();
    }



    public static void RestartApp(Activity activity) {

        Intent i = activity.getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(activity.getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(i);

    }


    public static void loadGoogleInterstitial(final Activity activity, final Intent intent, final boolean isNeedToFinish, final String simpleName) {
        try {
            if (Util_Share.isNeedToAdShow(activity)) {

                if (!mainapplication.getInstance().requestNewInterstitial()) {

                    if (intent != null)
                        activity.startActivity(intent);

                    if (isNeedToFinish)
                        activity.finish();

                } else {
                    mainapplication.getInstance().mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();
                            mainapplication.getInstance().mInterstitialAd.setAdListener(null);
                            mainapplication.getInstance().mInterstitialAd = null;
                            mainapplication.getInstance().ins_adRequest = null;
                            mainapplication.getInstance().LoadAds(simpleName);

                            if (intent != null)
                                activity.startActivity(intent);

                            if (isNeedToFinish)
                                activity.finish();
                        }

                        @Override
                        public void onAdFailedToLoad(int i) {
                            super.onAdFailedToLoad(i);
                        }

                        @Override
                        public void onAdLoaded() {
                            super.onAdLoaded();
                        }
                    });
                }
            }
            else{

                if (intent != null)
                    activity.startActivity(intent);

                if (isNeedToFinish)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void loadGoogleAds(Context context, String simpleName) {
        if (Util_Share.isNeedToAdShow(context)) {
            if (!mainapplication.getInstance().isLoaded()) {
                mainapplication.getInstance().LoadAds(simpleName);
            }
        }
    }
}
