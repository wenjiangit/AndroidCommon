package com.wenjian.commonskill.retrofit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import tech.linjiang.pandora.Pandora;

/**
 * Description: RetrofitManager
 * Date: 2018/7/24
 *
 * @author jian.wen@ubtrobot.com
 */
public class RetrofitManager {

    private static final String BASE_URL = "https://www.apiopen.top";
    private Retrofit mRetrofit;
    private ExampleService mService;
    private RxService mRxService;

    private RetrofitManager() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(Pandora.get().getInterceptor())
                .build();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    public synchronized static ExampleService getService() {
        if (get().mService == null) {
            get().mService = get().mRetrofit.create(ExampleService.class);
        }
        return get().mService;
    }

     public synchronized static RxService getRxService() {
        if (get().mRxService == null) {
            get().mRxService = get().mRetrofit.create(RxService.class);
        }
        return get().mRxService;
    }



    public static RetrofitManager get() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final RetrofitManager INSTANCE = new RetrofitManager();
    }


}
