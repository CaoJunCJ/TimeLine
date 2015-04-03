package com.brisktouch.timeline.util;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.*;
import java.util.Calendar;

/**
 * Created by jim on 4/1/2015.
 */
public class FileUtil {
    private File mainFolder;
    private File jsonFile;
    private String jsonFileName = "data.json";
    private String folderName = "/brisktouch";
    private static FileUtil instance;

    boolean canBeWritable;

    private FileUtil(){
        canBeWritable = Tool.isExternalStorageReadable()&&Tool.isExternalStorageWritable();
        if(mainFolder == null && canBeWritable){
            mainFolder = new File(Environment.getExternalStorageDirectory() + folderName);
            jsonFile = new File(mainFolder, jsonFileName);
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
}
