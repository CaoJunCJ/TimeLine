package com.brisktouch.timeline.custom;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.brisktouch.timeline.R;
import com.brisktouch.timeline.style.BaseStyleActivity;
import com.brisktouch.timeline.util.ImageCache;
import com.brisktouch.timeline.util.ImageNative;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by jim on 4/8/2015.
 */
public class BrowseNativeImageUtil implements View.OnClickListener{

    private String TAG = "BrowseNativeImageUtil";

    private static final int UPDARE_IMAGELIST_UI = 0;

    private Context context;
    private int clickedViewId;

    private ListView mListView;
    public ImageNative mImageFetcher;
    private ListViewAdapter mListViewAdapter;
    HashMap<Long, List<String>> dateGruopMap;
    List<Map.Entry<Long, List<String>>> infoIds;
    private boolean isInit = false;
    private Runnable queryImageURI;

    public BrowseNativeImageUtil(Context context){
        this.context = context;
        infoIds = new ArrayList<Map.Entry<Long, List<String>>>();

        queryImageURI = new Runnable() {
            @Override
            public void run() {
                //for test, Simulation very many image.
                //try{
                //    Thread.currentThread().sleep(5000);
                //   Log.d(TAG, "getImage end");
                //}catch (Exception e){e.printStackTrace();}
                getImage();
                Message msg = new Message();
                msg.what = UPDARE_IMAGELIST_UI;
                handler.sendMessage(msg);
            }
        };

        new Thread(queryImageURI).start();
        //init();
    }

    public View getView(){
        return mListView;
    }

    public void setChangeImageId(int id){
        clickedViewId = id;
    }

    public void hideView(){
        mListView.setVisibility(View.INVISIBLE);
    }

    public void showView(){
        mListView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v){
        ImageView imageView = (ImageView)((Activity)context).findViewById(clickedViewId);
        String imagePath = (String)v.getTag();
        if(imagePath!=null){
            //TODO when the image is very big, is will be OutOfMemory.
            imageView.setImageBitmap(BitmapFactory.decodeFile(imagePath));
        }
        hideView();
        ((BaseStyleActivity)context).nativeImageListDisplay = false;
    }

    public void init(){
        mImageFetcher = new ImageNative(context, 80);
        mImageFetcher.setLoadingImage(R.drawable.empty_photo);
        ImageCache.ImageCacheParams cacheParams = new ImageCache.ImageCacheParams();
        cacheParams.setMemCacheSizePercent(0.25f);
        mImageFetcher.addImageCache(cacheParams);
        LayoutInflater.from(context).inflate(R.layout.test_main, null);
        //mListView=(ListView) LayoutInflater.from(context).inflate(R.layout.test_main, null).findViewById(R.id.listView);
        mListView = new ListView(context);
        RelativeLayout.LayoutParams listViewParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        listViewParams.setMargins(20,20,20,20);
        mListView.setLayoutParams(listViewParams);
        mListView.setBackgroundColor(Color.WHITE);
        mListView.setFocusable(false);
        //getImage();

        mListViewAdapter=new ListViewAdapter(infoIds, context, this);
        mListView.setAdapter(mListViewAdapter);
        isInit = true;
        //TODO alert a Progress Box when queryImageURI not finish.
    }

    public boolean isInit(){
        return isInit;
    }

    private void getImage(){
        if(dateGruopMap!=null)
            return;
        else
            dateGruopMap = new HashMap<Long, List<String>>();
        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Toast.makeText(context, "Not External Storage Card", Toast.LENGTH_SHORT).show();
            return;
        }

        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver mContentResolver = context.getContentResolver();

        Cursor mCursor = mContentResolver.query(mImageUri, null,
                MediaStore.Images.Media.MIME_TYPE + "=? or "
                        + MediaStore.Images.Media.MIME_TYPE + "=?",
                new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DEFAULT_SORT_ORDER);

        while(mCursor.moveToNext()){

            String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
            File file = new File(path);
            long lastModified = file.lastModified();
            String parentPath = file.getParentFile().getAbsolutePath();
            SimpleDateFormat time=new SimpleDateFormat("yyyy MM dd HH mm ss");
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(lastModified);
            Date d = null;
            try{
                d = time.parse(String.format("%s %s %s 00 00 00",
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH)+1,
                        cal.get(Calendar.DAY_OF_MONTH)));
            }catch (Exception e){}
            long day = d.getTime();

            if(!dateGruopMap.containsKey(day)){
                List<String> chileList = new ArrayList<String>();
                chileList.add(path);
                dateGruopMap.put(day, chileList);
            }else{
                dateGruopMap.get(day).add(path);
            }
        }

        //infoIds = new ArrayList<Map.Entry<Long, List<String>>>(dateGruopMap.entrySet());
        infoIds.addAll(dateGruopMap.entrySet());

        Collections.sort(infoIds, new Comparator<Map.Entry<Long, List<String>>>() {
            public int compare(Map.Entry<Long, List<String>> o1,
                               Map.Entry<Long, List<String>> o2) {
                return (o2.getKey()).compareTo(o1.getKey());
            }
        });

    }

    public void onResume() {
        if(mImageFetcher!=null)
            mImageFetcher.setExitTasksEarly(false);
        if(mListViewAdapter!=null)
            mListViewAdapter.notifyDataSetChanged();
    }

    public void onPause() {
        if(mImageFetcher!=null){
            mImageFetcher.setPauseWork(false);
            mImageFetcher.setExitTasksEarly(true);
            mImageFetcher.flushCache();
        }
    }

    public void onDestroy() {
        if(mImageFetcher!=null)
            mImageFetcher.closeCache();
        if(dateGruopMap!=null)
            dateGruopMap.clear();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == UPDARE_IMAGELIST_UI) {
                if(mListViewAdapter!=null)
                    mListViewAdapter.notifyDataSetChanged();
            }
        }
    };
}
