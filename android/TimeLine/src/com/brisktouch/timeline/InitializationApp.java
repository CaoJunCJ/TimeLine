package com.brisktouch.timeline;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import com.brisktouch.timeline.util.Global;

import java.util.Stack;

/**
 * Created by jim on 4/16/2015.
 */
public class InitializationApp {
    private Context context;
    private static InitializationApp instance;

    private InitializationApp(Context context){this.context = context;}

    public static InitializationApp getInstance(Context context){
        if(instance == null){
            instance = new InitializationApp(context);
        }
        return  instance;
    }

    public void initTypefaceFromAsset(){
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Global.typeFaceData[0] = Typeface.DEFAULT;
                Global.typeFaceData[1] = Typeface.createFromAsset(context.getAssets(), "Fonts/zh_cn/MFTheGoldenEra_Noncommercial-Light.otf");
                Global.typeFaceData[2] = Typeface.createFromAsset(context.getAssets(), "Fonts/zh_cn/MFPinSong_Noncommercial-Regular.otf");
                Global.typeFaceData[3] = Typeface.createFromAsset(context.getAssets(), "Fonts/zh_cn/MFQingShu_Noncommercial-Regular.otf");
            }
        };
        new Thread(r).start();
    }

}
