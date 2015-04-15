package com.brisktouch.timeline.style;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.brisktouch.timeline.R;
import com.brisktouch.timeline.wbapi.WBShareMainActivity;
import com.brisktouch.timeline.wxapi.Constants;
import com.brisktouch.timeline.wxapi.WXEntryActivity;
import com.tencent.mm.sdk.openapi.*;

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
        foggyCurrentScreen();
        ImageView weixinImageView = (ImageView)stackBlurImageView.findViewById(R.id.imageViewWeiXin);
        ImageView weiboImageView = (ImageView)stackBlurImageView.findViewById(R.id.imageViewXinLangWeiBo);
        IWXAPI api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, false);
        api.registerApp(Constants.APP_ID);

        weixinImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), WXEntryActivity.class);
                intent.putExtra("TYPE","WEIXIN");
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                //sendMessage();
                //finish();
            }
        });

        weiboImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), WBShareMainActivity.class);
                intent.putExtra("TYPE","WEIBO");
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                //finish();
            }
        });


    }

}
