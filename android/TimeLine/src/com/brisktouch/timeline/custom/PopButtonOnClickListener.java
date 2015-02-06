package com.brisktouch.timeline.custom;

import android.animation.AnimatorSet;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

/**
 * Created by jim on 2/5/2015.
 */
public class PopButtonOnClickListener implements View.OnClickListener {
    private int[] v1 = {0,-80};
    private int[] v2 = {-50,-80+25};
    private int[] v3 = {-80,-80+70};
    boolean isDisplayButton = false;

    ImageView mImageView1;
    ImageView mImageView2;
    ImageView mImageView3;

    ArcTranslateAnimation animationPop1;
    ArcTranslateAnimation animationPop2;
    ArcTranslateAnimation animationPop3;

    ArcTranslateAnimation animationBack1;
    ArcTranslateAnimation animationBack2;
    ArcTranslateAnimation animationBack3;


    public PopButtonOnClickListener(ImageView image1,ImageView image2, ImageView image3){
        mImageView1 = image1;
        mImageView2 = image2;
        mImageView3 = image3;
        animationPop1 = new ArcTranslateAnimation(0, v1[0], 0, v1[1], mImageView1, true);
        animationPop1.setInterpolator(new LinearInterpolator());
        animationPop1.setDuration(200);
        //animationPop1.setFillAfter(true);

        animationPop2 = new ArcTranslateAnimation(0, v2[0], 0, v2[1], mImageView2, true);
        animationPop2.setInterpolator(new LinearInterpolator());
        animationPop2.setDuration(200);
        //animationPop2.setFillAfter(true);

        animationPop3 = new ArcTranslateAnimation(0, v3[0], 0, v3[1], mImageView3, true);
        animationPop3.setInterpolator(new LinearInterpolator());
        animationPop3.setDuration(100);
        //animationPop3.setFillAfter(true);

        animationBack1 = new ArcTranslateAnimation(0, -v1[0], 0, -v1[1], mImageView1, false);
        animationBack1.setInterpolator(new LinearInterpolator());
        animationBack1.setDuration(500);
        //animationBack1.setFillAfter(true);
        //animationBack1.setAnimationListener(new HideView(mImageView1));

        animationBack2 = new ArcTranslateAnimation(0, -v2[0], 0, -v2[1], mImageView2, false);
        animationBack2.setInterpolator(new LinearInterpolator());
        animationBack2.setDuration(500);
        //animationBack2.setFillAfter(true);
        //animationBack2.setAnimationListener(new HideView(mImageView2));

        animationBack3 = new ArcTranslateAnimation(0, -v3[0], 0, -v3[1], mImageView3, false);
        animationBack3.setInterpolator(new LinearInterpolator());
        animationBack3.setDuration(500);
        //animationBack3.setFillAfter(true);
        //animationBack3.setAnimationListener(new HideView(mImageView3));

    }

    @Override
    public void onClick(View v) {
        if(!isDisplayButton){
            mImageView1.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());

            mImageView2.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());

            mImageView3.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());

            mImageView1.setVisibility(View.VISIBLE);
            mImageView2.setVisibility(View.VISIBLE);
            mImageView3.setVisibility(View.VISIBLE);
            mImageView1.startAnimation(animationPop1);

            animationPop1.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mImageView1.layout(mImageView1.getLeft()+v1[0], mImageView1.getTop()+v1[1], mImageView1.getRight()+v1[0], mImageView1.getBottom()+v1[1]);
                    mImageView2.startAnimation(animationPop2);
                    animationPop2.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            mImageView2.layout(mImageView2.getLeft()+v2[0], mImageView2.getTop()+v2[1], mImageView2.getRight()+v2[0], mImageView2.getBottom()+v2[1]);
                            mImageView3.startAnimation(animationPop3);
                            animationPop3.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    mImageView3.layout(mImageView3.getLeft()+v3[0], mImageView3.getTop()+v3[1], mImageView3.getRight()+v3[0], mImageView3.getBottom()+v3[1]);
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            isDisplayButton = true;
        }else{
            mImageView1.startAnimation(animationBack1);
            animationBack1.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mImageView1.layout(mImageView1.getLeft()-v1[0], mImageView1.getTop()-v1[1], mImageView1.getRight()-v1[0], mImageView1.getBottom()-v1[1]);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    
                }
            });

            mImageView2.startAnimation(animationBack2);
            animationBack2.setAnimationListener(new BackListener(mImageView2, v2));
            mImageView3.startAnimation(animationBack3);
            animationBack3.setAnimationListener(new BackListener(mImageView3, v3));
            isDisplayButton = false;
        }

    }
}

class BackListener implements Animation.AnimationListener {
    ImageView v;
    int[] ints;
    public BackListener(ImageView v, int[] ints){
        this.v = v;
        this.ints = ints;
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        //v.setVisibility(View.INVISIBLE);
        //v.setAlpha(0);
        v.layout(v.getLeft()-ints[0], v.getTop()-ints[1], v.getRight()-ints[0], v.getBottom()-ints[1]);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
