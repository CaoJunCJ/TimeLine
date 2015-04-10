package com.brisktouch.timeline.style;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.*;
import com.brisktouch.timeline.R;
import com.brisktouch.timeline.util.Utils;
import stackblur.StackBlurManager;

/**
 * Created by jim on 3/30/2015.
 */
public class SceneryStyleActivity extends BaseStyleActivity{
    String TAG = "SceneryStyleActivity";
    LinearLayout sceneryStyleLinearLayout;
    LinearLayout scenery;
    TextView title;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sceneryStyleLinearLayout = (LinearLayout)LayoutInflater.from(getApplication()).inflate(R.layout.scenery_style, null);

        scenery = (LinearLayout)sceneryStyleLinearLayout.findViewById(R.id.sceneryStyleLinearLayout);
        reLoadByJsonData(scenery);

        scenery.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, mScreenHeight));

        scrollView.addView(sceneryStyleLinearLayout);

        setContentView(maxOutsideLayout);

        addAssistiveTouchButton();

        initEditWordView();

        sceneryStyleLinearLayout.addView(editWordView);

        title = (TextView)findViewById(R.id.textView8);

        setAllImageViewAndTextViewOnClickListener(scenery);

    }

    @Override
    public void save() {
        super.save(TAG, title.getText().toString(), scenery);
    }

    @Override
    public void share() {
        foggyCurrentScreen();
    }
}
