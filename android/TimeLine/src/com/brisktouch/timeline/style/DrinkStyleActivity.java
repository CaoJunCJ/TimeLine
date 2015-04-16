package com.brisktouch.timeline.style;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
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
        long startTime = System.currentTimeMillis();
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
        long endTime = System.currentTimeMillis();
        Log.d(TAG, "use time : " + (endTime - startTime));
    }

    @Override
    public void save() {
        super.save(TAG, title.getText().toString(), drink);
    }

    @Override
    public void share() {
        super.share();
        /*
        foggyCurrentScreen();
        ImageView weixinImageView = (ImageView)stackBlurImageView.findViewById(R.id.imageViewWeiXin);
        ImageView weiboImageView = (ImageView)stackBlurImageView.findViewById(R.id.imageViewXinLangWeiBo);
        ImageView frendsImageView = (ImageView) stackBlurImageView.findViewById(R.id.imageViewPengYouQuan);


        weixinImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), WXEntryActivity.class);
                intent.putExtra("TYPE","WEIXIN");
                intent.putExtra("IMAGE_PATH", currentScreenFilePath);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        });

        frendsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), WXEntryActivity.class);
                intent.putExtra("TYPE","WEIXIN");
                intent.putExtra("FRIENDS","YES");
                intent.putExtra("IMAGE_PATH", currentScreenFilePath);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        });

        weiboImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), WBShareMainActivity.class);
                intent.putExtra("TYPE","WEIBO");
                intent.putExtra("IMAGE_PATH", currentScreenFilePath);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        });
        */

    }

}
