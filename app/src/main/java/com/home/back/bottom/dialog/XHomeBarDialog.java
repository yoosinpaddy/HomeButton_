package com.home.back.bottom.dialog;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;


import com.home.back.bottom.R;
import com.home.back.bottom.util.PreferencesUtils;

public class XHomeBarDialog extends DialogFragment {
    private static final String TAG = DialogFragment.class.getSimpleName();
    private static final String X_HOME_BAR_PACKAGE = "com.lagache.sylvain.xhomebar";

    public static XHomeBarDialog createInstance() {
        return new XHomeBarDialog();
    }

    public Dialog onCreateDialog(Bundle bundle) {
        PreferencesUtils.savePref(PreferencesUtils.PREF_X_HOME_BAR_SHOWN, true);
        Dialog onCreateDialog = super.onCreateDialog(bundle);
        onCreateDialog.requestWindowFeature(1);
        onCreateDialog.setContentView(initViews());
        return onCreateDialog;
    }

    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, TAG);
    }

    private View initViews() {
        View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_x_home_bar, null);
        inflate.findViewById(R.id.dismiss_button).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                XHomeBarDialog.this.dismiss();
            }
        });
        inflate.findViewById(R.id.playstore_button).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                XHomeBarDialog.this.startPlayStore();
                XHomeBarDialog.this.dismiss();
            }
        });
        return inflate;
    }


    public void startPlayStore() {
        try {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=com.lagache.sylvain.xhomebar")));
        } catch (ActivityNotFoundException unused) {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=com.lagache.sylvain.xhomebar")));
        }
    }
}
