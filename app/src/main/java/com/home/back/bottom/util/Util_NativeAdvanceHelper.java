package com.home.back.bottom.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.home.back.bottom.R;


import java.util.List;

public class Util_NativeAdvanceHelper {

    public static String TAG = "Ads_123";

    public static UnifiedNativeAd nativeAd;

    public static int miAdNumber = 1;

    public static void loadAd(final Context mContext, final FrameLayout frameLayout) {

        AdLoader.Builder builder;
        if (miAdNumber==1){
            builder = new AdLoader.Builder(mContext, mContext.getString(R.string.native_ad_unit_id1));
        }else if (miAdNumber==2){
            builder = new AdLoader.Builder(mContext, mContext.getString(R.string.native_ad_unit_id2));
        }else {
            builder = new AdLoader.Builder(mContext, mContext.getString(R.string.native_ad_unit_id));
        }

        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                if (nativeAd != null) {
                    nativeAd.destroy();
                }
                nativeAd = unifiedNativeAd;
                UnifiedNativeAdView adView = (UnifiedNativeAdView) ((Activity)mContext).getLayoutInflater().inflate(R.layout.layout_nativead, null);
                populateUnifiedNativeAdView(unifiedNativeAd, adView);
                frameLayout.setVisibility(View.VISIBLE);
                frameLayout.removeAllViews();
                frameLayout.addView(adView);
            }
        });
        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.i(TAG, "on Native AdFailedToLoad: " + miAdNumber + "  errorCode: "+ errorCode);
                if (miAdNumber==1){
                    miAdNumber++;
                }else if (miAdNumber==2){
                    miAdNumber = 0;
                }else {
                    miAdNumber = 1;
                }
                loadAd(mContext, frameLayout);
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.i(TAG, "on Native AdLoaded: " + miAdNumber);
                miAdNumber = 1;
            }
        }).build();


        adLoader.loadAd(new AdRequest.Builder().build());


    }


    private static void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {

        MediaView mediaView = adView.findViewById(R.id.ad_media);
        ImageView mainImageView = adView.findViewById(R.id.ad_image);

        VideoController vc = nativeAd.getVideoController();

        vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
            public void onVideoEnd() {
                super.onVideoEnd();
            }
        });

        if (vc.hasVideoContent()) {
            adView.setMediaView(mediaView);
            mainImageView.setVisibility(View.GONE);
        } else {
            adView.setImageView(mainImageView);
            mediaView.setVisibility(View.GONE);

            List<NativeAd.Image> images = nativeAd.getImages();

            if (images.size() != 0) {
                mainImageView.setImageDrawable(images.get(0).getDrawable());
            }
        }

        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));

        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        adView.setNativeAd(nativeAd);
    }




    //

    public static void loadSmallNativeAd(final Context mContext, final FrameLayout frameLayout) {


        AdLoader.Builder builder;
        if (miAdNumber==1){
            builder = new AdLoader.Builder(mContext, mContext.getString(R.string.native_ad_unit_id1));
        }else if (miAdNumber==2){
            builder = new AdLoader.Builder(mContext, mContext.getString(R.string.native_ad_unit_id2));
        }else {
            builder = new AdLoader.Builder(mContext, mContext.getString(R.string.native_ad_unit_id));
        }

        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                if (nativeAd != null) {
                    nativeAd.destroy();
                }
                nativeAd = unifiedNativeAd;
                UnifiedNativeAdView adView = (UnifiedNativeAdView) ((Activity)mContext).getLayoutInflater().inflate(R.layout.layout_nativead_small, null);
                populateUnifiedNativeAdView(unifiedNativeAd, adView);
                frameLayout.setVisibility(View.VISIBLE);
                frameLayout.removeAllViews();
                frameLayout.addView(adView);
            }
        });

        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.i(TAG, "on Native AdFailedToLoad: " + miAdNumber + "  errorCode: "+ errorCode);
                if (miAdNumber==1){
                    miAdNumber++;
                }else if (miAdNumber==2){
                    miAdNumber = 0;
                }else {
                    miAdNumber = 1;
                }
                loadSmallNativeAd(mContext, frameLayout);
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.i(TAG, "on Native AdLoaded: " + miAdNumber);
                miAdNumber = 1;
            }
        }).build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }



    public static void onDestroy() {
        if (nativeAd != null) {
            nativeAd.destroy();
        }
    }
}
