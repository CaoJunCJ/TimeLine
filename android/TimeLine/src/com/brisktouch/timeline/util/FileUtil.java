package com.brisktouch.timeline.util;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

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
}
