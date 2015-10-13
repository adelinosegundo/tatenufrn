package com.android_dev.tatenuufrn.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by adelinosegundo on 10/8/15.
 */
public class DbBitmapUtility {

    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream(bitmap.getByteCount());
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    public static Bitmap LoadImageFromWebOperations(String url_string) {
        try {
            URL url = new URL(url_string);
            Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            Bitmap emptyBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
            if (bitmap.sameAs(emptyBitmap)) {
                return null;
            }
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String encodeBitmapToBase64(Bitmap bitmap) {
        return Base64.encodeToString(getBytes(bitmap), Base64.DEFAULT);
    }

    public static Bitmap decodeBitmapFromBase64(String encoded) {
        byte[] decodedString = Base64.decode(encoded, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
}
