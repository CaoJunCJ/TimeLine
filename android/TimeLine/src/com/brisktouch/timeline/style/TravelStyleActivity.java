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
    LinearLayout travel;
    TextView title;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        travelStyleLinearLayout = (LinearLayout) LayoutInflater.from(getApplication()).inflate(R.layout.travel_style, null);

        travel = (LinearLayout)travelStyleLinearLayout.findViewById(R.id.travelLinearLayout);

        LinearLayout.LayoutParams travelLayouParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, mScreenHeight-25);

        travelLayouParams.topMargin = 25;

        travel.setLayoutParams(travelLayouParams);

        scrollView.addView(travelStyleLinearLayout);

        setContentView(maxOutsideLayout);

        addAssistiveTouchButton();

        initEditWordView();

        travelStyleLinearLayout.addView(editWordView);

        title = (TextView)findViewById(R.id.textView19);

        setAllImageViewAndTextViewOnClickListener(travel);

    }

    @Override
    public void save() {
        super.save(TAG, title.getText().toString(), travel);
    }

    @Override
    public void share() {

    }
}
