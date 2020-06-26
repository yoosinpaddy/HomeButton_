package com.home.back.bottom.activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;
import androidx.vectordrawable.graphics.drawable.PathInterpolatorCompat;

import com.home.back.bottom.R;
import com.home.back.bottom.screenshotter.ScreenshotCallback;
import com.home.back.bottom.screenshotter.Screenshotter;

import com.home.back.bottom.dialog.SimpleDialogFragment;
import com.home.back.bottom.service.ButtonOverlayService;
import com.home.back.bottom.util.ImageSaver;
import com.home.back.bottom.util.PreferencesUtils;
import com.home.back.bottom.util.ScreenUtils;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@RequiresApi(api = 21)
public class ScreenShotActivity extends AppCompatActivity implements OnClickListener, SimpleDialogFragment.SimpleFragmentListener {
    private static String[] PERMISSIONS_STORAGE = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
    private static final int REQUEST_EXTERNAL_STORAGE = 2;
    private static final int REQUEST_MEDIA_PROJECTION = 1;
    private static final boolean SHOW_SCREENSHOT_BUTTON = false;
    private static final String TAG = "ScreenShotActivity";
    private TextView cancelTextView;
    private Intent overlayService;
    private CardView screenshotCardView;
    private DateFormat screenshotDateFormat;
    private Button takeScreenshotButton;
    private SimpleDialogFragment warningDialog;

    public void onNegativeButtonPressed(SimpleDialogFragment simpleDialogFragment) {
    }


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_screen_shot);
        ((FrameLayout) findViewById(R.id.activity_screen_shot)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ScreenShotActivity.this.finish();
            }
        });
        this.screenshotCardView = (CardView) findViewById(R.id.screenshot_cardview);
        this.takeScreenshotButton = (Button) findViewById(R.id.take_screenshot_button);
        this.cancelTextView = (TextView) findViewById(R.id.cancel_textview);
        this.screenshotCardView.setVisibility(8);
        this.takeScreenshotButton.setVisibility(8);
        checkStoragePermissions(this);
        this.screenshotDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        stopButtonService();
    }


    public void onStart() {
        super.onStart();
    }


    public void onDestroy() {
        super.onDestroy();
        restartButtonService();
    }


    public void onActivityResult(int i, final int i2, final Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == -1) {
            new Handler() {
                public void handleMessage(Message message) {
                    super.handleMessage(message);
                    Screenshotter.getInstance().setSize(ScreenUtils.getScreenWidth(ScreenShotActivity.this), ScreenUtils.getScreenHeight(ScreenShotActivity.this)).takeScreenshot(ScreenShotActivity.this, i2, intent, new ScreenshotCallback() {
                        public void onScreenshot(Bitmap bitmap) {
                            ScreenShotActivity.this.saveBitmap(bitmap);
                            ScreenShotActivity.this.finish();
                        }
                    });
                }
            }.sendEmptyMessageDelayed(0, 500);
            return;
        }
        Toast.makeText(this, getString(R.string.record_denied), 0).show();
        finish();
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i != 2) {
            return;
        }
        if (iArr.length <= 0 || iArr[0] != 0) {
            finish();
        } else {
            checkIfWarningNeeded();
        }
    }

    public void onPositiveButtonPressed(SimpleDialogFragment simpleDialogFragment) {
        if (simpleDialogFragment == this.warningDialog) {
            PreferencesUtils.savePref(PreferencesUtils.PREF_SCREENSHOT_WARNING, false);
            this.warningDialog.dismiss();
            takeScreenshot();
        }
    }

    public void onClick(View view) {
        if (view == this.takeScreenshotButton) {
            this.screenshotCardView.setVisibility(8);
            this.takeScreenshotButton.setVisibility(8);
            checkIfWarningNeeded();
        } else if (view == this.cancelTextView) {
            finish();
        }
    }

    public void checkStoragePermissions(Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, 2);
        } else {
            checkIfWarningNeeded();
        }
    }

    private void checkIfWarningNeeded() {
        if (PreferencesUtils.getPref(PreferencesUtils.PREF_SCREENSHOT_WARNING, true)) {
            this.warningDialog = SimpleDialogFragment.createInstance(getString(R.string.screenshot_android_title), getString(R.string.screenshot_android_warning), getString(R.string.screenshot_android_ok));
            this.warningDialog.setCancelable(false);
            this.warningDialog.show(getSupportFragmentManager(), SimpleDialogFragment.TAG);
            return;
        }
        takeScreenshot();
    }

    private void stopButtonService() {
        PreferencesUtils.initPreferences(this);
        this.overlayService = new Intent(this, ButtonOverlayService.class);
        PreferencesUtils.savePref(PreferencesUtils.PREF_SERVICE_ACTIVE, false);
        try {
            stopService(this.overlayService);
        } catch (Exception e) {
            Log.w(TAG, e);
        }
    }

    private void restartButtonService() {
        PreferencesUtils.savePref(PreferencesUtils.PREF_SERVICE_ACTIVE, true);
        startService(this.overlayService);
    }

    public void takeScreenshot() {
        startActivityForResult(((MediaProjectionManager) getSystemService("media_projection")).createScreenCaptureIntent(), 1);
    }


    public void saveBitmap(Bitmap bitmap) {
        File save = new ImageSaver(this).setFileName(createFileNameWithTime()).setDirectoryName("Screenshots").setExternal(true).save(bitmap);
        showNotification(save);
        refreshGallery(save);
    }

    private String createFileNameWithTime() {
        StringBuilder sb = new StringBuilder();
        sb.append("Screenshot_");
        sb.append(this.screenshotDateFormat.format(new Date()));
        sb.append(".png");
        return sb.toString();
    }

    private void showNotification(File file) {
        Uri uri;
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        try {
            if (VERSION.SDK_INT >= 24) {
                intent.setFlags(1);
                uri = FileProvider.getUriForFile(this, "com.home.button.bottom.fileProvider", file);
                intent.setDataAndType(uri, "image/*");
            } else {
                uri = Uri.fromFile(file);
                intent.setDataAndType(uri, "image/*");
            }
            PendingIntent activity = PendingIntent.getActivity(this, 0, intent, 134217728);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, ButtonOverlayService.NOTIFICATION_CHANNEL_ID);
            builder.setAutoCancel(true).setDefaults(-1).setWhen(System.currentTimeMillis()).setSmallIcon(R.drawable.ic_image_white_24dp).setContentTitle(getString(R.string.screenshot_captured)).setContentText(uri.toString()).setStyle(new NotificationCompat.BigTextStyle().bigText(uri.toString())).setPriority(1).setDefaults(5).setContentIntent(activity);
            ((NotificationManager) getSystemService(PreferencesUtils.PREF_NOTIFICATION_ENABLE)).notify(PathInterpolatorCompat.MAX_NUM_POINTS, builder.build());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void refreshGallery(File file) {
        Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        intent.setData(Uri.fromFile(file));
        sendBroadcast(intent);
    }
}
