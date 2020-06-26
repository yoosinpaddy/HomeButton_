package com.home.back.bottom.helper;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

public class PackageHelper {
    public static String getVersionName(Context context) {
        String str = "";
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return str;
        }
    }
}
