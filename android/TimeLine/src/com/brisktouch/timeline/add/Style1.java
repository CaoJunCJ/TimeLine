package com.brisktouch.timeline.add;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.*;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.brisktouch.timeline.R;
import android.net.Uri;
import android.widget.AbsListView.LayoutParams;
import com.brisktouch.timeline.ui.RecyclingImageView;
import com.brisktouch.timeline.util.ImageCache;
import com.brisktouch.timeline.util.ImageNative;
import com.brisktouch.timeline.util.Utils;

import java.io.File;
import java.util.*;
import java.text.SimpleDateFormat;

/**
 * Created by cerosoft on 1/12/2015.
 */


//TODO must be Reconstruction!!!!!
public class Style1 extends Activity {

    HashMap<Long, List<String>> dateGruopMap;
    HashMap<String, List<String>> folderGruopMap;
    private int mImageThumbSize;
    private int mImageThumbSpacing;
    private ImageNative mImageFetcher;
    ListView listView;
    ImageListAdapter mImageListAdapter;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //hide status bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.style1);
        final ImageButton image = (ImageButton)findViewById(R.id.imageButton8);
        image.setOnClickListener(new SelectPic());

    }

    public void popSelectPictureDialog(){
        List<Map.Entry<Long, List<String>>> infoIds = new ArrayList<Map.Entry<Long, List<String>>>(dateGruopMap.entrySet());
        Collections.sort(infoIds, new Comparator<Map.Entry<Long, List<String>>>() {
            public int compare(Map.Entry<Long, List<String>> o1,
                               Map.Entry<Long, List<String>> o2) {
                return (o1.getKey()).compareTo(o2.getKey());
            }
        });
        Log.d(Style1.class.getName(),"infoIds size:"+infoIds.size());

        mImageThumbSize = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_size);
        mImageThumbSpacing = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_spacing);
        ImageCache.ImageCacheParams cacheParams = new ImageCache.ImageCacheParams();
        cacheParams.setMemCacheSizePercent(0.25f);
        mImageFetcher = new ImageNative(this, mImageThumbSize);
        mImageFetcher.setLoadingImage(R.drawable.empty_photo);
        mImageFetcher.addImageCache(cacheParams);

        LinearLayout main = new LinearLayout(Style1.this);
        main.setOrientation(LinearLayout.VERTICAL);
        main.setBackgroundColor(Color.parseColor("#ffffffff"));




        listView = new ListView(Style1.this);
        listView.setBackgroundColor(Color.parseColor("#ffffffff"));
        listView.setCacheColorHint(0);
        mImageListAdapter = new ImageListAdapter(Style1.this, infoIds, listView);
        listView.setAdapter(mImageListAdapter);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                // Pause fetcher to ensure smoother scrolling when flinging
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    // Before Honeycomb pause image loading on scroll to help with performance
                    if (!Utils.hasHoneycomb()) {
                        mImageFetcher.setPauseWork(true);
                    }
                } else {
                    mImageFetcher.setPauseWork(false);
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
            }
        });



        main.addView(listView);


        new AlertDialog.Builder(Style1.this).setView(main).show();
    }

    public class SelectPic implements View.OnClickListener {



        private Handler mHandler = new Handler(){
            public void handleMessage(Message msg){
                popSelectPictureDialog();
            }
        };

        public SelectPic(){

            dateGruopMap = new HashMap<Long, List<String>>();
            folderGruopMap = new HashMap<String, List<String>>();
        }



        public void onClick(View v) {
            Log.d(Style1.class.getName(),"onclick");
            getImage();
        }

        private void getImage(){
            if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                Toast.makeText(Style1.this, "Not External Storage Card", Toast.LENGTH_SHORT).show();
                return;
            }

            //maybe show progress dialog?

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    ContentResolver mContentResolver = Style1.this.getContentResolver();

                    Cursor mCursor = mContentResolver.query(mImageUri, null,
                            MediaStore.Images.Media.MIME_TYPE + "=? or "
                    + MediaStore.Images.Media.MIME_TYPE + "=?",
                            new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DEFAULT_SORT_ORDER);

                    while(mCursor.moveToNext()){
                        String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                        File file = new File(path);
                        long lastModified = file.lastModified();
                        String parentPath = file.getParentFile().getAbsolutePath();
                        if(!folderGruopMap.containsKey(parentPath)){
                            List<String> chileList = new ArrayList<String>();
                            chileList.add(path);
                            folderGruopMap.put(parentPath, chileList);
                        }else{
                            folderGruopMap.get(parentPath).add(path);
                        }

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
                    mHandler.sendEmptyMessage(1);
                }
            }).start();
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
                    mImageThumbSize, mImageThumbSize);
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
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setLayoutParams(mImageViewLayoutParams);
            } else { // Otherwise re-use the converted view
                imageView = (ImageView) convertView;
            }

            // Finally load the image asynchronously into the ImageView, this also takes care of
            // setting a placeholder image while the background thread runs
            //mImageFetcher.loadImage(Images.imageThumbUrls[position - mNumColumns], imageView);
            //imageView.setLayoutParams(mImageViewLayoutParams);
            mImageFetcher.setImageSize(mImageThumbSize ,mImageThumbSize);
            mImageFetcher.loadImage(list.get(position), imageView);
            return imageView;
        }

    }


    public class ImageListAdapter extends BaseAdapter {
        List<Map.Entry<Long, List<String>>> list;
        Context context;
        ListView mListView;

        public ImageListAdapter(Context c, List<Map.Entry<Long, List<String>>> list, ListView mListView){
            this.list = list;
            this.context = c;
            this.mListView = mListView;
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
        public View getView(int index, View convertView, ViewGroup arg2) {
            ListHolder holder;
            if(null == convertView){
                holder = new ListHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.image_list_item, null);
                holder.textView = (TextView)convertView.findViewById(R.id.textView7);
                holder.gridView = (GridView)convertView.findViewById(R.id.gridView123);
                convertView.setTag(holder);
            }else{
                holder = (ListHolder) convertView.getTag();
            }
            long dateTime = list.get(index).getKey();
            SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
            holder.textView.setText(timeFormat.format(new Date(dateTime)));
            int mNumColumns = context.getResources().getDisplayMetrics().widthPixels/10*9/mImageThumbSize;
            holder.gridView.setNumColumns(mNumColumns);
            holder.gridView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
            holder.gridView.setAdapter(new GroupAdapter(Style1.this, list.get(index).getValue(), holder.gridView));
            holder.gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                    // Pause fetcher to ensure smoother scrolling when flinging
                    if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                        // Before Honeycomb pause image loading on scroll to help with performance
                        if (!Utils.hasHoneycomb()) {
                            mImageFetcher.setPauseWork(true);
                        }
                    } else {
                        mImageFetcher.setPauseWork(false);
                    }
                }

                @Override
                public void onScroll(AbsListView absListView, int firstVisibleItem,
                                     int visibleItemCount, int totalItemCount) {
                }
            });

            holder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(Style1.this, "OnClick at" + position, Toast.LENGTH_SHORT).show();
                }
            });

            int row = list.get(index).getValue().size()/mNumColumns;
            if(list.get(index).getValue().size()%mNumColumns != 0){
                row++;
            }
            //MyImageView imageView = (MyImageView)LayoutInflater.from(context).inflate(R.layout.image_item, null).findViewById(R.id.group_image); //mContext指的是调用的Activtty
            //LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) imageView.getLayoutParams();
            LinearLayout.LayoutParams textParams = (LinearLayout.LayoutParams) holder.textView.getLayoutParams();
            int height = row * mImageThumbSize + textParams.height + 80;
            convertView.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, height));
            return convertView;
        }

    }

    class ListHolder
    {
        public TextView textView;
        public GridView gridView;
    }
}
