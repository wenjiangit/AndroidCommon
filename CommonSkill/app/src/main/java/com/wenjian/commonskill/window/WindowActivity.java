package com.wenjian.commonskill.window;

import android.annotation.SuppressLint;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.wenjian.commonskill.R;

public class WindowActivity extends AppCompatActivity {


    private WindowManager.LayoutParams mLayoutParams;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_window);

        final Button floatingButton = new Button(this);
        floatingButton.setText("悬浮窗");

        mLayoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                0, 0,
                PixelFormat.TRANSPARENT
        );
        // flag 设置 Window 属性
        mLayoutParams.flags= WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // type 设置 Window 类别（层级）
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        mLayoutParams.gravity = Gravity.CENTER;
        WindowManager windowManager = getWindowManager();
        windowManager.addView(floatingButton, mLayoutParams);
        floatingButton.setOnTouchListener(new View.OnTouchListener() {

            int mLastX;
            int mLastY;

            int mParamX;
            int mParamY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i("wj", "onTouch: ");
                int x = (int) event.getRawX();
                int y = (int) event.getRawY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mLastX = x;
                        mLastY = y;
                        mParamX = mLayoutParams.x;
                        mParamY = mLayoutParams.y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int offsetX = x - mLastX;
                        int offsetY = y - mLastY;
                        mLayoutParams.x = mParamX + offsetX;
                        mLayoutParams.y = mParamY + offsetY;
                        getWindowManager().updateViewLayout(floatingButton, mLayoutParams);
                        break;
                    default:
                }
                return true;
            }
        });
    }
}
