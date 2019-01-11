package com.wenjian.commonskill.glide;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;

import java.io.InputStream;

/**
 * Description: MyGlideModule
 * Date: 2018/7/11
 *
 * @author jian.wen@ubtrobot.com
 */

@com.bumptech.glide.annotation.GlideModule
public class MyGlideModule extends AppGlideModule {
    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        //设置磁盘缓存为外部存储(如果有内部存储存在,则使用内部存储)
        builder.setDiskCache(new ExternalPreferredCacheDiskCacheFactory(context));
        //设置加载不需要动画
        builder.setDefaultRequestOptions(RequestOptions.noAnimation());
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        //更改网络请求引擎为okhttp,先使用自定义的加载器,失败时候用默认加载器
        registry.replace(GlideUrl.class, InputStream.class,new OkhttpGlideUrlLoader.Factory());
    }
}
