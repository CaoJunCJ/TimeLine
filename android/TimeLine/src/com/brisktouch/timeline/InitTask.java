package com.brisktouch.timeline;

import android.content.Context;
import android.os.Handler;
import android.os.Environment;
import android.util.Log;
import com.brisktouch.timeline.util.Global;
import com.brisktouch.timeline.util.Tool;
import org.cjson.JSONArray;
import org.cjson.JSONObject;

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
                    "title":"title",
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
        /*
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
        */

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
                Log.d(tag, "file not exists , create");
                OutputStream os = null;
                try{
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(System.currentTimeMillis());
                    JSONArray data = new JSONArray();
                    JSONObject theFirstContext = new JSONObject();
                    JSONArray array = new JSONArray();
                    JSONObject j = new JSONObject();
                    j.put(Global.JSON_KEY_TITLE, "welcome to timeLine.");
                    j.put(Global.JSON_KEY_STYLE, "NONE");
                    j.put(Global.JSON_KEY_TIME, String.format("%s:%s:%s",
                            cal.get(Calendar.HOUR_OF_DAY),
                            cal.get(Calendar.MINUTE),
                            cal.get(Calendar.SECOND)));
                    array.put(j);
                    theFirstContext.put(Global.JSON_KEY_DATE, String.format("%s.%s.%s",
                            cal.get(Calendar.YEAR),
                            (cal.get(Calendar.MONTH)+1),
                            cal.get(Calendar.DAY_OF_MONTH)));
                    theFirstContext.put(Global.JSON_KEY_THINGS, array);
                    data.put(theFirstContext);
                    mJson.put(Global.JSON_KEY_DATA, data);

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
            }else{
                FileInputStream inputStream = null;
                try {
                    inputStream = new FileInputStream(jsonFile);
                    byte[] data = new byte[(int)jsonFile.length()];
                    inputStream.read(data);
                    mJson = new JSONObject(new String(data,"utf-8"));
                    Global.setJsonData(mJson);
                }catch (Exception e){e.printStackTrace();}
                finally {
                    if(inputStream!=null)
                        try {
                            inputStream.close();
                        }catch (Exception e){e.printStackTrace();}
                }
            }
        }

        if(mHandler!=null){
            mHandler.sendEmptyMessage(0);
        }
    }


}
