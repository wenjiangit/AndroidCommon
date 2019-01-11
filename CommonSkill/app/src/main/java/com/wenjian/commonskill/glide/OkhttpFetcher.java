package com.wenjian.commonskill.glide;

import android.support.annotation.NonNull;
import android.util.Log;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.util.ContentLengthInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Description: 将glide网络请求引擎改为okhttp
 * Date: 2018/7/11
 *
 * @author jian.wen@ubtrobot.com
 */

public class OkhttpFetcher implements DataFetcher<InputStream> {

    private static final String TAG = "OkhttpFetcher";

    private GlideUrl mGlideUrl;
    private Call.Factory mClient;
    private ResponseBody mResponseBody;
    private volatile Call mCall;
    private InputStream mStream;

    public OkhttpFetcher(GlideUrl glideUrl, Call.Factory client) {
        mGlideUrl = glideUrl;
        mClient = client;
    }

    @Override
    public void loadData(@NonNull Priority priority, @NonNull DataCallback<? super InputStream> callback) {
        Request.Builder builder = new Request.Builder()
                .url(mGlideUrl.toStringUrl())
                .get();
        addHeaders(builder);
        mCall = getClient().newCall(builder.build());
        try {
            Response response = mCall.execute();
            mResponseBody = response.body();
            if (!response.isSuccessful() || mResponseBody == null) {
                throw new IOException("MRequest failed " + response.code() + ": " + response.message());
            }
            mStream = ContentLengthInputStream.obtain(mResponseBody.byteStream(), mResponseBody.contentLength());
            callback.onDataReady(mStream);
        } catch (IOException e) {
            Log.d(TAG, "Failed to load data for url", e);
            callback.onLoadFailed(e);
        }
    }

    private void addHeaders(Request.Builder builder) {
        Map<String, String> headers = mGlideUrl.getHeaders();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            builder.addHeader(entry.getKey(), entry.getValue());
        }
        builder.addHeader("engine", "okhttp");
    }

    private Call.Factory getClient() {
        if (mClient == null) {
            mClient = new OkHttpClient.Builder()
                    .addInterceptor(new LoggerInterceptor())
                    .addInterceptor(new ProgressInterceptor())
                    .build();
        }
        return mClient;
    }

    @Override
    public void cleanup() {
        if (mStream != null) {
            try {
                mStream.close();
            } catch (IOException e) {
                //ignore
            }
        }

        if (mResponseBody != null) {
            mResponseBody.close();
        }

    }

    @Override
    public void cancel() {
        if (mCall != null) {
            mCall.cancel();
        }
    }

    @NonNull
    @Override
    public Class<InputStream> getDataClass() {
        return InputStream.class;
    }

    @NonNull
    @Override
    public DataSource getDataSource() {
        return DataSource.REMOTE;
    }

    public String getId() {
        return mGlideUrl.getCacheKey();
    }

    private static class LoggerInterceptor implements Interceptor {
        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Request request = chain.request();

            Log.i("wj", "url ==> " + request.url());
            Log.i("wj", "method ==> " + request.method());
            Headers headers = request.headers();
            Log.i("wj", "==> headers ");
            for (int i = 0; i < headers.size(); i++) {
                Log.i("wj", headers.name(i) + " ==> " + headers.value(i));
            }
            Log.i("wj", "<== headers ");

            return chain.proceed(request);
        }
    }
}
