package com.brisktouch.timeline.test;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ListView;
import android.widget.Toast;
import com.brisktouch.timeline.R;
import com.brisktouch.timeline.custom.ListViewAdapter;
import com.brisktouch.timeline.util.ImageCache;
import com.brisktouch.timeline.util.ImageNative;

/**
 * Created by jim on 4/8/2015.
 */
public class TestActivity extends Activity{

    private ListView mListView;
    public ImageNative mImageFetcher;
    private ListViewAdapter mListViewAdapter;
    HashMap<Long, List<String>> dateGruopMap = new HashMap<Long, List<String>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_main);
        init();
    }
    private void init(){
        mImageFetcher = new ImageNative(this, 80);
        mImageFetcher.setLoadingImage(R.drawable.empty_photo);
        ImageCache.ImageCacheParams cacheParams = new ImageCache.ImageCacheParams();
        cacheParams.setMemCacheSizePercent(0.25f);
        mImageFetcher.addImageCache(cacheParams);
        mListView=(ListView) findViewById(R.id.listView);
        getImage();
        List<Map.Entry<Long, List<String>>> infoIds = new ArrayList<Map.Entry<Long, List<String>>>(dateGruopMap.entrySet());
        Collections.sort(infoIds, new Comparator<Map.Entry<Long, List<String>>>() {
            public int compare(Map.Entry<Long, List<String>> o1,
                               Map.Entry<Long, List<String>> o2) {
                return (o1.getKey()).compareTo(o2.getKey());
            }
        });
        mListViewAdapter=new ListViewAdapter(infoIds, TestActivity.this, null);
        mListView.setAdapter(mListViewAdapter);
    }

    private void getImage(){
        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Toast.makeText(getApplication(), "Not External Storage Card", Toast.LENGTH_SHORT).show();
            return;
        }

        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver mContentResolver = getApplication().getContentResolver();

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
                d = time.parse(String.format("%s %s %s 00 00 00",cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)));
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
    }

}
