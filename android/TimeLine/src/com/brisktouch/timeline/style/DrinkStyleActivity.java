package com.brisktouch.timeline.style;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.brisktouch.timeline.R;

/**
 * Created by jim on 3/30/2015.
 */
public class DrinkStyleActivity extends BaseStyleActivity{

    String TAG = "DrinkStyleActivity";
    LinearLayout drinkStyleLinearLayout;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        drinkStyleLinearLayout = (LinearLayout) LayoutInflater.from(getApplication()).inflate(R.layout.drink_style, null);

        scrollView.addView(drinkStyleLinearLayout);

        setContentView(maxOutsideLayout);

        addAssistiveTouchButton();

        BaseStyleActivity.SelectPic mClientListener = new BaseStyleActivity.SelectPic();
        findViewById(R.id.imageView12).setOnClickListener(mClientListener);
        findViewById(R.id.imageView13).setOnClickListener(mClientListener);
        findViewById(R.id.imageView14).setOnClickListener(mClientListener);
        findViewById(R.id.imageView15).setOnClickListener(mClientListener);

        initEditWordView();

        drinkStyleLinearLayout.addView(editWordView);

        TextView title = (TextView)findViewById(R.id.textView14);
        TextView desc = (TextView)findViewById(R.id.textView15);
        TextView authorName = (TextView)findViewById(R.id.textView16);


        title.setOnClickListener(wordOnclickListener);
        desc.setOnClickListener(wordOnclickListener);
        authorName.setOnClickListener(wordOnclickListener);

    }

    @Override
    public void save() {

    }

    @Override
    public void share() {

    }
}
