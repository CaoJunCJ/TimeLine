package com.brisktouch.timeline;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.brisktouch.timeline.add.StyleActivity;
import com.brisktouch.timeline.custom.CircleButton;

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

        //maybe write a clone function .

        RelativeLayout mainView = new RelativeLayout(this);
        mainView.setGravity(Gravity.BOTTOM);

        LinearLayout mSkipLinearLayout = new LinearLayout(this);
        mSkipLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,GridView.LayoutParams.MATCH_PARENT));
        mSkipLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        mSkipLinearLayout.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
        TextView jump = new TextView(this);
        jump.setOnClickListener(new JumpToMainView());
        jump.setPadding(0, 0, 20, 30);
        jump.setTextColor(Color.WHITE);
        jump.setTextSize(26);
        jump.setText(R.string.skip);
        mSkipLinearLayout.addView(jump);

        LinearLayout mLinearLayout = new LinearLayout(this);
        mLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,GridView.LayoutParams.MATCH_PARENT));
        mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        mLinearLayout.setGravity(Gravity.CENTER | Gravity.BOTTOM);

        LinearLayout.LayoutParams pointLayout = new LinearLayout.LayoutParams(15,15);
        pointLayout.setMargins(0,0,0,20);

        final CircleButton mCircleButton1 = new CircleButton(this);
        mCircleButton1.setColor(Color.WHITE);
        mCircleButton1.setIsClickAnimator(false);
        mCircleButton1.setLayoutParams(pointLayout);

        final CircleButton mCircleButton2 = new CircleButton(this);
        mCircleButton2.setColor(Color.GRAY);
        mCircleButton2.setIsClickAnimator(false);
        mCircleButton2.setLayoutParams(pointLayout);

        final CircleButton mCircleButton3 = new CircleButton(this);
        mCircleButton3.setColor(Color.GRAY);
        mCircleButton3.setIsClickAnimator(false);
        mCircleButton3.setLayoutParams(pointLayout);

        mLinearLayout.addView(mCircleButton1);
        mLinearLayout.addView(mCircleButton2);
        mLinearLayout.addView(mCircleButton3);

        mViewPager = new android.support.v4.view.ViewPager(this);




        GridView.LayoutParams mLayoutParams = new GridView.LayoutParams(GridView.LayoutParams.WRAP_CONTENT,GridView.LayoutParams.WRAP_CONTENT);
        ImageView mImageView1 = new ImageView(this);
        mImageView1.setLayoutParams(mLayoutParams);
        mImageView1.setPadding(0,10,0,0);
        mImageView1.setImageResource(R.drawable.wel1);
        LinearLayout welcomeView1 = new LinearLayout(this);
        welcomeView1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
        welcomeView1.setOrientation(LinearLayout.VERTICAL);
        welcomeView1.setGravity(Gravity.CENTER_HORIZONTAL);
        welcomeView1.addView(mImageView1);
        TextView text1 = new TextView(this);
        text1.setTextColor(Color.WHITE);
        text1.setGravity(Gravity.CENTER);
        text1.setText("Hello World");
        text1.setTextSize(22);
        TextView desc1 = new TextView(this);
        desc1.setPadding(15,10,15,0);
        desc1.setTextColor(Color.WHITE);
        desc1.setText(R.string.Lolita);
        desc1.setGravity(Gravity.CENTER);
        desc1.setTextSize(12);
        welcomeView1.addView(text1);
        welcomeView1.addView(desc1);
        //mImageView1.setBackgroundResource(R.drawable.welcome_backgroud_1);

        ImageView mImageView2 = new ImageView(this);
        mImageView2.setLayoutParams(mLayoutParams);
        mImageView2.setPadding(0,10,0,0);
        mImageView2.setImageResource(R.drawable.wel2);
        LinearLayout welcomeView2 = new LinearLayout(this);
        welcomeView2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
        welcomeView2.setOrientation(LinearLayout.VERTICAL);
        welcomeView2.setGravity(Gravity.CENTER_HORIZONTAL);
        welcomeView2.addView(mImageView2);
        TextView text2 = new TextView(this);
        text2.setTextColor(Color.WHITE);
        text2.setGravity(Gravity.CENTER);
        text2.setText("Hello World");
        text2.setTextSize(22);
        TextView desc2 = new TextView(this);
        desc2.setPadding(15,10,15,0);
        desc2.setTextColor(Color.WHITE);
        desc2.setText(R.string.Lolita);
        desc2.setGravity(Gravity.CENTER);
        desc2.setTextSize(12);
        welcomeView2.addView(text2);
        welcomeView2.addView(desc2);
        //mImageView2.setBackgroundResource(R.drawable.welcome_background_2);

        ImageView mImageView3 = new ImageView(this);
        mImageView3.setLayoutParams(mLayoutParams);
        mImageView3.setPadding(0,10,0,0);
        mImageView3.setImageResource(R.drawable.wel3);
        LinearLayout welcomeView3 = new LinearLayout(this);
        welcomeView3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
        welcomeView3.setOrientation(LinearLayout.VERTICAL);
        welcomeView3.setGravity(Gravity.CENTER_HORIZONTAL);
        welcomeView3.addView(mImageView3);
        TextView text3 = new TextView(this);
        text3.setTextColor(Color.WHITE);
        text3.setGravity(Gravity.CENTER);
        text3.setText("Hello World");
        text3.setTextSize(22);
        TextView desc3 = new TextView(this);
        desc3.setPadding(15,10,15,0);
        desc3.setTextColor(Color.WHITE);
        desc3.setText(R.string.Lolita);
        desc3.setGravity(Gravity.CENTER);
        desc3.setTextSize(12);
        welcomeView3.addView(text3);
        welcomeView3.addView(desc3);
        //mImageView3.setBackgroundResource(R.drawable.welcome_background_3);
        views[0] = welcomeView1;
        views[1] = welcomeView2;
        views[2] = welcomeView3;

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
        //mViewPager.setBackgroundResource(R.drawable.welcome_back);
        mViewPager.setBackgroundColor(Color.argb(255, 140, 41, 78));
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float v, int i1) {
                //Log.d("onPageScrolled", "i:" + position + ", v:" + v + ", i1:" + i1);
                //why?????
                if (v > 0f) {
                    if (position == 0) {
                        mViewPager.setBackgroundColor(Color.argb(255, (int) (140 - (30 * v)), (int) (41 + (161 * v)), (int) (78 + 158 * v)));
                    }

                    if (position == 1) {
                        mViewPager.setBackgroundColor(Color.argb(255, (int) (110 + 24 * v), (int) (202 - v * 7), (int) (236 - 155 * v)));
                    }

                    if (position == 2) {
                        //because max is 3 , so never call this.
                        mViewPager.setBackgroundColor(Color.argb(255, (int) (125 * v), 100, 100));
                    }

                }


            }

            @Override
            public void onPageSelected(int i) {
                switch (i) {
                    case 0:
                        mCircleButton1.setColor(Color.WHITE);
                        mCircleButton2.setColor(Color.GRAY);
                        mCircleButton3.setColor(Color.GRAY);
                        break;
                    case 1:
                        mCircleButton1.setColor(Color.GRAY);
                        mCircleButton2.setColor(Color.WHITE);
                        mCircleButton3.setColor(Color.GRAY);
                        break;
                    case 2:
                        mCircleButton1.setColor(Color.GRAY);
                        mCircleButton2.setColor(Color.GRAY);
                        mCircleButton3.setColor(Color.WHITE);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
        mainView.addView(mViewPager);
        mainView.addView(mLinearLayout);
        mainView.addView(mSkipLinearLayout);
        setContentView(mainView);
    }

    public class JumpToMainView implements View.OnClickListener {
        public void onClick(View v){
            Intent intent = new Intent();
            intent.setClass(WelcomeActivity.this, MyActivity.class);
            startActivity(intent);
            finish();
        }
    }
}



