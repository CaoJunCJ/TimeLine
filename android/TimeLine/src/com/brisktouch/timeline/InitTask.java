package com.brisktouch.timeline;

import android.content.Context;
import android.os.Handler;
import android.os.Environment;
import android.util.Log;
import com.brisktouch.timeline.util.Tool;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;

import java.io.File;
import java.util.Calendar;

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

    /*
    the json data format is
    {
        "data":[
            {
                "date":"2012.12.5",
                "things":[
                    {
                    "context":"title",
                    "time":"12:11"
                    }
                ]
            }
        ]
    }
     */


    public InitTask(Context context, Handler handler, JSONObject json){
        this.mContext = context;
        this.mHandler = handler;
        this.mJson = json;
        this.folderName = "brisktouch";
        this.jsonFileName = "data.json";
        try{
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(System.currentTimeMillis());
            JSONArray data = new JSONArray();
            JSONObject theFirstContext = new JSONObject();
            JSONArray array = new JSONArray();
            JSONObject j = new JSONObject();
            j.put("context", "welcome to timeLine.");
            j.put("time",cal.get(cal.HOUR_OF_DAY)+":"+cal.get(cal.MINUTE));
            array.put(j);
            theFirstContext.put("date",cal.get(cal.YEAR)+"."+(cal.get(cal.MONTH)+1)+"."+cal.get(cal.DAY_OF_MONTH));
            theFirstContext.put("things",array);
            data.put(theFirstContext);
            mJson.put("data", data);
        }catch (Exception e){e.printStackTrace();}


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
