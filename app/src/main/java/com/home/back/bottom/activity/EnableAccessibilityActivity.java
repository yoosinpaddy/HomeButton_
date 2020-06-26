package com.home.back.bottom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.home.back.bottom.R;
import com.home.back.bottom.service.AccessibilityActionService;
import com.home.back.bottom.util.PackageUtils;

public class EnableAccessibilityActivity extends AppCompatActivity {

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_enable_accessibility);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViewById(R.id.accessibility_button).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                EnableAccessibilityActivity.this.requestAccessibility();
            }
        });
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
        if (isAccessibilityEnabled()) {
            finish();
        }
    }

    private boolean isAccessibilityEnabled() {
        return PackageUtils.isAccessibilitySettingsOn(this, AccessibilityActionService.class);
    }


    public void requestAccessibility() {
        startActivity(new Intent("android.settings.ACCESSIBILITY_SETTINGS"));
    }
}
