package com.wenjian.commonskill.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.orhanobut.logger.Logger;
import com.wenjian.commonskill.R;
import com.wenjian.commonskill.retrofit.NetResponse;
import com.wenjian.commonskill.retrofit.RetrofitManager;
import com.wenjian.commonskill.utils.LogUtil;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit2);

    }

    public void simple(View view) {
        Call<NetResponse<Object>> call = RetrofitManager.getService().getMeitu(1);
        call.enqueue(new Callback<NetResponse<Object>>() {
            @Override
            public void onResponse(@NonNull Call<NetResponse<Object>> call, @NonNull Response<NetResponse<Object>> response) {
                if (response.isSuccessful()) {
                    NetResponse<Object> ret = response.body();
                    LogUtil.d("wj", "meitu: %s", ret);
                }
            }

            @Override
            public void onFailure(@NonNull Call<NetResponse<Object>> call, Throwable t) {

            }
        });

    }

    public void rxSimple(View view) {
        Observable<NetResponse<Object>> observable = RetrofitManager.getRxService().getWeather("深圳");
        observable.subscribeOn(Schedulers.io())
                .subscribe(new Observer<NetResponse<Object>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(NetResponse<Object> stringResponse) {
                        LogUtil.i("%s", stringResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("exception == %s", e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
