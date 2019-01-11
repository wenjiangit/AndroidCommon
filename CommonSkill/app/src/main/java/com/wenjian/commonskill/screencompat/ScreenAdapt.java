package com.wenjian.commonskill.screencompat;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

import tech.linjiang.pandora.ui.connector.SimpleActivityLifecycleCallbacks;
import timber.log.Timber;

/**
 * Description: ScreenAdapt
 * Date: 2018/8/22
 *
 * @author jian.wen@ubtrobot.com
 */
public class ScreenAdapt {

    private static float sNonCompatDensity;
    private static float sNonCompatScaleDensity;
    private static int sNonCompatDensityDpi;

    private static ScreenConfig sConfig;

    private static boolean open = true;

    public static void stop() {
        open = false;
    }

    public static void init(@NonNull ScreenConfig config) {
        sConfig = config;
        sConfig.getApplication().registerActivityLifecycleCallbacks(new SimpleActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                super.onActivityCreated(activity, savedInstanceState);
                if (!open) {
                    return;
                }
                if (activity instanceof ScreenAdapter) {
                    boolean compat = ((ScreenAdapter) activity).needCompat();
                    if (compat) {
                        adapt(activity, sConfig.getApplication());
                    }
                }
            }


            @Override
            public void onActivityStopped(Activity activity) {
                super.onActivityStopped(activity);
                cancelAdapt(activity);
            }
        });
    }

    private static void adapt(Activity activity, final Application application) {
        final DisplayMetrics appDisplayMetrics = application.getResources().getDisplayMetrics();
        if (sNonCompatDensity == 0) {
            sNonCompatDensity = appDisplayMetrics.density;
            sNonCompatScaleDensity = appDisplayMetrics.scaledDensity;
            sNonCompatDensityDpi = appDisplayMetrics.densityDpi;
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration newConfig) {
                    if (newConfig != null && newConfig.fontScale > 0) {
                        sNonCompatScaleDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {

                }
            });
        }
        float targetDensity;
        if (sConfig.isBaseOnWidth()) {
            targetDensity = appDisplayMetrics.widthPixels * 1.0f / sConfig.getDesignWidth();
        } else {
            targetDensity = appDisplayMetrics.heightPixels * 1.0f / sConfig.getDesignHeight();
        }
        Timber.i("targetDensity = %f", targetDensity);

        final float targetScaleDensity = targetDensity * (sNonCompatScaleDensity / sNonCompatDensity);
        final int targetDensityDpi = (int) (160 * targetDensity);

        appDisplayMetrics.density = targetDensity;
        appDisplayMetrics.densityDpi = targetDensityDpi;
        appDisplayMetrics.scaledDensity = targetScaleDensity;

        DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
        activityDisplayMetrics.density = targetDensity;
        activityDisplayMetrics.densityDpi = targetDensityDpi;
        activityDisplayMetrics.scaledDensity = targetScaleDensity;

    }

    /**
     * 取消适配
     *
     * @param activity 适配的Activity
     */
    private static void cancelAdapt(Activity activity) {
        if (sNonCompatDensity == 0) {
            return;
        }
        DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
        activityDisplayMetrics.density = sNonCompatDensity;
        activityDisplayMetrics.densityDpi = sNonCompatDensityDpi;
        activityDisplayMetrics.scaledDensity = sNonCompatScaleDensity;

        DisplayMetrics appDisplayMetrics = sConfig.getApplication().getResources().getDisplayMetrics();
        appDisplayMetrics.density = sNonCompatDensity;
        appDisplayMetrics.densityDpi = sNonCompatDensityDpi;
        appDisplayMetrics.scaledDensity = sNonCompatScaleDensity;
    }

}
