package com.brisktouch.timeline.style;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.brisktouch.timeline.R;
import com.brisktouch.timeline.add.EditWordUtil;

/**
 * Created by jim on 3/30/2015.
 */
public class SceneryStyleActivity extends BaseStyleActivity{
    String TAG = "SceneryStyleActivity";
    LinearLayout sceneryStyleLinearLayout;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sceneryStyleLinearLayout = (LinearLayout)LayoutInflater.from(getApplication()).inflate(R.layout.scenery_style, null);

        scrollView.addView(sceneryStyleLinearLayout);

        setContentView(maxOutsideLayout);

        addAssistiveTouchButton();

        SelectPic mClientListener = new SelectPic();
        findViewById(R.id.imageView9).setOnClickListener(mClientListener);
        findViewById(R.id.imageView10).setOnClickListener(mClientListener);

        initEditWordView();

        sceneryStyleLinearLayout.addView(editWordView);

        TextView wordContext = (TextView)findViewById(R.id.textView9);
        TextView smallTitle = (TextView)findViewById(R.id.textView6);
        TextView title = (TextView)findViewById(R.id.textView8);
        TextView headImageName = (TextView)findViewById(R.id.textView11);
        TextView authorName = (TextView)findViewById(R.id.textView10);


        wordContext.setOnClickListener(wordOnclickListener);
        smallTitle.setOnClickListener(wordOnclickListener);
        title.setOnClickListener(wordOnclickListener);
        headImageName.setOnClickListener(wordOnclickListener);
        authorName.setOnClickListener(wordOnclickListener);

    }

    @Override
    public void save() {

    }

    @Override
    public void share() {

    }
}
