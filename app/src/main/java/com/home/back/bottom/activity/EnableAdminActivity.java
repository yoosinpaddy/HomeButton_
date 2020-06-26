package com.home.back.bottom.activity;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;

import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.home.back.bottom.R;
import com.home.back.bottom.broadcast.reciever.LockScreenAdmin;


import java.util.Objects;

public class EnableAdminActivity extends AppCompatActivity {
    private static final int ASK_ADMIN = 99;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_enable_admin);
        if (VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }
        findViewById(R.id.admin_button).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                requestAdminRights();
            }
        });
        TextView textView = (TextView) findViewById(R.id.admin_textview);
        if (VERSION.SDK_INT >= 24) {
            textView.setText(Html.fromHtml(getString(R.string.lock_screen_admin_instructions), 0));
        } else {
            textView.setText(Html.fromHtml(getString(R.string.lock_screen_admin_instructions)));
        }
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return false;
        }
        onBackPressed();
        return true;
    }


    public void onResume() {
        super.onResume();
        if (isAdminGranted()) {
            finish();
        }
    }


    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (isAdminGranted()) {
            finish();
        }
    }

    private boolean isAdminGranted() {
        return ((DevicePolicyManager) getSystemService("device_policy")).isAdminActive(new ComponentName(this, LockScreenAdmin.class));
    }


    public void requestAdminRights() {
        ComponentName componentName = new ComponentName(this, LockScreenAdmin.class);
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.DeviceAdminAdd"));
        intent.putExtra("android.app.extra.DEVICE_ADMIN", componentName);
        intent.putExtra("android.app.extra.ADD_EXPLANATION", getString(R.string.lock_screen_admin_description));
        try {
            startActivityForResult(intent, 99);
        } catch (SecurityException unused) {
            Snackbar make = Snackbar.make(findViewById(16908290), (int) R.string.admin_right_not_available, 0);
            ((TextView) make.getView().findViewById(R.id.snackbar_text)).setMaxLines(4);
            make.show();
        }
    }
}
