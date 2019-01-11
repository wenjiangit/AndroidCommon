package com.wenjian.commonskill.glide;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelCache;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;

import java.io.InputStream;

import okhttp3.Call;

/**
 * Description: OkhttpGlideUrlLoader
 * Date: 2018/7/11
 *
 * @author jian.wen@ubtrobot.com
 */

public class OkhttpGlideUrlLoader implements ModelLoader<GlideUrl, InputStream> {

    private final ModelCache<GlideUrl, GlideUrl> mModelCache;
    private Call.Factory mClient;

    public OkhttpGlideUrlLoader(Call.Factory client, ModelCache<GlideUrl, GlideUrl> modelCache) {
        this.mModelCache = modelCache;
        this.mClient = client;
    }


    @Nullable
    @Override
    public LoadData<InputStream> buildLoadData(@NonNull GlideUrl model, int width, int height, @NonNull Options options) {
        GlideUrl url = model;
        if (mModelCache != null) {
            url = mModelCache.get(model, 0, 0);
            if (url == null) {
                mModelCache.put(model, 0, 0, model);
                url = model;
            }
        }
        return new LoadData<>(url, new OkhttpFetcher(url, mClient));
    }

    @Override
    public boolean handles(@NonNull GlideUrl glideUrl) {
        return true;
    }

    public static class Factory implements ModelLoaderFactory<GlideUrl, InputStream> {

        private final ModelCache<GlideUrl, GlideUrl> modelCache = new ModelCache<>(500);
        private Call.Factory mClient;

        public Factory() {
            this(null);
        }

        public Factory(Call.Factory client) {
            this.mClient = client;
        }


        @NonNull
        @Override
        public ModelLoader<GlideUrl, InputStream> build(@NonNull MultiModelLoaderFactory multiFactory) {
            return new OkhttpGlideUrlLoader(mClient, modelCache);
        }

        @Override
        public void teardown() {
            // Do nothing.
        }
    }
}
