package com.brisktouch.timeline;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.view.WindowManager;
import com.brisktouch.timeline.style.StyleActivity;
import com.brisktouch.timeline.util.Global;
import org.cjson.JSONObject;
import android.widget.Button;
import android.content.Intent;
import android.view.View;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    String TAG = "MyActivity";

    ListAdapter adapter;
    ListView listview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //JSONObject json = TestData.getRandomData();
        JSONObject json = Global.getJsonData();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        listview = new ListView(this);
        //listview.setVerticalScrollBarEnabled(true);
        adapter = new ListAdapter(json, this);
        listview.setBackgroundColor(Color.WHITE);
        listview.setBackgroundColor(Color.parseColor("#F9F9F9"));
        listview.setAdapter(adapter);
        listview.setDivider(null);
        adapter.setListView(listview);
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
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                //finish();
            }

        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        if(adapter!=null){
            adapter.setData(Global.getJsonData());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");

    }
}
