package com.brisktouch.timeline;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.view.WindowManager;
import com.brisktouch.timeline.add.StyleActivity;
import com.brisktouch.timeline.test.TestData;
import org.json.JSONObject;
import android.widget.Button;
import android.content.Intent;
import android.view.View;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    String TAG = "MyActivity";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JSONObject json = TestData.getRandomData();
            //setContentView(R.layout.main);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            ListView listview = new ListView(this);

            BaseAdapter adapter = new ListAdapter(json, this);
            listview.setAdapter(adapter);
            listview.setDivider(null);
            requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
            setContentView(listview);
            getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.mycustomtitle);
            Button btn1 =(Button)findViewById(R.id.header_right_btn);
            btn1.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(MyActivity.this, StyleActivity.class);
                    startActivity(intent);
                    //finish();
                }

            });
        new InitTask(this,null,json).start();
    }
}
