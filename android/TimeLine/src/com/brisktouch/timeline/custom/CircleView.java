package com.brisktouch.timeline.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import com.brisktouch.timeline.R;

/**
 * Created by jim on 3/11/2015.
 */
public class CircleView extends View {

    int color = Color.BLACK;;

    public CircleView(Context context) {
        super(context);
        init(context, null);
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs){
        if(attrs != null){
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleView);
            color = a.getColor(R.styleable.CircleView_cv_color, color);

        }
    }

    public int getColor(){
        return color;
    }

    public void setColor(int color){
        this.color = color;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint p = new Paint();
        //p.setStyle(Paint.Style.STROKE);
        p.setAntiAlias(true);
        p.setColor(color);
        p.setStrokeWidth(3);
        canvas.drawCircle(15, 15, 12, p);
    }

}
