package com.brisktouch.timeline.style;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;
import com.brisktouch.timeline.R;
import com.brisktouch.timeline.add.EditWordUtil;
import com.brisktouch.timeline.util.FileUtil;
import com.brisktouch.timeline.util.Global;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by jim on 3/30/2015.
 */
public class HumanStyleActivity extends BaseStyleActivity {

    String TAG = "HumanStyleActivity";
    LinearLayout humanStyleLinearLayout;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        humanStyleLinearLayout = (LinearLayout)LayoutInflater.from(getApplication()).inflate(R.layout.human_style, null);

        scrollView.addView(humanStyleLinearLayout);

        setContentView(maxOutsideLayout);

        addAssistiveTouchButton();

        SelectPic mClientListener = new SelectPic();
        findViewById(R.id.imageButton8).setOnClickListener(mClientListener);
        findViewById(R.id.imageButton9).setOnClickListener(mClientListener);
        findViewById(R.id.imageButton10).setOnClickListener(mClientListener);
        findViewById(R.id.imageButton11).setOnClickListener(mClientListener);

        initEditWordView();

        humanStyleLinearLayout.addView(editWordView);

        final TextView wordContext = (TextView)findViewById(R.id.textView3);
        final TextView wordTitle = (TextView)findViewById(R.id.textView2);

        wordContext.setOnClickListener(wordOnclickListener);
        wordTitle.setOnClickListener(wordOnclickListener);

    }



    public void share(){

    }

    public void save(){
        try {
            JSONObject jsonObject = Global.getJsonData();
            JSONArray jsonData = jsonObject.optJSONArray("data");
            for (int i = 0; i < jsonData.length(); i++) {
                JSONObject jsonItem = jsonData.optJSONObject(i);
                String dateString = jsonItem.optString("date");
                //String dateTemp[] = dateString.split("\\.");
                //dateString = xxxx.xx.xx eg. 2001.12.24
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(System.currentTimeMillis());
                String today = cal.get(Calendar.YEAR) + "." + (cal.get(Calendar.MONTH) + 1) + "." + cal.get(Calendar.DAY_OF_MONTH);
                if (dateString.equals(today)) {
                    JSONObject add = new JSONObject();
                    add.put("style",TAG);

                    add.put("context", ((TextView) findViewById(R.id.textView2)).getText().toString());
                    add.put("time",cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE)+":"+cal.get(Calendar.SECOND));
                    JSONArray jsonThings = jsonItem.optJSONArray("things");
                    jsonThings.put(add);
                    Log.d(TAG, jsonObject.toString(1));
                    FileUtil.getInstance().saveJsonToFile();
                    Toast.makeText(getApplication(), "Save Success", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }catch (Exception e){e.printStackTrace();}
    }
}
