package com.home.back.bottom.AppRateDialog.helpers;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;

public class IntentHelper {
    public static Intent getEmailIntent(String str, String str2) {
        Intent intent = new Intent("android.intent.action.SENDTO", Uri.fromParts("mailto", str, null));
        intent.putExtra("android.intent.extra.SUBJECT", str2);
        Intent createChooser = Intent.createChooser(intent, null);
        createChooser.addFlags(268435456);
        return createChooser;
    }

    public static Intent getPlayStoreIntent(String str) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("market://details?id=");
            sb.append(str);
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(sb.toString()));
            intent.addFlags(268435456);
            return intent;
        } catch (ActivityNotFoundException unused) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("https://play.google.com/store/apps/details?id=");
            sb2.append(str);
            Intent intent2 = new Intent("android.intent.action.VIEW", Uri.parse(sb2.toString()));
            intent2.addFlags(268435456);
            return intent2;
        }
    }
}
