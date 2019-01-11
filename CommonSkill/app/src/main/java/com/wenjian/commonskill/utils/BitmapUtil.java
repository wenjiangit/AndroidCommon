package com.wenjian.commonskill.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import timber.log.Timber;

/**
 * Description: BitmapUtil
 * Date: 2018/8/23
 *
 * @author jian.wen@ubtrobot.com
 */
public class BitmapUtil {

    public static Bitmap decodeBitmapFromRes(Context context, int resId, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeResource(context.getResources(), resId, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        Timber.i("inSampleSize=%d", options.inSampleSize);

        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeResource(context.getResources(), resId, options);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int outWidth = options.outWidth;
        final int outHeight = options.outHeight;

        Timber.i("outWidth=%d,outHeight=%d", outWidth, outHeight);

        int inSampleSize = 1;
        if (outWidth > reqWidth || outHeight > reqHeight) {

            while ((outWidth / inSampleSize) >= reqWidth || (outHeight / inSampleSize) >= reqHeight) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
}
