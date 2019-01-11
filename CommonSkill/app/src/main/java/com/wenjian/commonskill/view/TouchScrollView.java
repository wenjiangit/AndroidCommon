package com.wenjian.commonskill.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Description: TouchScrollView
 * Date: 2018/8/20
 *
 * @author jian.wen@ubtrobot.com
 */
public class TouchScrollView extends View{


    private int mLastX;

    private int mLastY;

    public TouchScrollView(Context context) {
        super(context);
    }

    public TouchScrollView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchScrollView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;

                setTranslationX(getTranslationX() + deltaX);
                setTranslationY(getTranslationY() + deltaY);

                break;
                default:
        }
        mLastX = x;
        mLastY = y;
        return true;
    }

}
