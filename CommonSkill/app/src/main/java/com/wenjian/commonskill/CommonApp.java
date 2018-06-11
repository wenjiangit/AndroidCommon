package com.wenjian.commonskill;

import android.app.Application;

/**
 * Description: CommonApp
 * Date: 2018/6/11
 *
 * @author jian.wen@ubtrobot.com
 */

public class CommonApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AppConfigure.init(this);

    }
}
