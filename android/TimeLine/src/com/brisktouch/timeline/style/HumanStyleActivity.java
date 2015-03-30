package com.brisktouch.timeline.style;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;
import com.brisktouch.timeline.R;
import com.brisktouch.timeline.add.EditWordUtil;
import com.brisktouch.timeline.util.Global;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by jim on 3/30/2015.
 */
public class HumanStyleActivity extends BaseStyleActivity {
    String TAG = "HumanStyle";
    boolean isDisplayButton = false;

    boolean isDisplayContextEditWord = false;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //hide status bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.human_style);

        SelectPic mClientListener = new SelectPic();
        findViewById(R.id.imageButton8).setOnClickListener(mClientListener);
        findViewById(R.id.imageButton9).setOnClickListener(mClientListener);
        findViewById(R.id.imageButton10).setOnClickListener(mClientListener);
        findViewById(R.id.imageButton11).setOnClickListener(mClientListener);
        RelativeLayout rt = (RelativeLayout)findViewById(R.id.relativeLayout_style);

        /*
            assistive touch button
         */
        rt.addView(assistive);

        rt.addView(backButton,1);

        rt.addView(shareButton,1);

        rt.addView(saveButton,1);



        // edit word ui
        final ScrollView sv = (ScrollView)findViewById(R.id.scrollView2);
        final LinearLayout linear_style = (LinearLayout)findViewById(R.id.linear_style);
        final TextView wordContext = (TextView)findViewById(R.id.textView3);
        final TextView wordTitle = (TextView)findViewById(R.id.textView2);

        final EditWordUtil editWordUtil = new EditWordUtil(this);

        final View editWordView = editWordUtil.getEditWordView();
        editWordView.setVisibility(View.GONE);
        linear_style.addView(editWordView);

        wordContext.setOnClickListener(new WordOnclickListener(editWordUtil, editWordView, sv));
        wordTitle.setOnClickListener(new WordOnclickListener(editWordUtil, editWordView, sv));

    }

    public void share(){

    }

    public class WordOnclickListener implements View.OnClickListener{
        EditWordUtil editWordUtil;
        View editWordView;
        ScrollView sv;
        public WordOnclickListener(EditWordUtil editWordUtil, View editWordView, ScrollView sv){
            this.editWordUtil = editWordUtil;
            this.editWordView = editWordView;
            this.sv = sv;
        }

        public void onClick(View v) {
            TextView textView = (TextView)v;
            editWordUtil.setCurrentSelectTextView(textView);
            if(!isDisplayContextEditWord){
                editWordView.setVisibility(View.VISIBLE);
                EditText et = (EditText) editWordView.findViewById(R.id.editText);
                et.setText(textView.getText());
                sv.post(new Runnable() {
                    public void run() {
                        sv.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
                isDisplayContextEditWord = true;
            }else {
                editWordView.setVisibility(View.GONE);
                isDisplayContextEditWord = false;
            }
        }
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
                String today = cal.get(cal.YEAR) + "." + (cal.get(cal.MONTH) + 1) + "." + cal.get(cal.DAY_OF_MONTH);
                if (dateString.equals(today)) {
                    JSONObject add = new JSONObject();
                    add.put("style","style1");

                    add.put("context", ((TextView) findViewById(R.id.textView2)).getText().toString());
                    add.put("time",cal.get(cal.HOUR_OF_DAY)+":"+cal.get(cal.MINUTE));
                    JSONArray jsonThings = jsonItem.optJSONArray("things");
                    jsonThings.put(add);
                    Log.d(TAG, jsonObject.toString(1));
                    Toast.makeText(getApplication(), "Save Success", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }catch (Exception e){e.printStackTrace();}
    }
}
