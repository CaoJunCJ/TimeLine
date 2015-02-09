package com.brisktouch.timeline;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by jim on welcome_background_2/9/2015.
 */
public class WelcomeActivity extends Activity {
    android.support.v4.view.ViewPager mViewPager;
    View[] views = new View[3];
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mViewPager = new android.support.v4.view.ViewPager(this);
        GridView.LayoutParams mLayoutParams = new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT,GridView.LayoutParams.MATCH_PARENT);
        ImageView mImageView1 = new ImageView(this);
        mImageView1.setLayoutParams(mLayoutParams);
        mImageView1.setImageResource(R.drawable.wel1);
        //mImageView1.setBackgroundResource(R.drawable.welcome_backgroud_1);

        ImageView mImageView2 = new ImageView(this);
        mImageView2.setLayoutParams(mLayoutParams);
        mImageView2.setImageResource(R.drawable.wel2);
        //mImageView2.setBackgroundResource(R.drawable.welcome_background_2);

        ImageView mImageView3 = new ImageView(this);
        mImageView3.setLayoutParams(mLayoutParams);
        mImageView3.setImageResource(R.drawable.wel3);
        //mImageView3.setBackgroundResource(R.drawable.welcome_background_3);
        views[0] = mImageView1;
        views[1] = mImageView2;
        views[2] = mImageView3;

        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return views.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object o) {
                return view == o;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                // TODO Auto-generated method stub
                container.addView(views[position]);
                return views[position];
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(views[position]);
            }
        });
        mViewPager.setBackgroundResource(R.drawable.welcome_back);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float v, int i1) {
                //Log.d("onPageScrolled", "i:" + position + ", v:" + v + ", i1:" + i1);
                //why?????
                if (v > 0f) {
                    if (position == 0) {
                        mViewPager.setBackgroundColor(Color.argb(255, (int) (125 * v), 100, 100));
                    }

                    if (position == 1) {
                        mViewPager.setBackgroundColor(Color.argb(255, (int) (125 + 125 * v), 100, 100));

                    }

                    if (position == 2) {
                        mViewPager.setBackgroundColor(Color.argb(255, (int) (125 * v), 100, 100));
                    }

                }


            }

            @Override
            public void onPageSelected(int i) {
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
        setContentView(mViewPager);
    }
}

class DepthPageTransformer implements ViewPager.PageTransformer{
    View _viewPager;
    public DepthPageTransformer(View viewPager){
        _viewPager = viewPager;
    }
    public void transformPage(View view, float position){

        //float interpolatedTime = (position==0?0:(position+1)/2) ;
        Log.d("DepthPageTransformer", "position:"+position);
        //_viewPager.setBackgroundColor(Color.argb(255,(int)(Math.abs(interpolatedTime)*255),100,0));


    }
}

