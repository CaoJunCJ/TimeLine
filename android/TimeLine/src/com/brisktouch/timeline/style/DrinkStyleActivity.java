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
    TextView title;
    LinearLayout drink;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        drinkStyleLinearLayout = (LinearLayout) LayoutInflater.from(getApplication()).inflate(R.layout.drink_style, null);

        drink = (LinearLayout) drinkStyleLinearLayout.findViewById(R.id.drinkStyleLinearLayout);
        reLoadByJsonData(drink);

        drink.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, mScreenHeight));

        scrollView.addView(drinkStyleLinearLayout);

        setContentView(maxOutsideLayout);

        addAssistiveTouchButton();



        initEditWordView();

        drinkStyleLinearLayout.addView(editWordView);

        title = (TextView)findViewById(R.id.textView14);

        setAllImageViewAndTextViewOnClickListener(drink);

    }

    @Override
    public void save() {
        super.save(TAG, title.getText().toString(), drink);
    }

    @Override
    public void share() {

    }
}
