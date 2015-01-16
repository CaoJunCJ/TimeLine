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
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.brisktouch.timeline.R;
import android.net.Uri;
import android.widget.AbsListView.LayoutParams;

import java.io.File;
import java.util.*;
import java.text.SimpleDateFormat;

/**
 * Created by cerosoft on 1/12/2015.
 */


//TODO must be Reconstruction!!!!!
public class Style1 extends Activity {

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

    public class SelectPic implements View.OnClickListener {

        HashMap<Long, List<String>> dateGruopMap;
        HashMap<String, List<String>> folderGruopMap;

        private Handler mHandler = new Handler(){
            public void handleMessage(Message msg){
                popSelectPictureDialog();
            }
        };

        public SelectPic(){

            dateGruopMap = new HashMap<Long, List<String>>();
            folderGruopMap = new HashMap<String, List<String>>();
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

            LinearLayout main = new LinearLayout(Style1.this);
            main.setOrientation(LinearLayout.VERTICAL);
            main.setBackgroundColor(Color.parseColor("#ffffffff"));
            ListView listView = new ListView(Style1.this);
            listView.setBackgroundColor(Color.parseColor("#ffffffff"));
            listView.setCacheColorHint(0);
            listView.setAdapter(new ImageListAdapter(Style1.this, infoIds, listView));
            main.addView(listView);
            /*
            ScrollView sv = new ScrollView(Style1.this);
            LinearLayout line = new LinearLayout(Style1.this);
            line.setOrientation(LinearLayout.VERTICAL);
            for (int i = 0; i < infoIds.size(); i++) {
                long time = infoIds.get(i).getKey();
                SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
                Log.d(Style1.class.getName(), timeFormat.format(new Date(time)));
                LinearLayout lt = new LinearLayout(Style1.this);
                lt.setOrientation(LinearLayout.VERTICAL);
                TextView tv = new TextView(Style1.this);
                LinearLayout.LayoutParams param =
                        new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,30);
                int row = infoIds.get(i).getValue().size()/3;
                if(infoIds.get(i).getValue().size()%3 != 0){
                    row++;
                }
                MyImageView imageView = (MyImageView)LayoutInflater.from(Style1.this).inflate(R.layout.image_item, null).findViewById(R.id.group_image); //mContext指的是调用的Activtty
                LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) imageView.getLayoutParams();
                int height = row * linearParams.height + 35;
                lt.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, height));
                tv.setLayoutParams(param);
                tv.setText(timeFormat.format(new Date(time)));
                GridView gv = new GridView(Style1.this);
                gv.setNumColumns(3);
                gv.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
                gv.setAdapter(new GroupAdapter(Style1.this, infoIds.get(i).getValue(), gv));

                lt.addView(tv);
                lt.addView(gv);
                line.addView(lt);
            }
            sv.addView(line);
            main.addView(sv);
            */

            new AlertDialog.Builder(Style1.this).setView(main).show();
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

        public GroupAdapter(Context c, List<String> list, GridView mGridView){
            this.list = list;
            this.context = c;
            this.mGridView = mGridView;
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
            final Holder holder;
            if(null == convertView){
                holder = new Holder();
                convertView = LayoutInflater.from(context).inflate(R.layout.image_item, null);
                holder.iamgeView = (MyImageView)convertView.findViewById(R.id.group_image);
                holder.iamgeView.setOnMeasureListener(new MyImageView.OnMeasureListener() {
                    @Override
                    public void onMeasureSize(int width, int height) {
                        pt.set(width, height);
                    }
                });
                convertView.setTag(holder);
            }else{
                holder = (Holder) convertView.getTag();
                holder.iamgeView.setImageResource(R.drawable.friends_sends_pictures_no);
            }
            String path = list.get(index);
            holder.iamgeView.setTag(path);


            final Bitmap bitmap = NativeImageLoader.getInstance().getBitmapFromMemCache(path);
            if (bitmap != null) {
                holder.iamgeView.setImageBitmap(bitmap);
            } else {
                holder.iamgeView.setImageResource(R.drawable.friends_sends_pictures_no);
                try{
                    BitmapWorkerTask task = new BitmapWorkerTask(holder.iamgeView);
                    task.execute(path);
                }catch (Exception e){
                    e.printStackTrace();
                    holder.iamgeView.setImageResource(R.drawable.friends_sends_pictures_no);
                }catch (OutOfMemoryError e){
                    e.printStackTrace();
                    holder.iamgeView.setImageResource(R.drawable.friends_sends_pictures_no);
                }
            }
            /*

            Bitmap bitmap = NativeImageLoader.getInstance().loadNativeImage(path, pt, new NativeImageLoader.NativeImageCallBack() {
                @Override
                public void onImageLoader(Bitmap bitmap, String path) {
                    MyImageView ib = (MyImageView)mGridView.findViewWithTag(path);
                    if(bitmap != null && ib != null){
                        ib.setImageBitmap(bitmap);
                    }
                }
            });

            if(bitmap != null){
                holder.iamgeView.setImageBitmap(bitmap);
            }else{
                holder.iamgeView.setImageResource(R.drawable.friends_sends_pictures_no);
            }
            */
            return convertView;
        }

    }
    class Holder
    {
        public MyImageView iamgeView;

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
                holder.gridView = (GridView)convertView.findViewById(R.id.gridView);
                convertView.setTag(holder);
            }else{
                holder = (ListHolder) convertView.getTag();
            }
            long dateTime = list.get(index).getKey();
            SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
            holder.textView.setText(timeFormat.format(new Date(dateTime)));
            holder.gridView.setNumColumns(4);
            holder.gridView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
            holder.gridView.setAdapter(new GroupAdapter(Style1.this, list.get(index).getValue(), holder.gridView));
            int row = list.get(index).getValue().size()/4;
            if(list.get(index).getValue().size()%4 != 0){
                row++;
            }
            MyImageView imageView = (MyImageView)LayoutInflater.from(context).inflate(R.layout.image_item, null).findViewById(R.id.group_image); //mContext指的是调用的Activtty
            LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) imageView.getLayoutParams();
            LinearLayout.LayoutParams textParams = (LinearLayout.LayoutParams) holder.textView.getLayoutParams();
            int height = row * linearParams.height + textParams.height + 80;
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
