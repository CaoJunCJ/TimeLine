package com.brisktouch.timeline.add;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by caojim on 15/4/4.
 */
public class IterateViewGroup {

    public enum ViewClassType {
        TEXTVIEW,
        IMAGEVIEW,
    }

    public interface IterateViewCallBack {
        public void callBack(View v);
    }

    public static void iterateViewGroup(ViewGroup group, IterateViewCallBack cb, ViewClassType vct){
        for(int i = 0; i < group.getChildCount(); i++){
            View view = group.getChildAt(i);
            if(view instanceof ViewGroup){
                iterateViewGroup((ViewGroup) view, cb, vct);
            }

            switch (vct){
                case TEXTVIEW:
                    if(view instanceof TextView){
                        cb.callBack(view);
                    }
                    break;
                case IMAGEVIEW:
                    if(view instanceof ImageView){
                        cb.callBack(view);
                    }
                    break;
            }


        }
    }
}
