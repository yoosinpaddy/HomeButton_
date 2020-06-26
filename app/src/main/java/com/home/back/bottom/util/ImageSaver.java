package com.home.back.bottom.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageSaver {
    private boolean baseImageDir;
    private Context context;
    private String directoryName = "images";
    private boolean external;
    private String fileName = "image.png";

    public ImageSaver(Context context2) {
        this.context = context2;
    }

    public ImageSaver setFileName(String str) {
        this.fileName = str;
        return this;
    }

    public ImageSaver setExternal(boolean z) {
        this.external = z;
        return this;
    }

    public ImageSaver setDirectoryName(String str) {
        this.directoryName = str;
        return this;
    }

    public ImageSaver setBaseImageDir(boolean z) {
        this.baseImageDir = z;
        return this;
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0029 A[SYNTHETIC, Splitter:B:17:0x0029] */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0036 A[SYNTHETIC, Splitter:B:25:0x0036] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public File save(Bitmap bitmap) {
        FileOutputStream fileOutputStream;
        File createFile = createFile();
        try {
            fileOutputStream = new FileOutputStream(createFile());
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return createFile;
    }

    @NonNull
    private File createFile() {
        File file;
        if (!this.external) {
            file = this.context.getDir(this.directoryName, 0);
        } else if (this.baseImageDir) {
            file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        } else {
            file = getAlbumStorageDir(this.directoryName);
        }
        return new File(file, this.fileName);
    }

    private File getAlbumStorageDir(String str) {
        String file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        StringBuilder sb = new StringBuilder();
        sb.append(file);
        sb.append("/");
        sb.append(str);
        File file2 = new File(sb.toString());
        if (!file2.exists()) {
            file2.mkdirs();
        }
        return file2;
    }

    public static boolean isExternalStorageWritable() {
        return "mounted".equals(Environment.getExternalStorageState());
    }

    public static boolean isExternalStorageReadable() {
        String externalStorageState = Environment.getExternalStorageState();
        return "mounted".equals(externalStorageState) || "mounted_ro".equals(externalStorageState);
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0022 A[SYNTHETIC, Splitter:B:17:0x0022] */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0031 A[SYNTHETIC, Splitter:B:25:0x0031] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public Bitmap load() {
        Throwable th;
        FileInputStream fileInputStream;
        FileInputStream fileInputStream2 = null;
        try {
            fileInputStream = new FileInputStream(createFile());
            try {
                Bitmap decodeStream = BitmapFactory.decodeStream(fileInputStream);
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return decodeStream;
            } catch (Exception e) {
                try {
                    e.printStackTrace();
                    if (fileInputStream != null) {
                    }
                    return null;
                } catch (Throwable th2) {
                    FileInputStream fileInputStream3 = fileInputStream;
                    th = th2;
                    fileInputStream2 = fileInputStream3;
                    if (fileInputStream2 != null) {
                    }
                    throw th;
                }
            }
        } catch (Exception e) {
            fileInputStream = null;
            e.printStackTrace();
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
            }
            return null;
        } catch (Throwable th3) {
            th = th3;
            if (fileInputStream2 != null) {
                try {
                    fileInputStream2.close();
                } catch (IOException e5) {
                    e5.printStackTrace();
                }
            }
            try {
                throw th;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        return null;
    }
}
