package com.brisktouch.timeline.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by jim on 2/2/2015.
 */
public class DragImageView extends ImageView {
    String TAG = "DragImageView";
    int lastX;
    int lastY;
    int screenWidth;
    int screenHeight;
    boolean isMove;
    OnClickListener listener;
    long downTimeMillis;
    public DragImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        screenHeight = context.getResources().getDisplayMetrics().heightPixels;
    }

    public DragImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        screenHeight = context.getResources().getDisplayMetrics().heightPixels;
    }

    public void setOnClickListener(OnClickListener l) {
        listener = l;
    }


    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch(ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //Log.d(TAG, "ACTION_DOWN");
                isMove = false;
                lastX = (int) ev.getRawX();
                lastY = (int) ev.getRawY();
                //Log.d(TAG, "x:" + lastX);
                //Log.d(TAG, "y:" + lastY);
                downTimeMillis = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_MOVE:
                isMove = true;
                //Log.d(TAG, "ACTION_MOVE");
                int dx = (int) ev.getRawX() - lastX;
                int dy = (int) ev.getRawY() - lastY;

                int left = getLeft() + dx;
                int top = getTop() + dy;
                int right = getRight() + dx;
                int bottom = getBottom() + dy;

                //Log.d(TAG, "left:" + left);
                //Log.d(TAG, "top:" + top);
                //Log.d(TAG, "right:" + right);
                //Log.d(TAG, "bottom:" + bottom);

                // set out of max
                if (left < 0) {
                    left = 0;
                    right = left + getWidth();
                }

                if (right > screenWidth) {
                    right = screenWidth;
                    left = right - getWidth();
                }

                if (top < 0) {
                    top = 0;
                    bottom = top + getHeight();
                }

                if (bottom > screenHeight) {
                    bottom = screenHeight;
                    top = bottom - getHeight();
                }
                layout(left, top, right, bottom);

                lastX = (int) ev.getRawX();
                lastY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                //Log.d(TAG, "ACTION_UP");
                if((!isMove||(System.currentTimeMillis()-downTimeMillis)<300) && listener != null){
                    listener.onClick(this);
                }
                break;
        }
        return true;
        //return super.dispatchTouchEvent(ev);
    }
}
