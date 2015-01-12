package com.brisktouch.timeline.add;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import com.brisktouch.timeline.R;

/**
 * Created by cerosoft on 1/12/2015.
 */
public class Style1 extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //hide status bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.style1);
    }
}
