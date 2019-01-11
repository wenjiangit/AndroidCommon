package com.wenjian.commonskill.glide;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.request.transition.Transition;

/**
 * Description: ProgressViewTarget
 * Date: 2018/7/13
 *
 * @author jian.wen@ubtrobot.com
 */

public class ProgressViewTarget extends ViewTarget<View, Drawable> {

    private String mUrl;
    public ProgressViewTarget(@NonNull View view, @NonNull String url) {
        super(view);
        mUrl = url;
    }

    @Override
    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
        if (view instanceof ImageView) {
            ((ImageView) view).setImageDrawable(resource);
        }
        ProgressInterceptor.unRegisterListener(mUrl);
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
