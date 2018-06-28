package com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Objects;

import static com.kirtanlabs.nammaapartmentssecurity.nammaapartmentsecurityhome.eintercom.EIntercom.imageFilePath;

public class ImagePicker {

    private static final int DEFAULT_MIN_WIDTH_QUALITY = 400;        // min pixels
    private static final String TEMP_IMAGE_NAME = "tempImage";

    public static Bitmap getImageFromResult(Context context, int resultCode,
                                            Intent imageReturnedIntent) {
        Bitmap bm = null;
        File imageFile = getTempFile(context);
        if (resultCode == Activity.RESULT_OK) {
            Uri selectedImage;
            boolean isCamera = (imageReturnedIntent == null ||
                    imageReturnedIntent.getData() == null ||
                    imageReturnedIntent.getData().equals(Uri.fromFile(imageFile)));
            if (isCamera && !imageFilePath.isEmpty()) {
                selectedImage = Uri.parse(imageFilePath);
            } else {
                selectedImage = imageReturnedIntent.getData();
            }

            bm = getImageResized(context, selectedImage, isCamera);
            int rotation = getRotation(context, selectedImage, isCamera);
            bm = rotate(bm, rotation);
        }
        return bm;
    }

    public static byte[] fileToByteArray(FileInputStream fileInputStream) {
        byte[] byteArray = null;
        try {
            Bitmap bmp = BitmapFactory.decodeStream(fileInputStream);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byteArray = stream.toByteArray();
            bmp.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return byteArray;
    }

    public static byte[] bitmapToByteArray(Bitmap bitmapProfilePic) {
        byte[] profilePhotoByteArray;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmapProfilePic.compress(Bitmap.CompressFormat.PNG, 100, stream);
        profilePhotoByteArray = stream.toByteArray();
        bitmapProfilePic.recycle();
        return profilePhotoByteArray;
    }

    private static Bitmap decodeBitmap(Context context, Uri theUri, int sampleSize, boolean isCamera) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = sampleSize;

        if (isCamera) {
            return BitmapFactory.decodeFile(imageFilePath, options);
        }

        AssetFileDescriptor fileDescriptor = null;
        try {
            fileDescriptor = context.getContentResolver().openAssetFileDescriptor(theUri, "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return BitmapFactory.decodeFileDescriptor(
                Objects.requireNonNull(fileDescriptor).getFileDescriptor(), null, options);
    }

    /**
     * Resize to avoid using too much memory loading big images (e.g.: 2560*1920)
     **/
    private static Bitmap getImageResized(Context context, Uri selectedImage, boolean isCamera) {
        Bitmap bm;
        int[] sampleSizes = new int[]{5, 3, 2, 1};
        int i = 0;
        do {
            bm = decodeBitmap(context, selectedImage, sampleSizes[i], isCamera);
            i++;
        } while (bm.getWidth() < DEFAULT_MIN_WIDTH_QUALITY && i < sampleSizes.length);
        return bm;
    }


    private static int getRotation(Context context, Uri imageUri, boolean isCamera) {
        int rotation;
        if (isCamera) {
            rotation = getRotationFromCamera(context, imageUri);
        } else {
            rotation = getRotationFromGallery(context, imageUri);
        }
        return rotation;
    }

    private static int getRotationFromCamera(Context context, Uri imageFile) {
        int rotate = 0;
        try {

            context.getContentResolver().notifyChange(imageFile, null);
            ExifInterface exif = new ExifInterface(imageFile.getPath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }

    private static int getRotationFromGallery(Context context, Uri imageUri) {
        String[] columns = {MediaStore.Images.Media.ORIENTATION};
        Cursor cursor = context.getContentResolver().query(imageUri, columns, null, null, null);
        if (cursor == null) return 0;

        cursor.moveToFirst();

        int orientationColumnIndex = cursor.getColumnIndex(columns[0]);
        return cursor.getInt(orientationColumnIndex);
    }


    private static Bitmap rotate(Bitmap bm, int rotation) {
        if (rotation != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);
            return Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        }
        return bm;
    }


    private static File getTempFile(Context context) {
        File imageFile = new File(context.getExternalCacheDir(), TEMP_IMAGE_NAME);
        imageFile.getParentFile().mkdir();
        return imageFile;
    }
}
