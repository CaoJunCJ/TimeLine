package com.brisktouch.timeline.add;

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
import com.brisktouch.timeline.style.HumanStyleActivity;

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
        ImageButton image = (ImageButton) findViewById(R.id.imageButton);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(StyleActivity.this, HumanStyleActivity.class);
                startActivity(intent);
                //finish();
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            intent.setClass(StyleActivity.this, MyActivity.class);
            startActivity(intent);
            finish();
        }
        return false;
    }
}
