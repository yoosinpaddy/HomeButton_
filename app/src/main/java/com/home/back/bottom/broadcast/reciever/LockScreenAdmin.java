package com.home.back.bottom.broadcast.reciever;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class LockScreenAdmin extends DeviceAdminReceiver {
    public CharSequence onDisableRequested(Context context, Intent intent) {
        return "This is an optional message to warn the user about disabling.";
    }


    public void showToast(Context context, CharSequence charSequence) {
        Toast.makeText(context, charSequence, 0).show();
    }

    public void onEnabled(Context context, Intent intent) {
        super.onEnabled(context, intent);
        showToast(context, "Sample Device Admin: enabled");
    }

    public void onDisabled(Context context, Intent intent) {
        showToast(context, "Sample Device Admin: disabled");
    }
}
