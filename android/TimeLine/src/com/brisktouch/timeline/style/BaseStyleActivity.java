package com.brisktouch.timeline.style;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.brisktouch.timeline.MyActivity;
import com.brisktouch.timeline.R;
import com.brisktouch.timeline.add.EditWordUtil;
import com.brisktouch.timeline.add.IterateViewGroup;
import com.brisktouch.timeline.add.StyleActivity;
import com.brisktouch.timeline.custom.BrowseNativeImageUtil;
import com.brisktouch.timeline.custom.CircleButton;
import com.brisktouch.timeline.custom.DragImageView;
import com.brisktouch.timeline.custom.PopButtonOnClickListener;
import com.brisktouch.timeline.ui.RecyclingImageView;
import com.brisktouch.timeline.util.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by jim on 3/30/2015.
 */
public abstract class BaseStyleActivity extends Activity {
    String TAG = "BaseStyle";
    //HashMap<Long, List<String>> dateGruopMap;
    //HashMap<String, List<String>> folderGruopMap;
    //private int mImageThumbSize;
    //private int mImageThumbSpacing;
    //private ImageNative mImageFetcher;
    //ListView listView;
    //ImageListAdapter mImageListAdapter;
    //AlertDialog alertView;
    ImageView assistive;
    ImageView saveButton;
    ImageView backButton;
    ImageView shareButton;
    public RelativeLayout maxOutsideLayout;
    //ScrollView scrollView;
    LinearLayout scrollView;
    EditWordUtil editWordUtil;
    View editWordView;
    WordOnclickListener wordOnclickListener;

    boolean isDisplayContextEditWord = false;

    int mScreenWidth;
    int mScreenHeight;

    SelectPic mClientListener;

    BrowseNativeImageUtil browseNativeImageUtil;

    View browseNativeImageView;

    public boolean nativeImageListDisplay = false;

    public BaseStyleActivity(){
    }

    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        //hide title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //hide status bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mScreenWidth = getResources().getDisplayMetrics().widthPixels;
        mScreenHeight = getResources().getDisplayMetrics().heightPixels;

