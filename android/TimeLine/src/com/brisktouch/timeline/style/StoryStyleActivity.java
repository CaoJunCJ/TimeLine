package com.brisktouch.timeline.style;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.brisktouch.timeline.R;

/**
 * Created by jim on 3/30/2015.
 */
public class StoryStyleActivity  extends BaseStyleActivity {
    String TAG = "StoryStyleActivity";
    LinearLayout storyStyleLinearLayout;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        storyStyleLinearLayout = (LinearLayout) LayoutInflater.from(getApplication()).inflate(R.layout.story_style, null);

        LinearLayout story = (LinearLayout)storyStyleLinearLayout.findViewById(R.id.storyStyleLinearLayout);

        story.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, mScreenHeight));

        scrollView.addView(storyStyleLinearLayout);

        setContentView(maxOutsideLayout);

        addAssistiveTouchButton();

        SelectPic mClientListener = new SelectPic();
        findViewById(R.id.imageView11).setOnClickListener(mClientListener);

        initEditWordView();

        storyStyleLinearLayout.addView(editWordView);

        TextView title = (TextView)findViewById(R.id.textView12);
        TextView authorName = (TextView)findViewById(R.id.textView13);


        title.setOnClickListener(wordOnclickListener);
        authorName.setOnClickListener(wordOnclickListener);

    }

    @Override
    public void save() {

    }

    @Override
    public void share() {

    }
}
