package com.brisktouch.timeline.util;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.*;
import java.text.Collator;
import java.util.*;

/**
 * Created by jim on 4/1/2015.
 */
public class FileUtil {
    private String TAG = "FileUtil";

    private File mainFolder;
    private File jsonFile;
    private File screenFolder;
    private String jsonFileName = "data.json";
    private String folderName = "/brisktouch";
    private String screenImage = "screenImage";
    private static FileUtil instance;

    boolean canBeWritable;

    private FileUtil(){
        canBeWritable = Tool.isExternalStorageReadable()&&Tool.isExternalStorageWritable();
        if(mainFolder == null && canBeWritable){
            mainFolder = new File(Environment.getExternalStorageDirectory() + folderName);
            jsonFile = new File(mainFolder, jsonFileName);
            screenFolder = new File(mainFolder, screenImage);
            if(!screenFolder.exists()){
                screenFolder.mkdir();
            }
        }
    }

    public static FileUtil getInstance(){
        if(instance == null){
            instance = new FileUtil();
        }
        return instance;
    }

    public boolean saveJsonToFile(){
        if(!canBeWritable)
            return false;
        OutputStream os = null;
        try{
            os = new FileOutputStream(jsonFile);
            os.write(Global.getJsonData().toString(1).getBytes());
            os.flush();
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                if(os!=null)
                    os.close();
            }catch (Exception e){e.printStackTrace();}
        }
        return false;
    }



    public boolean saveBitmap(Bitmap bitmap, String fileName, Calendar cal) {
        //Create a thread to run it.
        String year = Integer.toString(cal.get(Calendar.YEAR));
        String month = Integer.toString(cal.get(Calendar.MONTH) + 1);
        String day = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
        String hour = Integer.toString(cal.get(Calendar.HOUR_OF_DAY));
        String minute = Integer.toString(cal.get(Calendar.MINUTE));
        String second = Integer.toString(cal.get(Calendar.SECOND));

        if(month.length()==1){month = "0" + month;}
        if(day.length()==1){day = "0" + day;}
        if(hour.length()==1){hour = "0" + hour;}
        if(minute.length()==1){minute = "0" + minute;}
        if(second.length()==1){second = "0" + second;}

        String d = year + month + day;
        String s = hour + minute + second;

        File dFolder = new File(mainFolder, d);
        if(!dFolder.exists()){
            dFolder.mkdir();
        }

        File sFolder = new File(dFolder, s);
        if(!sFolder.exists()){
            sFolder.mkdir();
        }

        File imageFile = new File(sFolder, fileName);
        try {
            imageFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean saveBitmap(Bitmap bitmap, String name){
        File imageFile = new File(mainFolder, name);
        try {
            imageFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String saveScreenBitmap(Bitmap bitmap, String name){
        //Create a thread to run it.
        File imageFile = new File(screenFolder, name);
        try {
            imageFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            return imageFile.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<File> getImagesFilePath(String date, String time){
        String dates[] = date.split("\\.");
        String times[] = time.split(":");
        String year = dates[0];
        String month = dates[1];
        String day = dates[2];

        String hour = times[0];
        String minute = times[1];
        String second = times[2];

        if(month.length()==1){month = "0" + month;}
        if(day.length()==1){day = "0" + day;}
        if(hour.length()==1){hour = "0" + hour;}
        if(minute.length()==1){minute = "0" + minute;}
        if(second.length()==1){second = "0" + second;}

        String d = year + month + day;
        String s = hour + minute + second;
        File dFolder = new File(mainFolder, d);
        if(dFolder.exists()){
            File sFolder = new File(dFolder, s);
            if(sFolder.exists()){
                File[] files = sFolder.listFiles();
                List<File> list = Arrays.asList(files);
                Collections.sort(list, new Comparator<File>() {
                    @Override
                    public int compare(File o1, File o2) {
                        if (o1.isDirectory() && o2.isFile())
                            return -1;
                        if (o1.isFile() && o2.isDirectory())
                            return 1;
                        return o1.getName().compareTo(o2.getName());
                    }
                });
                return list;
            }
        }
        return null;
    }


}
