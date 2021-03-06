package com.brisktouch.timeline.style;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import android.widget.ImageButton;
import com.brisktouch.timeline.MyActivity;
import com.brisktouch.timeline.R;
import com.brisktouch.timeline.util.Utils;

/**
 * Created by cerosoft on 1/9/2015.
 */
public class StyleActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //hide status bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);



        setContentView(R.layout.select_style);
        ImageButton humanStyleButton = (ImageButton) findViewById(R.id.imageButton);
        humanStyleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(StyleActivity.this, HumanStyleActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                //finish();
            }
        });

        ImageButton storyStyleButton = (ImageButton) findViewById(R.id.imageButton2);
        storyStyleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(StyleActivity.this, StoryStyleActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                //finish();
            }
        });

        ImageButton sceneryStyleButton = (ImageButton) findViewById(R.id.imageButton3);
        sceneryStyleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(StyleActivity.this, SceneryStyleActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                //finish();
            }
        });

        ImageButton drinkStyleButton = (ImageButton) findViewById(R.id.imageButton4);
        drinkStyleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(StyleActivity.this, DrinkStyleActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                //finish();
            }
        });


        ImageButton travelStyleButton = (ImageButton) findViewById(R.id.imageButton5);
        travelStyleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(StyleActivity.this, TravelStyleActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                //finish();
            }
        });

        ImageButton foodStyleButton = (ImageButton) findViewById(R.id.imageButton6);
        foodStyleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(StyleActivity.this, FoodStyleActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                //finish();
            }
        });



    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            if(Utils.hasECLAIR())
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
