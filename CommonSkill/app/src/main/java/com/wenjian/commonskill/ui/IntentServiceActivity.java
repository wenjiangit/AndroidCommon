package com.wenjian.commonskill.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

import com.wenjian.commonskill.BaseActivity;
import com.wenjian.commonskill.R;
import com.wenjian.commonskill.thread.LocalIntentService;

import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class IntentServiceActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent_service);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.bt_start)
    void onServiceStart() {
        Timber.i("onServiceStart");
        Intent intent = new Intent(this, LocalIntentService.class);
        intent.putExtra("aa", "sadada");
        startService(intent);
    }


    @OnClick(R.id.button6)
    void onTestANR() {
       new Thread(){
           @Override
           public void run() {
               super.run();
               doSomething();
           }
       }.start();
        SystemClock.sleep(10);
        doSomethingInUi();
    }

    private synchronized void doSomething() {
        SystemClock.sleep(30000);
    }

    private synchronized void doSomethingInUi() {
        findViewById(R.id.button6);
    }


}
