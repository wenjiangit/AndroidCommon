package com.wenjian.commonskill.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;

import java.util.Random;

/**
 * Description: ColorView
 * Date: 2018/8/14
 *
 * @author jian.wen@ubtrobot.com
 */
public class ColorView extends View {

    private boolean isRunning;

    private Random random = new Random();

    public ColorView(Context context) {
        super(context);
    }

    public ColorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isRunning) {
                    return false;
                }
                float x = event.getX();
                float y = event.getY();
                Log.i("wj", "onTouchEvent: ");
                Animator animator = ViewAnimationUtils.createCircularReveal(this, (int) x, (int) y, 0, 4000);
                setRandomColor();
                animator.setDuration(500);
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isRunning = false;
                    }
                });
                this.setVisibility(VISIBLE);
                animator.start();
                isRunning = true;
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }

    private void setRandomColor() {
        setBackgroundColor(Color.argb(255, random.nextInt(255), random.nextInt(255), random.nextInt(255)));
    }

}
