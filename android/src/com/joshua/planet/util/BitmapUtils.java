package com.joshua.planet.util;

import android.graphics.Bitmap;
import android.os.Environment;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class BitmapUtils {
    public static byte[] bitmap2Bytes(Bitmap bmp) {
        int bytes = bmp.getByteCount();

        ByteBuffer buf = ByteBuffer.allocate(bytes);
        bmp.copyPixelsToBuffer(buf);

        return buf.array();
    }

    public static Bitmap bytes2Bitmap(byte[] bytes, int width, int height, @NonNull Bitmap.Config config) {
        Bitmap bmp = Bitmap.createBitmap(width, height, config);
        bmp.copyPixelsFromBuffer(ByteBuffer.wrap(bytes));
        return bmp;
    }

    public static Bitmap buffer2Bitmap(ByteBuffer buffer, int width, int height, @NonNull Bitmap.Config config) {
        Bitmap bmp = Bitmap.createBitmap(width, height, config);
        bmp.copyPixelsFromBuffer(buffer);
        return bmp;
    }

    public static void saveBitmap(Bitmap bitmap, String bitName, Bitmap.CompressFormat format) {
        try {
            File filePic = new File(Environment.getExternalStorageDirectory(), "/" + bitName);
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            bitmap.compress(format, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
