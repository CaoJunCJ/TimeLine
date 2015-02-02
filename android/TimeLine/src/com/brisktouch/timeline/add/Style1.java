package com.brisktouch.timeline.add;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
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
import com.brisktouch.timeline.util.ImageResizer;
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
    AlertDialog alertView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //hide status bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.style1);
        SelectPic mClientListener = new SelectPic();
        findViewById(R.id.imageButton8).setOnClickListener(mClientListener);
        findViewById(R.id.imageButton9).setOnClickListener(mClientListener);
        findViewById(R.id.imageButton10).setOnClickListener(mClientListener);
        findViewById(R.id.imageButton11).setOnClickListener(mClientListener);
        ImageView assistive = (ImageView)findViewById(R.id.imageButton12);
        int mScreenWidth = this.getResources().getDisplayMetrics().widthPixels;
        int mScreenHeight = this.getResources().getDisplayMetrics().heightPixels;
        RelativeLayout.LayoutParams assistiveLayout = new RelativeLayout.LayoutParams(mScreenWidth*2/10 -10, mScreenWidth*2/10 -10);
        assistiveLayout.setMargins(mScreenWidth*7/10, mScreenHeight*8/10,0,0);
        assistive.setLayoutParams(assistiveLayout);
        assistive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Style1.this, "OPOPOOOHHIH", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void popSelectPictureDialog(int id){

        if(alertView == null){
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
            mImageListAdapter.setChangeImageId(id);
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


            alertView = new AlertDialog.Builder(Style1.this).setView(main).create();
        }

        if(alertView!=null){
            alertView.show();
        }


    }

    public class SelectPic implements View.OnClickListener {



        private Handler mHandler = new Handler(){
            public void handleMessage(Message msg){
                Bundle ble = msg.getData();
                popSelectPictureDialog(ble.getInt("id"));
            }
        };

        public SelectPic(){

            dateGruopMap = new HashMap<Long, List<String>>();
            folderGruopMap = new HashMap<String, List<String>>();
        }



        public void onClick(View v) {
            if(mImageListAdapter!=null){
                mImageListAdapter.setChangeImageId(v.getId());
            }

            Log.d(Style1.class.getName(),"onclick");
            if(alertView == null){
                getImage(v.getId());
            }else{
                popSelectPictureDialog(v.getId());
            }

        }

        private void getImage(final int id){
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

                    Message message = new Message();
                    Bundle ble = new Bundle();
                    ble.putInt("id", id);
                    message.setData(ble);
                    mHandler.sendMessage(message);
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
        int changeImageId;

        public ImageListAdapter(Context c, List<Map.Entry<Long, List<String>>> list, ListView mListView){
            this.list = list;
            this.context = c;
            this.mListView = mListView;
        }

        public void setChangeImageId(int id){
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
                    //Toast.makeText(Style1.this, "OnClick at" + position, Toast.LENGTH_SHORT).show();
                    String path = list.get(index).getValue().get(position);
                    Log.d("path:",path);
                    ImageButton mImageButton = (ImageButton)((Activity)context).findViewById(changeImageId);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    //options.inJustDecodeBounds = true;
                    //BitmapFactory.decodeFile(path, options);
                    //int height = mImageButton.getHeight();
                    //options.inSampleSize = ImageResizer.calculateInSampleSize(options, options.outWidth, height);
                    //options.inJustDecodeBounds = false;
                    Bitmap bitmap = BitmapFactory.decodeFile(path, options);
                    //int width = bitmap.getWidth();
                    mImageButton.setScaleType(ImageView.ScaleType.CENTER_CROP );
                    mImageButton.setImageBitmap(bitmap);
                    alertView.dismiss();
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
