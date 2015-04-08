package com.brisktouch.timeline.test;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.brisktouch.timeline.R;
import com.brisktouch.timeline.ui.RecyclingImageView;
import com.brisktouch.timeline.util.ImageCache;
import com.brisktouch.timeline.util.ImageNative;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by jim on 4/7/2015.
 */
public class ImageListActivity extends Activity {
    String TAG = "ImageListActivity";

    private ImageNative mImageFetcher;
    int mScreenWidth;
    int mScreenHeight;
    int imageHeight = 80;
    int imageWidth = 80;
    HashMap<Long, List<String>> dateGruopMap = new HashMap<Long, List<String>>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");


        RelativeLayout relativeLayout = new RelativeLayout(getApplication());
        relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT));
        relativeLayout.setBackgroundColor(Color.WHITE);
        LinearLayout linearLayout = new LinearLayout(getApplication());
        mScreenWidth = getResources().getDisplayMetrics().widthPixels;
        mScreenHeight = getResources().getDisplayMetrics().heightPixels;
        RelativeLayout.LayoutParams linearParams = new RelativeLayout.LayoutParams(mScreenWidth*8/10, mScreenHeight*8/10);
        linearParams.setMargins(mScreenWidth / 10, mScreenWidth / 10, 0, 0);
        linearLayout.setLayoutParams(linearParams);

        getImage();
        List<Map.Entry<Long, List<String>>> infoIds = new ArrayList<Map.Entry<Long, List<String>>>(dateGruopMap.entrySet());
        Collections.sort(infoIds, new Comparator<Map.Entry<Long, List<String>>>() {
            public int compare(Map.Entry<Long, List<String>> o1,
                               Map.Entry<Long, List<String>> o2) {
                return (o1.getKey()).compareTo(o2.getKey());
            }
        });

        ListView listView = new ListView(getApplication());
        listView.setBackgroundColor(Color.WHITE);
        listView.setDivider(null);
        ImageListAdapter mImageListAdapter = new ImageListAdapter(getApplication(), infoIds, listView);
        listView.setAdapter(mImageListAdapter);
        ImageCache.ImageCacheParams cacheParams = new ImageCache.ImageCacheParams();
        cacheParams.setMemCacheSizePercent(0.25f);
        mImageFetcher = new ImageNative(this, 80);
        mImageFetcher.setLoadingImage(R.drawable.empty_photo);
        mImageFetcher.addImageCache(cacheParams);

        linearLayout.addView(listView);

        relativeLayout.addView(linearLayout);

        setContentView(relativeLayout);

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


    public class ImageListAdapter extends BaseAdapter {
        List<Map.Entry<Long, List<String>>> list;
        Context context;
        ListView mListView;
        int changeImageId;

        public ImageListAdapter(Context c, List<Map.Entry<Long, List<String>>> list, ListView mListView) {
            this.list = list;
            this.context = c;
            this.mListView = mListView;
        }

        public void setChangeImageId(int id) {
            changeImageId = id;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int arg0) {
            return list.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(final int index, View convertView, ViewGroup arg2) {
            LinearLayout lar;
            TextView tv = null;
            ImageView imageListLinear = null;

            if(null == convertView){
                Log.d(TAG, "null");
                lar = new LinearLayout(getApplication());
                tv = new TextView(getApplication());
                imageListLinear = new ImageView(getApplication());
                lar.addView(tv);
                lar.addView(imageListLinear);
                lar.setTag(lar);
            }else{
                Log.d(TAG, "not null");
                lar = (LinearLayout) convertView.getTag();
                tv = (TextView)lar.getChildAt(0);
                imageListLinear = (ImageView)lar.getChildAt(1);
            }
            lar.setOrientation(LinearLayout.VERTICAL);
            long dateTime = list.get(index).getKey();
            SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
            tv.setText(timeFormat.format(new Date(dateTime)));
            tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            imageListLinear.setImageResource(R.drawable.ic_action_save);
            /*
            imageListLinear.setOrientation(LinearLayout.VERTICAL);
            imageListLinear.setBackgroundColor(Color.WHITE);
            imageListLinear.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            int w = mScreenWidth*8/10;
            int rowDisplaySum = w/80;

            imageListLinear.setWeightSum(rowDisplaySum);
            List<String> listImagePath = list.get(index).getValue();

            int rowSum = listImagePath.size()/rowDisplaySum;
            if(listImagePath.size()%rowDisplaySum>0)
                rowSum++;

            for(int i=0;i<rowSum;i++){
                LinearLayout rowLinearLayout;
                if(imageListLinear.getChildCount()>i){
                    rowLinearLayout = (LinearLayout)imageListLinear.getChildAt(i);
                }else{
                    rowLinearLayout = new LinearLayout(getApplication());
                    imageListLinear.addView(rowLinearLayout);
                }

                rowLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
                rowLinearLayout.setWeightSum(rowDisplaySum);
                if(rowLinearLayout.getChildCount()<rowDisplaySum){
                    rowLinearLayout.removeAllViews();
                    for(int j=0;j<rowDisplaySum;j++){
                        ImageView _image = new RecyclingImageView(getApplication());
                        rowLinearLayout.addView(_image);
                    }
                }

                for(int j=0; j< rowLinearLayout.getChildCount(); j++){
                    ImageView _image = (ImageView)rowLinearLayout.getChildAt(j);
                    _image.setScaleType(ImageView.ScaleType.FIT_XY);
                    _image.setImageResource(R.drawable.empty_photo);
                    LinearLayout.LayoutParams _params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 80);
                    _params.weight = 1;
                    _image.setLayoutParams(_params);
                    mImageFetcher.setImageSize(w / rowDisplaySum, 80);
                    int _index = i * rowSum + j;
                    if(listImagePath.size()>_index)
                        mImageFetcher.loadImage(listImagePath.get(i), _image);
                    else
                        _image.setBackgroundColor(Color.WHITE);
                }
            }

            int h = rowSum*80 + 100;
            */
            lar.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, 400));

            lar.setBackgroundColor(Color.WHITE);
            return lar;
        }
    }

    public class GroupAdapter extends BaseAdapter {
        final Point pt = new Point();
        List<String> list;
        Context context;
        GridView mGridView;
        private GridView.LayoutParams mImageViewLayoutParams;

        public GroupAdapter(Context c, List<String> list, GridView mGridView){
            this.list = list;
            this.context = c;
            this.mGridView = mGridView;
            mImageViewLayoutParams = new GridView.LayoutParams(
                    GridView.LayoutParams.WRAP_CONTENT, 80);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int arg0) {
            return list.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup arg2) {
            ImageView imageView;
            if (convertView == null) { // if it's not recycled, instantiate and initialize
                imageView = new RecyclingImageView(context);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setLayoutParams(mImageViewLayoutParams);
            } else { // Otherwise re-use the converted view
                imageView = (ImageView) convertView;
            }

            mImageFetcher.setImageSize(80 ,80);
            imageView.setBackgroundColor(Color.GREEN);
            mImageFetcher.loadImage(list.get(position), imageView);
            return imageView;
        }

    }

    class ListHolder
    {
        public TextView textView;
        public GridView gridView;
    }
}
