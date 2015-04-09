package com.brisktouch.timeline.style;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.brisktouch.timeline.R;
import com.brisktouch.timeline.add.EditWordUtil;
import com.brisktouch.timeline.util.FileUtil;
import com.brisktouch.timeline.util.Global;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by jim on 3/30/2015.
 */
public class HumanStyleActivity extends BaseStyleActivity {

    String TAG = "HumanStyleActivity";
    LinearLayout humanStyleLinearLayout;
    LinearLayout human;
    TextView wordTitle;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        humanStyleLinearLayout = (LinearLayout)LayoutInflater.from(getApplication()).inflate(R.layout.human_style, null);

        human = (LinearLayout)humanStyleLinearLayout.findViewById(R.id.hunmanStyleLinearLayout);
        reLoadByJsonData(human);

        human.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, mScreenHeight));

        scrollView.addView(humanStyleLinearLayout);

        setContentView(maxOutsideLayout);

        addAssistiveTouchButton();

        initEditWordView();

        humanStyleLinearLayout.addView(editWordView);

        wordTitle = (TextView)findViewById(R.id.textView2);

        setAllImageViewAndTextViewOnClickListener(human);

    }

    @Override
    public void save() {
        super.save(TAG, wordTitle.getText().toString(), human);
    }

    @Override
    public void share(){

    }
}