        maxOutsideLayout = new RelativeLayout(getApplication());
        maxOutsideLayout.setBackgroundColor(Color.WHITE);
        maxOutsideLayout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        //scrollView = new ScrollView(getApplication());
        scrollView = new LinearLayout(getApplication());
        scrollView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        maxOutsideLayout.addView(scrollView);
        mClientListener = new SelectPic();
        addBrowseNativeImageView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(browseNativeImageUtil!=null)
            browseNativeImageUtil.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(browseNativeImageUtil!=null)
            browseNativeImageUtil.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(browseNativeImageUtil!=null)
            browseNativeImageUtil.onDestroy();

    }

    public void initEditWordView(){

        editWordUtil = new EditWordUtil(this);

        editWordView = editWordUtil.getEditWordView();
        editWordView.setVisibility(View.GONE);

        wordOnclickListener = new WordOnclickListener(editWordUtil, editWordView, scrollView);

    }

    public void addAssistiveTouchButton(){
        initAssistiveTouchButton();
        maxOutsideLayout.addView(assistive);

        maxOutsideLayout.addView(backButton,1);

        maxOutsideLayout.addView(shareButton,1);

        maxOutsideLayout.addView(saveButton,1);
    }

    public void initAssistiveTouchButton(){
        /*
            assistive touch button
         */
        assistive = new DragImageView(getApplication(), null);
        assistive.setBackgroundResource(R.drawable.assistivetouch);

        RelativeLayout.LayoutParams assistiveLayout = new RelativeLayout.LayoutParams(mScreenWidth*2/10 -10, mScreenWidth*2/10 -10);
        assistiveLayout.setMargins(mScreenWidth*7/10, mScreenHeight*8/10,0,0);
        assistive.setLayoutParams(assistiveLayout);
        backButton = new CircleButton(this);
        backButton.setLayoutParams(assistiveLayout);
        backButton.setImageResource(R.drawable.ic_action_undo);
        backButton.setVisibility(View.INVISIBLE);
        //
        //rt.addView(iv1,1);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO should be create a stack for save activity ,when click back ,pop a activity from stack.
                Toast.makeText(getApplication(), "Onclick at back", Toast.LENGTH_SHORT).show();
                back();
            }
        });

        saveButton = new CircleButton(this);
        saveButton.setLayoutParams(assistiveLayout);
        saveButton.setImageResource(R.drawable.ic_action_save);
        saveButton.setVisibility(View.INVISIBLE);
        //rt.addView(iv2,1);
        //
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplication(), "Onclick at save", Toast.LENGTH_SHORT).show();
                save();
            }
        });

        shareButton = new CircleButton(this);
        shareButton.setLayoutParams(assistiveLayout);
        shareButton.setImageResource(R.drawable.ic_action_share);
        shareButton.setVisibility(View.INVISIBLE);
        //rt.addView(iv3,1);
        //
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplication(), "Onclick at share, not implement", Toast.LENGTH_SHORT).show();
                share();
            }
        });


        PopButtonOnClickListener popListener = new PopButtonOnClickListener(backButton,saveButton,shareButton);
        assistive.setOnClickListener(popListener);
    }

    public void setAllImageViewOnClickListener(ViewGroup viewGroup){
        //use setAllImageViewAndTextViewOnClickListener(ViewGroup viewGroup);
        throw new RuntimeException("Not implements");
    }

    public void setAllTextViewOnClickListener(ViewGroup viewGroup){
        throw new RuntimeException("Not implements");
    }

    public void setAllImageViewAndTextViewOnClickListener(ViewGroup viewGroup){
        for(int i = 0; i < viewGroup.getChildCount(); i++){
            View view = viewGroup.getChildAt(i);
            if(view instanceof ViewGroup){
                setAllImageViewAndTextViewOnClickListener((ViewGroup) view);
            }

            if(view instanceof ImageView){
                view.setOnClickListener(mClientListener);
            }

            if(view instanceof TextView){
                view.setOnClickListener(wordOnclickListener);
            }
        }
    }

    public abstract void share();
    public abstract void save();
    public void save(String style, String title, ViewGroup viewGroup){
        //maybe create two async task to do it.

        //save imagefile to sdcard
        ArrayList<ImageView> list = getAllImageViewFromViewGroup(viewGroup);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        for(int i = 0; i < list.size(); i++){
            Bitmap image = ((BitmapDrawable)list.get(i).getDrawable()).getBitmap();
            FileUtil.getInstance().saveBitmap(image, i+".jpg", cal);
        }

        ArrayList<JSONObject> strings = getAllTextViewStringFromViewGroup(viewGroup);

        //modify json data and save to sdcard
        try {
            JSONObject jsonObject = Global.getJsonData();
            JSONArray jsonData = jsonObject.optJSONArray(Global.JSON_KEY_DATA);
            //dateString = xxxx.xx.xx eg. 2001.12.24
            String today = String.format("%s.%s.%s",
                    cal.get(Calendar.YEAR),
                    (cal.get(Calendar.MONTH) + 1),
                    cal.get(Calendar.DAY_OF_MONTH)
            );
            for (int i = 0; i < jsonData.length(); i++) {
                JSONObject jsonItem = jsonData.optJSONObject(i);
                String dateString = jsonItem.optString(Global.JSON_KEY_DATE);
                if (dateString.equals(today)) {
                    JSONObject add = new JSONObject();
                    add.put(Global.JSON_KEY_STYLE, style);
                    add.put(Global.JSON_KEY_TITLE, title);
                    add.put(Global.JSON_KEY_TIME,String.format("%s:%s:%s",
                            cal.get(Calendar.HOUR_OF_DAY),
                            cal.get(Calendar.MINUTE),
                            cal.get(Calendar.SECOND)));
                    JSONArray jsonArrayStrings = new JSONArray(strings);
                    add.put(Global.JSON_KEY_STRINGS,jsonArrayStrings );

                    JSONArray jsonThings = jsonItem.optJSONArray(Global.JSON_KEY_THINGS);
                    jsonThings.put(add);
                    Log.d(TAG, jsonObject.toString(1));
                    FileUtil.getInstance().saveJsonToFile();
                    Toast.makeText(getApplication(), "Save Success", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            JSONObject newDate = new JSONObject();
            newDate.put(Global.JSON_KEY_DATE, today);
            JSONArray newThings = new JSONArray();
            JSONObject newTime = new JSONObject();
            newTime.put(Global.JSON_KEY_STYLE,style);

            newTime.put(Global.JSON_KEY_TITLE, title);
            newTime.put(Global.JSON_KEY_TIME, String.format("%s:%s:%s",
                    cal.get(Calendar.HOUR_OF_DAY),
                    cal.get(Calendar.MINUTE),
                    cal.get(Calendar.SECOND)));
            JSONArray jsonArrayStrings = new JSONArray(strings);
            newTime.put(Global.JSON_KEY_STRINGS, jsonArrayStrings );

            newThings.put(newTime);
            newDate.put(Global.JSON_KEY_THINGS, newThings);
            jsonData.put(newDate);
            Log.d(TAG, jsonObject.toString(1));
            FileUtil.getInstance().saveJsonToFile();
            Toast.makeText(getApplication(), "Save Success", Toast.LENGTH_SHORT).show();
            return;

        }catch (Exception e){e.printStackTrace();}
    }
    public void back(){
        Intent intent = new Intent();
        intent.setClass(this, StyleActivity.class);
        startActivity(intent);
        finish();
    }




    public class WordOnclickListener implements View.OnClickListener{
        EditWordUtil editWordUtil;
        View editWordView;
        ViewGroup sv;
        public WordOnclickListener(EditWordUtil editWordUtil, View editWordView, ViewGroup sv){
            this.editWordUtil = editWordUtil;
            this.editWordView = editWordView;
            this.sv = sv;
        }

        public void onClick(View v) {
            TextView textView = (TextView)v;
            editWordUtil.setCurrentSelectTextView(textView);
            if(!isDisplayContextEditWord){
                editWordView.setVisibility(View.VISIBLE);
                EditText et = (EditText) editWordView.findViewById(R.id.editText);

                String value = textView.getText().toString();

                et.setText(value);



                int w = mScreenWidth * 10/12 - 50;

                et.measure(0, 0);
                int height = et.getMeasuredHeight();
                int width = et.getMeasuredWidth();
                int h = (int)((float)width/w * et.getLineHeight() );
                if(h>190)
                    h = 190;


                /*sv.post(new Runnable() {
                    public void run() {
                        sv.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });*/
                editWordView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 290 + h));
                ((LinearLayout)sv.getChildAt(0)).setGravity(Gravity.BOTTOM);
                isDisplayContextEditWord = true;
            }else {
                ((LinearLayout)sv.getChildAt(0)).setGravity(Gravity.TOP);
                editWordView.setVisibility(View.GONE);
                isDisplayContextEditWord = false;
            }
        }
    }

    public void addBrowseNativeImageView(){
        browseNativeImageUtil = new BrowseNativeImageUtil(this);
    }

    public class SelectPic implements View.OnClickListener {
        public void onClick(View v) {
            if(!browseNativeImageUtil.isInit()){
                browseNativeImageUtil.init();
                maxOutsideLayout.addView(browseNativeImageUtil.getView());
            }

            if(!nativeImageListDisplay){
                browseNativeImageUtil.showView();
                browseNativeImageUtil.setChangeImageId(v.getId());
                nativeImageListDisplay = true;
            }else{
                browseNativeImageUtil.hideView();
                browseNativeImageUtil.setChangeImageId(v.getId());
                nativeImageListDisplay = false;
            }

        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(nativeImageListDisplay){
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                browseNativeImageUtil.hideView();
                nativeImageListDisplay = false;
                return false;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    int textViewIndex = 0;
    int imageViewIndex = 0;

    public void reLoadByJsonData(ViewGroup viewGroup){
        Intent intent = getIntent();
        IntentObjectData intentObjectData = (IntentObjectData)intent.getSerializableExtra("data");
        if(intentObjectData!=null){
            final ArrayList<String[]> strings = (ArrayList<String[]>)intentObjectData.list;
            final String time = intentObjectData.time;
            final String date = intentObjectData.date;
            final List<File> files = FileUtil.getInstance().getImagesFilePath(date, time);
            IterateViewGroup.iterateViewGroup(viewGroup,
                    new IterateViewGroup.IterateViewCallBack(){
                        public void callBack(View v){
                            //text view call back
                            TextView textView = (TextView)v;
                            String[] item = strings.get(textViewIndex);
                            String text = item[0];
                            String family = item[1];
                            float size = Float.parseFloat(item[2]);
                            int color = Integer.parseInt(item[3]);
                            textView.setText(text);
                            textView.setTextColor(color);
                            textView.setTextSize(size);
                            if(!family.equals("DEFAULT")){
                                Typeface tf = Typeface.createFromAsset(getAssets(), family);
                                textView.setTypeface(tf);
                            }
                            textViewIndex++;
                        }
                    },
                    new IterateViewGroup.IterateViewCallBack(){
                        public void callBack(View v){
                            //image view call back
                            ImageView imageView = (ImageView)v;
                            imageView.setImageBitmap(BitmapFactory.decodeFile(files.get(imageViewIndex).getAbsolutePath()));
                            imageViewIndex++;
                        }
                    }
            );
        }
    }



    /*

    public void popSelectPictureDialog(int id){

        if(alertView == null){
            List<Map.Entry<Long, List<String>>> infoIds = new ArrayList<Map.Entry<Long, List<String>>>(dateGruopMap.entrySet());
            Collections.sort(infoIds, new Comparator<Map.Entry<Long, List<String>>>() {
                public int compare(Map.Entry<Long, List<String>> o1,
                                   Map.Entry<Long, List<String>> o2) {
                    return (o1.getKey()).compareTo(o2.getKey());
                }
            });
            Log.d(TAG,"infoIds size:"+infoIds.size());

            mImageThumbSize = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_size);
            mImageThumbSpacing = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_spacing);
            ImageCache.ImageCacheParams cacheParams = new ImageCache.ImageCacheParams();
            cacheParams.setMemCacheSizePercent(0.25f);
            mImageFetcher = new ImageNative(this, mImageThumbSize);
            mImageFetcher.setLoadingImage(R.drawable.empty_photo);
            mImageFetcher.addImageCache(cacheParams);

            LinearLayout main = new LinearLayout(getApplication());
            main.setOrientation(LinearLayout.VERTICAL);
            main.setBackgroundColor(Color.parseColor("#ffffffff"));

            listView = new ListView(getApplication());
            listView.setBackgroundColor(Color.parseColor("#ffffffff"));
            listView.setCacheColorHint(0);
            mImageListAdapter = new ImageListAdapter(this, infoIds, listView);
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


            alertView = new AlertDialog.Builder(this).setView(main).create();
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

            Log.d(TAG,"onclick");
            if(alertView == null){
                getImage(v.getId());
            }else{
                popSelectPictureDialog(v.getId());
            }

        }

        private void getImage(final int id){
            if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                Toast.makeText(getApplication(), "Not External Storage Card", Toast.LENGTH_SHORT).show();
                return;
            }

            //maybe show progress dialog?

            new Thread(new Runnable() {
                @Override
                public void run() {
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
                    GridView.LayoutParams.WRAP_CONTENT, mImageThumbSize);
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

            // Finally load the image asynchronously into the ImageView, this also takes care of
            // setting a placeholder image while the background thread runs
            //mImageFetcher.loadImage(Images.imageThumbUrls[position - mNumColumns], imageView);
            //imageView.setLayoutParams(mImageViewLayoutParams);
            mImageFetcher.setImageSize(mImageThumbSize ,mImageThumbSize);
            imageView.setBackgroundColor(Color.GREEN);
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
            holder.gridView.setLayoutParams(new LinearLayout.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT));
            holder.gridView.setAdapter(new GroupAdapter(getApplication(), list.get(index).getValue(), holder.gridView));
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
                    Log.d("path:", path);
                    ImageView mImageButton = (ImageView)((Activity)context).findViewById(changeImageId);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    //options.inJustDecodeBounds = true;
                    //BitmapFactory.decodeFile(path, options);
                    //int height = mImageButton.getHeight();
                    //options.inSampleSize = ImageResizer.calculateInSampleSize(options, options.outWidth, height);
                    //options.inJustDecodeBounds = false;
                    Bitmap bitmap = BitmapFactory.decodeFile(path, options);
                    //int width = bitmap.getWidth();
                    mImageButton.setBackgroundColor(Color.WHITE);
                    mImageButton.setScaleType(ImageView.ScaleType.FIT_XY );
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
            convertView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, height));
            return convertView;
        }
    }

    class ListHolder
    {
        public TextView textView;
        public GridView gridView;
    }
    */

    public ArrayList<ImageView> getAllImageViewFromViewGroup(ViewGroup group){
        ArrayList<ImageView> list = new ArrayList<ImageView>();
        iterateViewGroupFindImageView(group, list);
        return list;
    }

    public void iterateViewGroupFindImageView(ViewGroup group, ArrayList<ImageView> list){
        for(int i = 0; i < group.getChildCount(); i++){
            View view = group.getChildAt(i);
            if(view instanceof ViewGroup){
                iterateViewGroupFindImageView((ViewGroup)view, list);
            }

            if(view instanceof ImageView){
                list.add((ImageView)view);
            }
        }
    }

    public ArrayList<JSONObject> getAllTextViewStringFromViewGroup(ViewGroup group){
        ArrayList<JSONObject> list = new ArrayList<JSONObject>();
        iterateViewGroupFindTextView(group, list);
        return list;
    }


    public void iterateViewGroupFindTextView(ViewGroup group, ArrayList<JSONObject> list){
        for(int i = 0; i < group.getChildCount(); i++){
            View view = group.getChildAt(i);
            if(view instanceof ViewGroup){
                iterateViewGroupFindTextView((ViewGroup) view, list);
            }

            if(view instanceof TextView){
                try {
                    TextView textView = (TextView) view;
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(Global.JSON_KEY_FONT_TEXT, textView.getText().toString());
                    jsonObject.put(Global.JSON_KEY_FONT_SIZE, textView.getTextSize());
                    String fontFamily = (String)textView.getTag();
                    if(fontFamily==null)
                        fontFamily = "DEFAULT";
                    jsonObject.put(Global.JSON_KEY_FONT_FAMILY, fontFamily);
                    jsonObject.put(Global.JSON_KEY_FONT_COLOR, textView.getCurrentTextColor());
                    list.add(jsonObject);
                }catch (Exception e){e.printStackTrace();}
            }
        }
    }

    /*
    Typeface[] typeFaceData = null;

    public String getFamilyByTypeFace(Typeface typeface){
        if(typeFaceData == null){
            Typeface t1 = Typeface.DEFAULT;
            Typeface t2 = Typeface.createFromAsset(getApplicationContext().getAssets(), "Fonts/zh_cn/MFTheGoldenEra_Noncommercial-Light.otf");
            Typeface t3 = Typeface.createFromAsset(getApplicationContext().getAssets(), "Fonts/zh_cn/MFPinSong_Noncommercial-Regular.otf");
            Typeface t4 = Typeface.createFromAsset(getApplicationContext().getAssets(), "Fonts/zh_cn/MFQingShu_Noncommercial-Regular.otf");
            typeFaceData = new Typeface[]{t1, t2, t3, t4};
        }

        if(typeface == null){
            return "DEFAULT";
        }

        if(typeface.equals(typeFaceData[0])){
            return "DEFAULT";
        }

        if(typeface.equals(typeFaceData[1])){
            return "Fonts/zh_cn/MFTheGoldenEra_Noncommercial-Light.otf";
        }

        if(typeface.equals(typeFaceData[2])){
            return "Fonts/zh_cn/MFPinSong_Noncommercial-Regular.otf";
        }

        if(typeface.equals(typeFaceData[3])){
            return "Fonts/zh_cn/MFQingShu_Noncommercial-Regular.otf";
        }

        return "DEFAULT";
    }
    */
}
