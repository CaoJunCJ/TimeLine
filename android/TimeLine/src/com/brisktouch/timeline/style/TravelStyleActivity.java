package com.brisktouch.timeline.style;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.brisktouch.timeline.R;

/**
 * Created by jim on 3/30/2015.
 */
public class TravelStyleActivity extends BaseStyleActivity {
    String TAG = "TravelStyleActivity";
    LinearLayout travelStyleLinearLayout;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        travelStyleLinearLayout = (LinearLayout) LayoutInflater.from(getApplication()).inflate(R.layout.travel_style, null);

        LinearLayout travelLinearLayout = (LinearLayout)travelStyleLinearLayout.findViewById(R.id.travelLinearLayout);

        LinearLayout.LayoutParams travelLayouParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, mScreenHeight-25);

        travelLayouParams.topMargin = 25;

        travelLinearLayout.setLayoutParams(travelLayouParams);

        scrollView.addView(travelStyleLinearLayout);

        setContentView(maxOutsideLayout);

        addAssistiveTouchButton();

        SelectPic mClientListener = new SelectPic();
        findViewById(R.id.imageView17).setOnClickListener(mClientListener);
        findViewById(R.id.imageView18).setOnClickListener(mClientListener);
        findViewById(R.id.imageView20).setOnClickListener(mClientListener);
        findViewById(R.id.imageView21).setOnClickListener(mClientListener);
        findViewById(R.id.imageView22).setOnClickListener(mClientListener);
        findViewById(R.id.imageView23).setOnClickListener(mClientListener);
        findViewById(R.id.imageView24).setOnClickListener(mClientListener);
        findViewById(R.id.imageView25).setOnClickListener(mClientListener);

        initEditWordView();

        travelStyleLinearLayout.addView(editWordView);

        TextView title = (TextView)findViewById(R.id.textView19);

        title.setOnClickListener(wordOnclickListener);

    }

    @Override
    public void save() {

    }

    @Override
    public void share() {

    }
}
