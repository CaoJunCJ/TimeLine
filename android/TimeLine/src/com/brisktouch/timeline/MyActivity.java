package com.brisktouch.timeline;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.brisktouch.timeline.test.TestData;
import org.json.JSONObject;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    String TAG = "MyActivity";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        ListView listview = new ListView(this);
        JSONObject json = TestData.getRandomData();
        BaseAdapter adapter = new ListAdapter(json, this);
        listview.setAdapter(adapter);
        listview.setDivider(null);
        setContentView(listview);
    }
}
