package com.wenjian.commonskill;

import android.annotation.SuppressLint;
import android.app.Application;

/**
 * Description: AppConfigure
 * Date: 2018/6/11
 *
 * @author jian.wen@ubtrobot.com
 */

public class AppConfigure {

    @SuppressLint("StaticFieldLeak")
    private static Application sApplication;

    static void init(Application application) {
        sApplication = application;
    }

    public static Application getApp() {
        return sApplication;
    }
}
