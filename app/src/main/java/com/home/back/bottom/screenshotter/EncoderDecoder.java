package com.home.back.bottom.screenshotter;

import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import android.util.Log;
import android.view.Surface;
import java.io.IOException;
import java.nio.ByteBuffer;

public class EncoderDecoder implements Runnable {
    private static final String TAG = "EncoderDecoder";
    private String MIME_TYPE = "video/avc";
    boolean VERBOSE = true;
    private CodecCallback codecCb;
    private MediaCodec decoder;
    private MediaCodec encoder;
    private int height;
    private CodecOutputSurface outputSurface;
    private int width;

    public EncoderDecoder(int i, int i2, CodecCallback codecCallback) {
        this.width = i;
        this.height = i2;
        this.codecCb = codecCallback;
        this.outputSurface = new CodecOutputSurface(i, i2);
    }

    public Surface createDisplaySurface() throws IOException {
        MediaFormat createVideoFormat = MediaFormat.createVideoFormat(this.MIME_TYPE, this.width, this.height);
        createVideoFormat.setInteger("bitrate", 524288);
        createVideoFormat.setInteger("frame-rate", 30);
        createVideoFormat.setInteger("color-format", 2130708361);
        createVideoFormat.setInteger("i-frame-interval", 1);
        if (this.VERBOSE) {
            Log.i(TAG, "Starting encoder");
        }
        this.encoder = MediaCodec.createEncoderByType(this.MIME_TYPE);
        this.encoder.configure(createVideoFormat, null, null, 1);
        Surface createInputSurface = this.encoder.createInputSurface();
        this.encoder.start();
        initDecoder();
        return createInputSurface;
    }

    private void initDecoder() throws IOException {
        this.decoder = MediaCodec.createDecoderByType(this.MIME_TYPE);
    }

    public void run() {
        BufferInfo bufferInfo = new BufferInfo();
        boolean z = false;
        boolean z2 = false;
        boolean z3 = false;
        while (!z) {
            if (!z2) {
                int dequeueOutputBuffer = this.encoder.dequeueOutputBuffer(bufferInfo, 10000);
                if (dequeueOutputBuffer == -1) {
                    if (this.VERBOSE) {
                        Log.d(TAG, "no output from encoder available");
                    }
                } else if (dequeueOutputBuffer == -2) {
                    MediaFormat outputFormat = this.encoder.getOutputFormat();
                    if (this.VERBOSE) {
                        String str = TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("encoder output format changed: ");
                        sb.append(outputFormat);
                        Log.d(str, sb.toString());
                    }
                } else if (dequeueOutputBuffer >= 0) {
                    ByteBuffer outputBuffer = this.encoder.getOutputBuffer(dequeueOutputBuffer);
                    outputBuffer.position(bufferInfo.offset);
                    outputBuffer.limit(bufferInfo.offset + bufferInfo.size);
                    if ((bufferInfo.flags & 2) != 0) {
                        MediaFormat createVideoFormat = MediaFormat.createVideoFormat(this.MIME_TYPE, this.width, this.height);
                        createVideoFormat.setByteBuffer("csd-0", outputBuffer);
                        this.decoder.configure(createVideoFormat, this.outputSurface.getSurface(), null, 0);
                        this.decoder.start();
                        if (this.VERBOSE) {
                            String str2 = TAG;
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("decoder configured (");
                            sb2.append(bufferInfo.size);
                            sb2.append(" bytes)");
                            Log.d(str2, sb2.toString());
                        }
                        z3 = true;
                    } else {
                        int dequeueInputBuffer = this.decoder.dequeueInputBuffer(-1);
                        ByteBuffer inputBuffer = this.decoder.getInputBuffer(dequeueInputBuffer);
                        inputBuffer.clear();
                        inputBuffer.put(outputBuffer);
                        this.decoder.queueInputBuffer(dequeueInputBuffer, 0, bufferInfo.size, bufferInfo.presentationTimeUs, bufferInfo.flags);
                        z2 = (bufferInfo.flags & 4) != 0;
                        if (this.VERBOSE) {
                            String str3 = TAG;
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append("passed ");
                            sb3.append(bufferInfo.size);
                            sb3.append(" bytes to decoder");
                            sb3.append(z2 ? " (EOS)" : "");
                            Log.d(str3, sb3.toString());
                        }
                    }
                    this.encoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                }
            }
            if (z3) {
                int dequeueOutputBuffer2 = this.decoder.dequeueOutputBuffer(bufferInfo, 10000);
                if (dequeueOutputBuffer2 == -1) {
                    if (this.VERBOSE) {
                        Log.d(TAG, "no output from decoder available");
                    }
                } else if (dequeueOutputBuffer2 != -2 && dequeueOutputBuffer2 >= 0) {
                    boolean z4 = bufferInfo.size != 0;
                    if ((bufferInfo.flags & 4) != 0) {
                        if (this.VERBOSE) {
                            Log.d(TAG, "output EOS");
                        }
                        z = true;
                    }
                    this.decoder.releaseOutputBuffer(dequeueOutputBuffer2, z4);
                    if (z4) {
                        this.outputSurface.awaitNewImage();
                        this.outputSurface.drawImage(true);
                    } else if (this.VERBOSE) {
                        Log.d(TAG, "got empty frame");
                    }
                }
            }
        }
    }

    public void stop() {
        this.encoder.signalEndOfInputStream();
    }
}
