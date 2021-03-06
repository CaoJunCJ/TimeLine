package com.brisktouch.timeline.custom;

import android.graphics.PointF;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;

/**
 * Created by jim on 2/4/2015.
 */
public class ArcTranslateAnimation extends Animation {
    private int mFromXType = ABSOLUTE;
    private int mToXType = ABSOLUTE;

    private int mFromYType = ABSOLUTE;
    private int mToYType = ABSOLUTE;

    private float mFromXValue = 0.0f;
    private float mToXValue = 0.0f;

    private float mFromYValue = 0.0f;
    private float mToYValue = 0.0f;

    private float mFromXDelta;
    private float mToXDelta;
    private float mFromYDelta;
    private float mToYDelta;

    private PointF mStart;
    private PointF mControl;
    private PointF mEnd;
    private PointF mControl2;


    private ImageView mImageView;
    private boolean alphaIsZero;
    /**
     * Constructor to use when building a ArcTranslateAnimation from code
     *
     * @param fromXDelta
     *            Change in X coordinate to apply at the start of the animation
     * @param toXDelta
     *            Change in X coordinate to apply at the end of the animation
     * @param fromYDelta
     *            Change in Y coordinate to apply at the start of the animation
     * @param toYDelta
     *            Change in Y coordinate to apply at the end of the animation
     */
    public ArcTranslateAnimation(float fromXDelta, float toXDelta,
                                 float fromYDelta, float toYDelta) {
        mFromXValue = fromXDelta;
        mToXValue = toXDelta;
        mFromYValue = fromYDelta;
        mToYValue = toYDelta;

        mFromXType = ABSOLUTE;
        mToXType = ABSOLUTE;
        mFromYType = ABSOLUTE;
        mToYType = ABSOLUTE;
    }

    public ArcTranslateAnimation(float fromXDelta, float toXDelta,
                                 float fromYDelta, float toYDelta, ImageView imageView, boolean alphaIsZero) {
        mFromXValue = fromXDelta;
        mToXValue = toXDelta;
        mFromYValue = fromYDelta;
        mToYValue = toYDelta;

        mFromXType = ABSOLUTE;
        mToXType = ABSOLUTE;
        mFromYType = ABSOLUTE;
        mToYType = ABSOLUTE;
        mImageView = imageView;
        this.alphaIsZero = alphaIsZero;
    }

