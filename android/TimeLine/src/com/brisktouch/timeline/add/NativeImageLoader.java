package com.brisktouch.timeline.add;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;

public class NativeImageLoader {
	private LruCache<String, Bitmap> mMemoryCache;
	private static NativeImageLoader mInstance = new NativeImageLoader();
	private ExecutorService mImageThreadPool = Executors.newFixedThreadPool(1);

	private NativeImageLoader(){
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

		final int cacheSize = maxMemory / 4;
		mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
			
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
			}
		};
	}
	

	public static NativeImageLoader getInstance(){
		return mInstance;
	}
	

	public Bitmap loadNativeImage(final String path, final NativeImageCallBack mCallBack){
		return this.loadNativeImage(path, null, mCallBack);
	}

	public static Bitmap decodeSampledBitmapFromPath(final String path,
														 int reqWidth, int reqHeight) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(path, options);
	}




	public Bitmap loadNativeImage(final String path, final Point mPoint, final NativeImageCallBack mCallBack){
		Bitmap bitmap = getBitmapFromMemCache(path);
		
		final Handler mHander = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				mCallBack.onImageLoader((Bitmap)msg.obj, path);
			}
			
		};
		
		if(bitmap == null){


			mImageThreadPool.execute(new Runnable() {
				
				@Override
				public void run() {
					Bitmap mBitmap = decodeThumbBitmapForFile(path, mPoint == null ? 0: mPoint.x, mPoint == null ? 0: mPoint.y);
					Message msg = mHander.obtainMessage();
					msg.obj = mBitmap;
					mHander.sendMessage(msg);

					addBitmapToMemoryCache(path, mBitmap);
				}
			});
		}
		return bitmap;
	}

	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (getBitmapFromMemCache(key) == null && bitmap != null) {
			mMemoryCache.put(key, bitmap);
		}
	}


	public Bitmap getBitmapFromMemCache(String key) {
		return mMemoryCache.get(key);
	}
	
	

	private Bitmap decodeThumbBitmapForFile(String path, int viewWidth, int viewHeight){
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		options.inSampleSize = computeScale(options, viewWidth, viewHeight);
		
		options.inJustDecodeBounds = false;
		
		return BitmapFactory.decodeFile(path, options);
	}
	

	private int computeScale(BitmapFactory.Options options, int reqWidth, int reqHeight){
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}
	
	

	public interface NativeImageCallBack{

		public void onImageLoader(Bitmap bitmap, String path);
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
											int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}
}
