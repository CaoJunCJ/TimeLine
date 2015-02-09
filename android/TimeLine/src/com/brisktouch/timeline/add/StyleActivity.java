package com.brisktouch.timeline.add;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import android.widget.ImageButton;
import com.brisktouch.timeline.R;

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
        ImageButton image = (ImageButton)findViewById(R.id.imageButton);
        image.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(StyleActivity.this, Style1.class);
                startActivity(intent);
                //finish();
            }
        });
    }
}
