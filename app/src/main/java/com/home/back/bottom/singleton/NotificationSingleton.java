package com.home.back.bottom.singleton;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build.VERSION;

import com.home.back.bottom.R;
import com.home.back.bottom.activity.MainActivity;
import com.home.back.bottom.util.PreferencesUtils;

public class NotificationSingleton {
    private static final int IDNOTIFICATION = 666;
    private static NotificationSingleton instance;

    private void sendOldNotification(Context context) {
    }

    private NotificationSingleton() {
    }

    public static NotificationSingleton getInstance() {
        if (instance == null) {
            instance = new NotificationSingleton();
        }
        return instance;
    }

    public void sendNotification(Context context, boolean z) {
        if (VERSION.SDK_INT >= 17) {
            sendNotificationJellyBean(context, z);
        } else {
            sendOldNotification(context);
        }
    }

    public void cancelNotification(Context context) {
        ((NotificationManager) context.getSystemService(PreferencesUtils.PREF_NOTIFICATION_ENABLE)).cancel(IDNOTIFICATION);
    }

    @SuppressLint({"NewApi"})
    @TargetApi(16)
    private void sendNotificationJellyBean(Context context, boolean z) {
        Notification notification;
        PendingIntent activity = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);
        if (z) {
            notification = new Builder(context).setContentTitle(context.getString(R.string.home_button)).setContentText(context.getString(R.string.home_button_activated)).setSmallIcon(R.drawable.ic_status).setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher)).setContentIntent(activity).setAutoCancel(false).setOngoing(true).setPriority(2).build();
        } else {
            notification = new Builder(context).setContentTitle(context.getString(R.string.home_button)).setContentText(context.getString(R.string.home_button_activated)).setSmallIcon(17170445).setContentIntent(activity).setAutoCancel(false).setOngoing(true).setPriority(-2).build();
        }
        ((NotificationManager) context.getSystemService(PreferencesUtils.PREF_NOTIFICATION_ENABLE)).notify(IDNOTIFICATION, notification);
    }
}
