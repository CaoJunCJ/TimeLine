package com.brisktouch.timeline.add;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by jim on 1/14/2015.
 */
public class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {

    ImageView imageView;
    boolean isWork = false;


    public BitmapWorkerTask(ImageView imageView){

        this.imageView = imageView;
        isWork = true;
    }

    public void setImageView(ImageView imageView){
        this.imageView = imageView;
        isWork = true;
    }


    protected Bitmap doInBackground(String ... params){
        final Bitmap bitmap = NativeImageLoader.decodeSampledBitmapFromPath(
                params[0], 100, 100);
        NativeImageLoader.getInstance().addBitmapToMemoryCache(String.valueOf(params[0]), bitmap);
        return bitmap;
    }

    protected void onPostExecute(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
        isWork = false;
    }
}