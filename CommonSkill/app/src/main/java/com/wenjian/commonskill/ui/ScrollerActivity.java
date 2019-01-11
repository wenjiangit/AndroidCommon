package com.wenjian.commonskill.ui;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.wenjian.commonskill.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScrollerActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroller);
        ButterKnife.bind(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }


    /*
    @OnClick(R.id.scroll_to)
    void scrollTo() {
        mLayout.scrollTo(-200, -200);
    }
     @OnClick(R.id.scroll_by)
    void scrollBy() {
         mLayout.scrollBy(-100, -100);
    }
*/

}
