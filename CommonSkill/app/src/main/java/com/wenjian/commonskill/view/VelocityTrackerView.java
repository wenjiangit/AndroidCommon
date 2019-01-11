package com.wenjian.commonskill.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

import timber.log.Timber;

/**
 * Description: VelocityTrackerView
 * Date: 2018/8/20
 *
 * @author jian.wen@ubtrobot.com
 */
public class VelocityTrackerView extends View {

    private static final String TAG = "VelocityTrackerView";
    private VelocityTracker mTracker;

    public VelocityTrackerView(Context context) {
        super(context);
    }

    public VelocityTrackerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public VelocityTrackerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mTracker = VelocityTracker.obtain();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            mTracker.addMovement(event);
            mTracker.computeCurrentVelocity(1000);
            int xVelocity = (int) mTracker.getXVelocity();
            int yVelocity = (int) mTracker.getYVelocity();
            Timber.i("x=%d,y=%d", xVelocity, yVelocity);
            return true;
        }


        return super.onTouchEvent(event);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mTracker.clear();
        mTracker.recycle();
    }
}
