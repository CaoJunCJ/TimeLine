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
    LinearLayout story;
    TextView title;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        storyStyleLinearLayout = (LinearLayout) LayoutInflater.from(getApplication()).inflate(R.layout.story_style, null);

        story = (LinearLayout)storyStyleLinearLayout.findViewById(R.id.storyStyleLinearLayout);
        reLoadByJsonData(story);

        story.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, mScreenHeight));

        scrollView.addView(storyStyleLinearLayout);

        setContentView(maxOutsideLayout);

        addAssistiveTouchButton();

        initEditWordView();

        storyStyleLinearLayout.addView(editWordView);

        title = (TextView)findViewById(R.id.textView12);

        setAllImageViewAndTextViewOnClickListener(story);
    }

    @Override
    public void save() {
        super.save(TAG, title.getText().toString(), story);
    }

    @Override
    public void share() {

    }
}
