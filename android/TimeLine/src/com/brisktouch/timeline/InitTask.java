package com.brisktouch.timeline;

import android.content.Context;
import android.os.Handler;
import android.os.Environment;
import android.util.Log;
import com.brisktouch.timeline.util.Tool;
import org.json.JSONObject;

import java.io.*;

import java.io.File;

/**
 * Created by jim on welcome_background_2/9/2015.
 */
public class InitTask extends Thread {
    String tag = "InitTask";
    Context mContext;
    Handler mHandler;
    String folderName;
    String jsonFileName;
    JSONObject mJson;
    public InitTask(Context context, Handler handler, JSONObject json){
        this.mContext = context;
        this.mHandler = handler;
        this.mJson = json;
        this.folderName = "brisktouch";
        this.jsonFileName = "data.json";

    }

    public void run(){

        if(Tool.isExternalStorageReadable()&&Tool.isExternalStorageWritable()){
            File folder = new File(Environment.getExternalStorageDirectory() + "/brisktouch");
            Log.d(tag, folder.getPath());
            if(!folder.exists()){
                folder.mkdir();
            }
            File jsonFile = new File(folder, jsonFileName);
            if(!jsonFile.exists()){
                OutputStream os = null;
                try{
                    os = new FileOutputStream(jsonFile);
                    os.write(mJson.toString(1).getBytes());
                    os.flush();
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    try{
                        if(os!=null)
                            os.close();
                    }catch (Exception e){e.printStackTrace();}
                }

            }
        }

        if(mHandler!=null){
            mHandler.sendEmptyMessage(0);
        }
    }


}
