package com.wenjian.commonskill;

import android.app.Application;
import android.util.DisplayMetrics;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.wenjian.commonskill.screencompat.ScreenAdapt;
import com.wenjian.commonskill.screencompat.ScreenConfig;

import tech.linjiang.pandora.Pandora;
import timber.log.Timber;

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

        Pandora.init(this).enableShakeOpen();

        Logger.addLogAdapter(new AndroidLogAdapter());

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
            Timber.tag("wj");
        }

        ScreenAdapt.init(new ScreenConfig.Builder(this)
                .designWidth(412)
                .build());

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        Timber.i("density=%f", displayMetrics.density);
        Timber.i("dpi=%d", displayMetrics.densityDpi);
        Timber.i("scaledDensity=%f", displayMetrics.scaledDensity);
        Timber.i("widthPixels=%d", displayMetrics.widthPixels);
        Timber.i("heightPixels=%d", displayMetrics.heightPixels);


    }
}
