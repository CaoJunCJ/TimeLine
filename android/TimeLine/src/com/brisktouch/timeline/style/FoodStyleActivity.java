package com.brisktouch.timeline.style;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.brisktouch.timeline.R;
import com.brisktouch.timeline.custom.EditWordUtil;

/**
 * Created by jim on 3/30/2015.
 */
public class FoodStyleActivity extends BaseStyleActivity{
    String TAG = "FoodStyleActivity";
    LinearLayout foodStyleLinearLayout;
    LinearLayout food;
    TextView title;
    TextView authorName;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        foodStyleLinearLayout = (LinearLayout) LayoutInflater.from(getApplication()).inflate(R.layout.food_style, null);

        food = (LinearLayout) foodStyleLinearLayout.findViewById(R.id.foodStyleLinearLayout);
        reLoadByJsonData(food);

        food.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, mScreenHeight));


        scrollView.addView(foodStyleLinearLayout);

        setContentView(maxOutsideLayout);

        addAssistiveTouchButton();

        findViewById(R.id.imageView16).setOnClickListener(mClientListener);
        findViewById(R.id.imageView19).setOnClickListener(mClientListener);

        initEditWordView();

        foodStyleLinearLayout.addView(editWordView);

        title = (TextView)findViewById(R.id.textView17);
        authorName = (TextView)findViewById(R.id.textView18);

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
        ViewGroup sv;
        boolean isChar;

        public CustomWordOnclickListener(EditWordUtil editWordUtil, View editWordView, ViewGroup sv, boolean isChar){
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

                String value = textView.getText().toString();

                et.setText(value);



                int w = mScreenWidth * 10/12 - 50;

                et.measure(0, 0);
                int height = et.getMeasuredHeight();
                int width = et.getMeasuredWidth();
                int h = (int)((float)width/w * et.getLineHeight() );
                if(h>190)
                    h = 190;


                /*sv.post(new Runnable() {
                    public void run() {
                        sv.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });*/
                editWordView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 290 + h));
                ((LinearLayout)sv.getChildAt(0)).setGravity(Gravity.BOTTOM);
                isDisplayContextEditWord = true;
            }else {
                ((LinearLayout)sv.getChildAt(0)).setGravity(Gravity.TOP);
                editWordView.setVisibility(View.GONE);
                isDisplayContextEditWord = false;
            }
        }
    }

    @Override
    public void save() {
        super.save(TAG, title.getText().toString(), food);
    }

    @Override
    public void share() {
        super.share();
    }
}
