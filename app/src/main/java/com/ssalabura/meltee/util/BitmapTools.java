package com.ssalabura.meltee.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageProxy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class BitmapTools {

    public static Bitmap fromImageProxy(ImageProxy image, CameraSelector cameraSelector) {
        ImageProxy.PlaneProxy planeProxy = image.getPlanes()[0];
        ByteBuffer buffer = planeProxy.getBuffer();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        Matrix matrix = new Matrix();
        matrix.postRotate(image.getImageInfo().getRotationDegrees());
        if(cameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA) {
            matrix.postScale(-1,1);
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static byte[] toByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] output = stream.toByteArray();
        try {
            stream.close();
        } catch (IOException ignored) { }
        return output;
    }

    public static Bitmap fromByteArray(byte[] array) {
        return BitmapFactory.decodeByteArray(array, 0, array.length);
    }

    public static Bitmap toProfilePicture(Bitmap bitmap) {
        Bitmap output;
        if (bitmap.getWidth() >= bitmap.getHeight()){
            output = Bitmap.createBitmap(bitmap, bitmap.getWidth()/2 - bitmap.getHeight()/2, 0, bitmap.getHeight(), bitmap.getHeight());
        } else{
            output = Bitmap.createBitmap(bitmap, 0, bitmap.getHeight()/2 - bitmap.getWidth()/2, bitmap.getWidth(), bitmap.getWidth());
        }
        return Bitmap.createScaledBitmap(output, 400, 400, true);
    }
}
