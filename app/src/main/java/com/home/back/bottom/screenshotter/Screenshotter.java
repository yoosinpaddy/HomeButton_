package com.home.back.bottom.screenshotter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.ImageReader.OnImageAvailableListener;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.nio.Buffer;

@RequiresApi(api = 21)
public class Screenshotter implements OnImageAvailableListener {
    private static final String TAG = "LibScreenshotter";
    private static Screenshotter mInstance;

    /* renamed from: cb */
    private ScreenshotCallback f52cb;
    private Context context;
    private Intent data;
    private int height;
    private volatile int imageAvailable = 0;
    private ImageReader mImageReader;
    private MediaProjection mMediaProjection;
    private int resultCode;
    private VirtualDisplay virtualDisplay;
    private int width;

    public static Screenshotter getInstance() {
        if (mInstance == null) {
            mInstance = new Screenshotter();
        }
        return mInstance;
    }

    private Screenshotter() {
    }

    public Screenshotter takeScreenshot(Context context2, int i, Intent intent, ScreenshotCallback screenshotCallback) {
        this.context = context2;
        this.f52cb = screenshotCallback;
        this.resultCode = i;
        this.data = intent;
        this.imageAvailable = 0;
        this.mImageReader = ImageReader.newInstance(this.width, this.height, 1, 2);
        MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) context2.getSystemService("media_projection");
        if (this.mMediaProjection == null) {
            this.mMediaProjection = mediaProjectionManager.getMediaProjection(this.resultCode, this.data);
            if (this.mMediaProjection == null) {
                Log.e(TAG, "MediaProjection null. Cannot take the screenshot.");
            }
        }
        try {
            this.virtualDisplay = this.mMediaProjection.createVirtualDisplay("Screenshotter", this.width, this.height, 50, 16, this.mImageReader.getSurface(), null, null);
            this.mImageReader.setOnImageAvailableListener(this, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public Screenshotter setSize(int i, int i2) {
        this.width = i;
        this.height = i2;
        return this;
    }

    public void onImageAvailable(ImageReader imageReader) {
        synchronized (this) {
            this.imageAvailable++;
            if (this.imageAvailable != 2) {
                Image acquireLatestImage = imageReader.acquireLatestImage();
                if (acquireLatestImage != null) {
                    Image.Plane[] planes=acquireLatestImage.getPlanes();
                    Buffer buffers=planes[0].getBuffer().rewind();
                    int pixelStride=planes[0].getPixelStride();
                    Bitmap bitmap=Bitmap.createBitmap(width + ((planes[0].getRowStride() - (pixelStride * width)) / pixelStride), height, android.graphics.Bitmap.Config.ARGB_8888);
                    bitmap.copyPixelsFromBuffer(buffers);
                    tearDown();
                    acquireLatestImage.close();
                    f52cb.onScreenshot(bitmap);
                }
            }
        }
    }
    private void tearDown() {
        this.virtualDisplay.release();
        this.mMediaProjection.stop();
        this.mMediaProjection = null;
        this.mImageReader = null;
    }
}
