package com.home.back.bottom.screenshotter;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLSurface;
import android.opengl.GLES20;
import android.util.Log;
import android.view.Surface;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class CodecOutputSurface implements OnFrameAvailableListener {
    private static final String TAG = "CodecOutputSurface";
    private static final boolean VERBOSE = true;

    /* renamed from: cb */
    private CodecCallback f51cb;
    private EGLContext mEGLContext = EGL14.EGL_NO_CONTEXT;
    private EGLDisplay mEGLDisplay = EGL14.EGL_NO_DISPLAY;
    private EGLSurface mEGLSurface = EGL14.EGL_NO_SURFACE;
    private boolean mFrameAvailable;
    private Object mFrameSyncObject = new Object();
    int mHeight;
    private ByteBuffer mPixelBuf;
    private Surface mSurface;
    private SurfaceTexture mSurfaceTexture;
    private STextureRender mTextureRender;
    int mWidth;

    public void awaitNewImage() {
    }

    public CodecOutputSurface(int i, int i2) {
        if (i <= 0 || i2 <= 0) {
            throw new IllegalArgumentException();
        }
        this.mWidth = i;
        this.mHeight = i2;
        eglSetup();
        makeCurrent();
        setup();
    }

    public void setBitmapCallback(CodecCallback codecCallback) {
        this.f51cb = codecCallback;
    }

    private void setup() {
        this.mTextureRender = new STextureRender();
        this.mTextureRender.surfaceCreated();
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("textureID=");
        sb.append(this.mTextureRender.getTextureId());
        Log.d(str, sb.toString());
        this.mSurfaceTexture = new SurfaceTexture(this.mTextureRender.getTextureId());
        this.mSurfaceTexture.setOnFrameAvailableListener(this);
        this.mSurface = new Surface(this.mSurfaceTexture);
        this.mPixelBuf = ByteBuffer.allocateDirect(this.mWidth * this.mHeight * 4);
        this.mPixelBuf.order(ByteOrder.LITTLE_ENDIAN);
    }

    private void eglSetup() {
        this.mEGLDisplay = EGL14.eglGetDisplay(0);
        if (this.mEGLDisplay != EGL14.EGL_NO_DISPLAY) {
            int[] iArr = new int[2];
            if (EGL14.eglInitialize(this.mEGLDisplay, iArr, 0, iArr, 1)) {
                EGLConfig[] eGLConfigArr = new EGLConfig[1];
                if (EGL14.eglChooseConfig(this.mEGLDisplay, new int[]{12324, 8, 12323, 8, 12322, 8, 12321, 8, 12352, 4, 12339, 1, 12344}, 0, eGLConfigArr, 0, eGLConfigArr.length, new int[1], 0)) {
                    this.mEGLContext = EGL14.eglCreateContext(this.mEGLDisplay, eGLConfigArr[0], EGL14.EGL_NO_CONTEXT, new int[]{12440, 2, 12344}, 0);
                    checkEglError("eglCreateContext");
                    if (this.mEGLContext != null) {
                        this.mEGLSurface = EGL14.eglCreatePbufferSurface(this.mEGLDisplay, eGLConfigArr[0], new int[]{12375, this.mWidth, 12374, this.mHeight, 12344}, 0);
                        checkEglError("eglCreatePbufferSurface");
                        if (this.mEGLSurface == null) {
                            throw new RuntimeException("surface was null");
                        }
                        return;
                    }
                    throw new RuntimeException("null context");
                }
                throw new RuntimeException("unable to find RGB888+recordable ES2 EGL config");
            }
            this.mEGLDisplay = null;
            throw new RuntimeException("unable to initialize EGL14");
        }
        throw new RuntimeException("unable to get EGL14 display");
    }

    public void release() {
        if (this.mEGLDisplay != EGL14.EGL_NO_DISPLAY) {
            EGL14.eglDestroySurface(this.mEGLDisplay, this.mEGLSurface);
            EGL14.eglDestroyContext(this.mEGLDisplay, this.mEGLContext);
            EGL14.eglReleaseThread();
            EGL14.eglTerminate(this.mEGLDisplay);
        }
        this.mEGLDisplay = EGL14.EGL_NO_DISPLAY;
        this.mEGLContext = EGL14.EGL_NO_CONTEXT;
        this.mEGLSurface = EGL14.EGL_NO_SURFACE;
        this.mSurface.release();
        this.mTextureRender = null;
        this.mSurface = null;
        this.mSurfaceTexture = null;
    }

    public void makeCurrent() {
        EGLDisplay eGLDisplay = this.mEGLDisplay;
        EGLSurface eGLSurface = this.mEGLSurface;
        if (!EGL14.eglMakeCurrent(eGLDisplay, eGLSurface, eGLSurface, this.mEGLContext)) {
            throw new RuntimeException("eglMakeCurrent failed");
        }
    }

    public Surface getSurface() {
        return this.mSurface;
    }

    public void drawImage(boolean z) {
        this.mTextureRender.drawFrame(this.mSurfaceTexture, z);
    }

    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        Log.d(TAG, "new frame available");
        SurfaceTexture surfaceTexture2 = this.mSurfaceTexture;
        if (surfaceTexture2 == null) {
            Log.e(TAG, "onFrameAvailable called even after releasing the texture");
            return;
        }
        surfaceTexture2.updateTexImage();
        drawImage(false);
        CodecCallback codecCallback = this.f51cb;
        if (codecCallback != null) {
            codecCallback.onFrameAvailable();
        }
    }

    public Bitmap getBitmap() throws IOException {
        this.mPixelBuf.rewind();
        GLES20.glReadPixels(0, 0, this.mWidth, this.mHeight, 6408, 5121, this.mPixelBuf);
        int i = this.mWidth;
        int i2 = this.mHeight;
        int i3 = i * i2;
        Bitmap createBitmap = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
        this.mPixelBuf.rewind();
        int[] iArr = new int[i3];
        this.mPixelBuf.asIntBuffer().get(iArr);
        for (int i4 = 0; i4 < i3; i4++) {
            iArr[i4] = (iArr[i4] & -16711936) | ((iArr[i4] & 255) << 16) | ((iArr[i4] & 16711680) >> 16);
        }
        int i5 = this.mWidth;
        createBitmap.setPixels(iArr, i3 - i5, -i5, 0, 0, i5, this.mHeight);
        return createBitmap;
    }

    private void checkEglError(String str) {
        int eglGetError = EGL14.eglGetError();
        if (eglGetError != 12288) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(": EGL error: 0x");
            sb.append(Integer.toHexString(eglGetError));
            throw new RuntimeException(sb.toString());
        }
    }
}
