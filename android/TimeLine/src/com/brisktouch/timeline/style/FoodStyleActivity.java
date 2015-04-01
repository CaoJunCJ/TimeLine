package com.brisktouch.timeline.style;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.brisktouch.timeline.R;
import com.brisktouch.timeline.add.EditWordUtil;

/**
 * Created by jim on 3/30/2015.
 */
public class FoodStyleActivity extends BaseStyleActivity{
    String TAG = "FoodStyleActivity";
    LinearLayout foodStyleLinearLayout;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        foodStyleLinearLayout = (LinearLayout) LayoutInflater.from(getApplication()).inflate(R.layout.food_style, null);
        foodStyleLinearLayout.findViewById(R.id.foodLinearLayout1).setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, mScreenHeight/2));
        foodStyleLinearLayout.findViewById(R.id.foodLinearLayout2).setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, mScreenHeight / 2));

        scrollView.addView(foodStyleLinearLayout);

        setContentView(maxOutsideLayout);

        addAssistiveTouchButton();

        SelectPic mClientListener = new SelectPic();
        findViewById(R.id.imageView16).setOnClickListener(mClientListener);
        findViewById(R.id.imageView19).setOnClickListener(mClientListener);

        initEditWordView();

        foodStyleLinearLayout.addView(editWordView);

        TextView title = (TextView)findViewById(R.id.textView17);
        TextView authorName = (TextView)findViewById(R.id.textView18);

        String authorNameString = getResources().getString(R.string.author_name);
        String titleString = getResources().getString(R.string.sense_of_taste);

        String result = "";
        boolean isChineseCharacter = EditWordUtil.isChineseCharacter(titleString);
        if(isChineseCharacter){
            char[] chars = titleString.toCharArray();
            for(int i=0;i<chars.length;i++){
                if(i==chars.length-1){
                    result = result + chars[i];
                }else{
                    result = result + chars[i] + '\n';
                }
            }
        }else{
            String[] strings = titleString.split("\\s+");
            for(int i=0;i<strings.length;i++){
                if(i==strings.length-1){
                    result = result + strings[i];
                }else{
                    result = result + strings[i] + '\n';
                }
            }
        }

        title.setText(result);

        result = "";
        char[] chars = authorNameString.toCharArray();
        for(int i=0;i<chars.length;i++){
            result = result + chars[i] + '\n';
        }

        authorName.setText(result);

        title.setOnClickListener(new CustomWordOnclickListener(editWordUtil, editWordView, scrollView, false));
        authorName.setOnClickListener(new CustomWordOnclickListener(editWordUtil, editWordView, scrollView, true));
    }

    public class CustomWordOnclickListener implements View.OnClickListener{
        EditWordUtil editWordUtil;
        View editWordView;
        ScrollView sv;
        boolean isChar;
        public CustomWordOnclickListener(EditWordUtil editWordUtil, View editWordView, ScrollView sv, boolean isChar){
            this.editWordUtil = editWordUtil;
            this.editWordView = editWordView;
            this.sv = sv;
            this.isChar = isChar;
        }

        public void onClick(View v) {
            TextView textView = (TextView)v;
            editWordUtil.setCurrentSelectTextView(textView);
            if(isChar){
                editWordUtil.setTextAction(EditWordUtil.TextViewAction.SplitChar);
            }else{
                editWordUtil.setTextAction(EditWordUtil.TextViewAction.SplitWord);
            }
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

    @Override
    public void save() {

    }

    @Override
    public void share() {

    }
}