    /**
     * Constructor to use when building a ArcTranslateAnimation from code
     *
     * @param fromXType
     *            Specifies how fromXValue should be interpreted. One of
     *            Animation.ABSOLUTE, Animation.RELATIVE_TO_SELF, or
     *            Animation.RELATIVE_TO_PARENT.
     * @param fromXValue
     *            Change in X coordinate to apply at the start of the animation.
     *            This value can either be an absolute number if fromXType is
     *            ABSOLUTE, or a percentage (where 1.0 is 100%) otherwise.
     * @param toXType
     *            Specifies how toXValue should be interpreted. One of
     *            Animation.ABSOLUTE, Animation.RELATIVE_TO_SELF, or
     *            Animation.RELATIVE_TO_PARENT.
     * @param toXValue
     *            Change in X coordinate to apply at the end of the animation.
     *            This value can either be an absolute number if toXType is
     *            ABSOLUTE, or a percentage (where 1.0 is 100%) otherwise.
     * @param fromYType
     *            Specifies how fromYValue should be interpreted. One of
     *            Animation.ABSOLUTE, Animation.RELATIVE_TO_SELF, or
     *            Animation.RELATIVE_TO_PARENT.
     * @param fromYValue
     *            Change in Y coordinate to apply at the start of the animation.
     *            This value can either be an absolute number if fromYType is
     *            ABSOLUTE, or a percentage (where 1.0 is 100%) otherwise.
     * @param toYType
     *            Specifies how toYValue should be interpreted. One of
     *            Animation.ABSOLUTE, Animation.RELATIVE_TO_SELF, or
     *            Animation.RELATIVE_TO_PARENT.
     * @param toYValue
     *            Change in Y coordinate to apply at the end of the animation.
     *            This value can either be an absolute number if toYType is
     *            ABSOLUTE, or a percentage (where 1.0 is 100%) otherwise.
     */
    public ArcTranslateAnimation(int fromXType, float fromXValue, int toXType,
                                 float toXValue, int fromYType, float fromYValue, int toYType,
                                 float toYValue) {

        mFromXValue = fromXValue;
        mToXValue = toXValue;
        mFromYValue = fromYValue;
        mToYValue = toYValue;

        mFromXType = fromXType;
        mToXType = toXType;
        mFromYType = fromYType;
        mToYType = toYType;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        //float dx = calcBezier(interpolatedTime, mStart.x, mControl.x, mEnd.x);
        //float dy = calcBezier(interpolatedTime, mStart.y, mControl.y, mEnd.y);

        float dx = calcBezier(interpolatedTime, mStart.x, mControl.x, mControl2.x, mEnd.x);
        float dy = calcBezier(interpolatedTime, mStart.y, mControl.y, mControl2.y, mEnd.y);

        t.getMatrix().setTranslate(dx, dy);

        if(mImageView!=null){
            if(alphaIsZero){
                mImageView.setAlpha((int)(interpolatedTime*255));
                ((CircleButton)mImageView).setBackgroundAlpha(interpolatedTime);
            }else{
                mImageView.setAlpha((int)((1-interpolatedTime)*255));
                ((CircleButton)mImageView).setBackgroundAlpha(1-interpolatedTime);
            }
        }
    }

    @Override
    public void initialize(int width, int height, int parentWidth,
                           int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        mFromXDelta = resolveSize(mFromXType, mFromXValue, width, parentWidth);
        mToXDelta = resolveSize(mToXType, mToXValue, width, parentWidth);
        mFromYDelta = resolveSize(mFromYType, mFromYValue, height, parentHeight);
        mToYDelta = resolveSize(mToYType, mToYValue, height, parentHeight);

        mStart = new PointF(mFromXDelta, mFromYDelta);
        mEnd = new PointF(mToXDelta, mToYDelta);
        mControl = new PointF(mFromXDelta, mToYDelta); // How to choose the

        mControl = new PointF(mFromXDelta-100, mFromYDelta+100);
        mControl2 = new PointF(mFromXDelta-100, mToYDelta+50);
        //mControl is control curves
        /*
            description:
            http://en.wikipedia.org/wiki/B%C3%A9zier_curve
         */
        // Control point(we can
        // use the cross of the
        // two tangents from p0,
        // p1)
    }

    /**
     * Calculate the position on a quadratic bezier curve by given three points
     * and the percentage of time passed.
     *
     * from http://en.wikipedia.org/wiki/B%C3%A9zier_curve
     *
     * @param interpolatedTime
     *            the fraction of the duration that has passed where 0 <= time
     *            <= 1
     * @param p0
     *            a single dimension of the starting point
     * @param p1
     *            a single dimension of the control point
     * @param p2
     *            a single dimension of the ending point
     */
    private long calcBezier(float interpolatedTime, float p0, float p1, float p2) {
        return Math.round((Math.pow((1 - interpolatedTime), 2) * p0)
                + (2 * (1 - interpolatedTime) * interpolatedTime * p1)
                + (Math.pow(interpolatedTime, 2) * p2));
    }


    //B(t)=p0*(1-t)^3+3*p1*t(1-t)^2+3*p2*t^2*(1-t)+p3*t^3
    private float calcBezier(float t, float p0, float p1, float p2, float p3) {
        return Math.round(p0*Math.pow((1-t),3)+3*p1*t*Math.pow((1-t),2)+3*p2*Math.pow(t,2)*(1-t)+p3*Math.pow(t,3));
    }

}
