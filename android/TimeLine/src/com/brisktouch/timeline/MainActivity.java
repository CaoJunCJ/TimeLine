package com.brisktouch.timeline;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.MovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.brisktouch.timeline.util.Global;


/**
 * Created by jim on 2/10/2015.
 */
public class MainActivity extends Activity {

    private Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, MyActivity.class);
            startActivity(intent);
            finish();
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View wait = new View(this);
        wait.setBackgroundColor(Color.WHITE);
        setContentView(wait);
        SharedPreferences perPreferences = getSharedPreferences("TimeLineByJim", MODE_PRIVATE);
        InitializationApp.getInstance(this).initTypefaceFromAsset();
        boolean isFirstRun = perPreferences.getBoolean("isFirstUse", true);
        Log.d("isFirstRun:", isFirstRun + "");
        new InitTask( this, !isFirstRun ? handler : null, Global.getJsonData()).start();
        if (isFirstRun){
            SharedPreferences.Editor editor = perPreferences.edit();
            editor.putBoolean("isFirstUse", false);
            editor.commit();
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, WelcomeActivity.class);
            startActivity(intent);
            finish();
            return;
        }
    }
}
