package com.wenjian.commonskill.utils;

import timber.log.Timber;

/**
 * Description: LogUtil
 * Date: 2018/7/25
 *
 * @author jian.wen@ubtrobot.com
 */
public class LogUtil {

    public static void d(String tag, String message, Object... args) {
        Timber.tag(tag).d(message, args);
    }

    public static void d(String message, Object... args) {
        Timber.d(message, args);
    }

    public static void i(String tag, String message, Object... args) {
        Timber.tag(tag).i(message, args);
    }

    public static void i(String message, Object... args) {
        Timber.i(message, args);
    }

    public static void e(String message, Object... args) {
        Timber.e(message, args);
    }


}
