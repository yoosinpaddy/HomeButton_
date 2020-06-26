package com.home.back.bottom.activity;

import android.os.Bundle;

import android.view.MenuItem;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.home.back.bottom.R;


public class WebviewActivity extends AppCompatActivity {
    public static final String BUNDLE_FILE_NAME = "file_name";
    public static final String BUNDLE_TITLE = "title";


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_webview);
        if (getIntent().getExtras() != null) {
            loadContent(getIntent().getExtras().getString(BUNDLE_TITLE), getIntent().getExtras().getString(BUNDLE_FILE_NAME));
        }
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void loadContent(String str, String str2) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle((CharSequence) str);
        WebView webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(false);
        StringBuilder sb = new StringBuilder();
        sb.append("file:///android_asset/htmls/");
        sb.append(str2);
        webView.loadUrl(sb.toString());
    }
}
