package com.home.back.bottom.activity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.home.back.bottom.R;
import com.home.back.bottom.util.PreferencesUtils;
import com.home.back.bottom.util.billing.IabHelper;
import com.home.back.bottom.util.billing.IabResult;
import com.home.back.bottom.util.billing.Inventory;
import com.home.back.bottom.util.billing.Purchase;

import java.util.ArrayList;

public class BillingActivity extends AppCompatActivity implements OnClickListener {
    public static final String SKU_PRO = "pro_version";
    private static final String TAG = "BillingActivity";

    public Button buyProButton;

    public TextView buyProTextView;
    private TextView errorTextView;

    public boolean hasPaidProVersion = false;
    private IabHelper.QueryInventoryFinishedListener mGotInventoryListener;
    private IabHelper mHelper;
    private IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener;
    private IabHelper.QueryInventoryFinishedListener mQueryFinishedListener;

    public TextView proContentTextView;

    public TextView proPaidTextView;

    public String proPrice;

    public ProgressBar progressBar;
    private Button retryButton;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_billing);
        initViews();
        initQueryListener();
        initInventoryListener();
        initPurchaseListener();
        setupBillingConnexion();
    }

    public void onDestroy() {
        super.onDestroy();
        IabHelper iabHelper = this.mHelper;
        if (iabHelper != null) {
            try {
                iabHelper.dispose();
            } catch (IllegalArgumentException e) {
                Log.w(TAG, e);
            }
        }
        this.mHelper = null;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }


    public void onActivityResult(int i, int i2, Intent intent) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onActivityResult(");
        sb.append(i);
        sb.append(",");
        sb.append(i2);
        sb.append(",");
        sb.append(intent);
        Log.d(str, sb.toString());
        if (!this.mHelper.handleActivityResult(i, i2, intent)) {
            super.onActivityResult(i, i2, intent);
        } else {
            Log.d(TAG, "onActivityResult handled by IABUtil.");
        }

    }

    private void initViews() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.buyProTextView = (TextView) findViewById(R.id.buy_pro_textview);
        this.proPaidTextView = (TextView) findViewById(R.id.pro_paid_textview);
        this.proContentTextView = (TextView) findViewById(R.id.pro_content_textview);
        this.errorTextView = (TextView) findViewById(R.id.error_textview);
        this.buyProButton = (Button) findViewById(R.id.buy_pro_button);
        this.retryButton = (Button) findViewById(R.id.retry_button);
        this.progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        this.buyProButton.setOnClickListener(this);
        this.retryButton.setOnClickListener(this);
        setupViews();
    }

    private void setupViews() {
        this.progressBar.setVisibility(0);
        this.buyProTextView.setVisibility(8);
        this.proPaidTextView.setVisibility(8);
        this.buyProButton.setVisibility(8);
        this.proContentTextView.setVisibility(8);
        this.errorTextView.setVisibility(8);
        this.retryButton.setVisibility(8);
    }


    public void showError() {
        this.progressBar.setVisibility(8);
        this.buyProTextView.setVisibility(8);
        this.proPaidTextView.setVisibility(8);
        this.buyProButton.setVisibility(8);
        this.proContentTextView.setVisibility(8);
        this.errorTextView.setVisibility(0);
        this.retryButton.setVisibility(0);
    }


    public void showSuccess() {
        this.progressBar.setVisibility(8);
        this.buyProTextView.setVisibility(8);
        this.buyProButton.setVisibility(8);
        this.proContentTextView.setVisibility(8);
        this.errorTextView.setVisibility(8);
        this.retryButton.setVisibility(8);
        this.proPaidTextView.setText(getString(R.string.billing_success));
        this.proPaidTextView.setVisibility(0);
    }

    private void setupBillingConnexion() {
        this.mHelper = new IabHelper(this, getString(R.string.billing_key));
        this.mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult iabResult) {
                if (!iabResult.isSuccess()) {
                    String str = BillingActivity.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Problem setting up In-app Billing: ");
                    sb.append(iabResult);
                    Log.d(str, sb.toString());
                    showError();
                    return;
                }
                String str2 = BillingActivity.TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Success setting up In-app Billing: ");
                sb2.append(iabResult);
                Log.d(str2, sb2.toString());
                loadPurchaseItems();
            }
        });
    }

    private void initQueryListener() {
        this.mQueryFinishedListener = new IabHelper.QueryInventoryFinishedListener() {
            public void onQueryInventoryFinished(IabResult iabResult, Inventory inventory) {
                if (iabResult.isFailure()) {
                    String str = BillingActivity.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Error loading purchase : ");
                    sb.append(iabResult);
                    Log.d(str, sb.toString());
                    showError();
                    return;
                }
                String str2 = BillingActivity.TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Success loading purchase : ");
                sb2.append(iabResult);
                Log.d(str2, sb2.toString());
                if(inventory!=null||inventory.getSkuDetails(BillingActivity.SKU_PRO).getPrice()!=null){
                    proPrice = inventory.getSkuDetails(BillingActivity.SKU_PRO).getPrice();
                }
                loadPurchasedItems();
            }
        };
    }

    private void initPurchaseListener() {
        this.mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
            public void onIabPurchaseFinished(IabResult iabResult, Purchase purchase) {
                if (iabResult.isFailure()) {
                    String str = BillingActivity.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Error purchasing : ");
                    sb.append(iabResult);
                    Log.d(str, sb.toString());
                    showError();
                } else if (purchase.getSku().equals(BillingActivity.SKU_PRO)) {
                    String str2 = BillingActivity.TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Success purchasing : ");
                    sb2.append(iabResult);
                    Log.d(str2, sb2.toString());
                    showSuccess();
                    PreferencesUtils.savePref(PreferencesUtils.PREF_REAL_PRO_VERSION, false);
                }
            }
        };
    }

    private void initInventoryListener() {
        this.mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
            public void onQueryInventoryFinished(IabResult iabResult, Inventory inventory) {
                if (iabResult.isFailure()) {
                    String str = BillingActivity.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Error loading inventory : ");
                    sb.append(iabResult);
                    Log.d(str, sb.toString());
                    showError();
                    return;
                }
                String str2 = BillingActivity.TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Success loading inventory : ");
                sb2.append(iabResult);
                Log.d(str2, sb2.toString());
                hasPaidProVersion = inventory.hasPurchase(BillingActivity.SKU_PRO);
                if (hasPaidProVersion) {
                    Log.d(BillingActivity.TAG, "hasPaidProVersion");
                    proPaidTextView.setVisibility(0);
                    progressBar.setVisibility(8);
                    return;
                }
                Log.d(BillingActivity.TAG, "!hasPaidProVersion");
                TextView access$800 = buyProTextView;
                StringBuilder sb3 = new StringBuilder();
                sb3.append(getString(R.string.buy_pro_for));
                sb3.append("  ");
                sb3.append(proPrice);
                access$800.setText(sb3.toString());
                proContentTextView.setVisibility(0);
                buyProTextView.setVisibility(0);
                buyProButton.setVisibility(0);
                progressBar.setVisibility(8);
            }
        };
    }


    public void loadPurchaseItems() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(SKU_PRO);
        try {
            this.mHelper.queryInventoryAsync(true, arrayList, this.mQueryFinishedListener);
        } catch ( NullPointerException e) {
            Log.w(TAG, e);
            showError();
        }
    }

    private void purchaseProVersion() {
        try {
            this.mHelper.launchPurchaseFlow(this, SKU_PRO, 10001, this.mPurchaseFinishedListener, "");
        } catch ( NullPointerException e) {
            Log.w(TAG, e);
            showError();
        }
    }


    public void loadPurchasedItems() {
        try {
            this.mHelper.queryInventoryAsync(this.mGotInventoryListener);
        } catch ( NullPointerException e) {
            Log.w(TAG, e);
            showError();
        }
    }

    public void onClick(View view) {
        if (view == this.retryButton) {
            setupViews();
            setupBillingConnexion();
        } else if (view == this.buyProButton) {
            purchaseProVersion();
        }
    }
}
