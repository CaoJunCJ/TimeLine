package com.brisktouch.timeline.util;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by cerosoft on 1/15/2015.
 */
public class ImageNative extends ImageResizer {
    private static final String TAG = "ImageNative";

    public ImageNative(Context context, int imageWidth, int imageHeight) {
        super(context, imageWidth, imageHeight);
    }

    /**
     * Initialize providing a single target image size (used for both width and height);
     *
     * @param context
     * @param imageSize
     */
    public ImageNative(Context context, int imageSize) {
        super(context, imageSize);

    }

    protected Bitmap processBitmap(Object data) {
        return processBitmap(String.valueOf(data));
    }

    private Bitmap processBitmap(String path) {
        Bitmap bitmap = decodeSampledBitmapFromFile(path, mImageWidth,
                mImageHeight, getImageCache());

        return bitmap;
    }


}
