package com.wenjian.commonskill.glide;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Description: ProgressInterceptor
 * Date: 2018/7/11
 *
 * @author jian.wen@ubtrobot.com
 */

public class ProgressInterceptor implements Interceptor {

    private static final Map<String, ProgressListener> LISTENER_MAP = new HashMap<>();

    public static Map<String, ProgressListener> getListenerMap() {
        return Collections.unmodifiableMap(LISTENER_MAP);
    }

    public static void registerListener(String url, ProgressListener listener) {
        LISTENER_MAP.put(url, listener);
    }

    public static void unRegisterListener(String url) {
        LISTENER_MAP.remove(url);
    }


    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        //此处在处理响应时,对ResponseBody做了一个代理
        Response.Builder builder = response.newBuilder()
                .body(new ProgressResponseBody(request.url().toString(), response.body()));
        return builder.build();
    }
}
