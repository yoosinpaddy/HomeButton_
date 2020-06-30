package com.home.back.bottom.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.home.back.bottom.util.Util_Share;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent mainIntent = new Intent(this, MainActivity.class);
        Util_Share.loadGoogleInterstitial(this, mainIntent, true, SplashActivity.TAG);

    }
}