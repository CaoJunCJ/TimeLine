package com.brisktouch.timeline;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.brisktouch.timeline.util.Global;

/**
 * Created by jim on 2/10/2015.
 */
public class MainActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences perPreferences = getSharedPreferences("TimeLineByJim", MODE_PRIVATE);
        new InitTask(this,null, Global.getJsonData()).start();
        InitializationApp.getInstance(this).initTypefaceFromAsset();
        if (perPreferences.getBoolean("isFirstUse", false)) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, MyActivity.class);
            startActivity(intent);
            finish();
        }else{
            SharedPreferences.Editor editor = perPreferences.edit();
            editor.putBoolean("isFirstUse", false);
            editor.commit();
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, WelcomeActivity.class);
            startActivity(intent);
            finish();
        }

    }
}
