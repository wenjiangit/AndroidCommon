package com.wenjian.commonskill.okhttp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.wenjian.commonskill.BaseActivity;
import com.wenjian.commonskill.R;
import com.wenjian.commonskill.okhttp.intercept.MInterceptor;
import com.wenjian.commonskill.okhttp.intercept.MRequest;
import com.wenjian.commonskill.okhttp.intercept.MResponse;
import com.wenjian.commonskill.okhttp.intercept.MyClient;
import com.wenjian.commonskill.room.AppDatabase;
import com.wenjian.commonskill.room.User;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import tech.linjiang.pandora.Pandora;

/**
 * @author ubt
 */
public class OkhttpActivity extends BaseActivity {

    private static final String BASE_URL = "http://10.10.25.67";
    private static final String TAG = "OkhttpActivity";
    private final Callback CALLBACK = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, IOException e) {
            Log.e(TAG, "onFailure: ", e);
        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
            if (response.isSuccessful()) {
                ResponseBody body = response.body();
                if (body != null) {
                    final String ret = body.string();
                    Log.i(TAG, "url: " + call.request().url());
                    Log.i(TAG, "msg: " + response.message());
                    Log.i(TAG, "code: " + response.code());
                    Log.i(TAG, "ret: " + ret);

                    OkhttpActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(OkhttpActivity.this, ret, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }
    };
    private final OkHttpClient mClient = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(Pandora.get().getInterceptor())
            .build();


    private MInterceptor mInterceptor1 = new MInterceptor() {
        @Override
        public MResponse intercept(Chain chain) throws IOException {
            MRequest request = chain.request();
            request.addHeader("mInterceptor1", "keep-alive");
            MResponse response = chain.proceed(request);
            MResponse.Builder builder = response.newBuilder().header("mInterceptor1", "mInterceptor1处理的结果");

            return builder.build();
        }
    };
    private MInterceptor mInterceptor2 = new MInterceptor() {
        @Override
        public MResponse intercept(Chain chain) throws IOException {
            MRequest request = chain.request();
            request.addHeader("mInterceptor2", "text/plain");
            MResponse response = chain.proceed(request);
            MResponse.Builder builder = response.newBuilder().header("mInterceptor2", "mInterceptor2处理的结果");

            return builder.build();
        }
    };
    private MInterceptor mInterceptor3 = new MInterceptor() {
        @Override
        public MResponse intercept(Chain chain) throws IOException {
            MRequest request = chain.request();
            request.addHeader("mInterceptor3", "if-cache");
            MResponse response = chain.proceed(request);
            MResponse.Builder builder = response.newBuilder().header("mInterceptor3", "mInterceptor3处理的结果");

            return builder.build();
        }
    };
    private MInterceptor mInterceptor4 = new MInterceptor() {
        @Override
        public MResponse intercept(Chain chain) throws IOException {
            MRequest request = chain.request();
            request.addHeader("mInterceptor4", "last-modified");
            MResponse response = chain.proceed(request);
            MResponse.Builder builder = response.newBuilder().header("mInterceptor4", "mInterceptor4处理的结果");
            return builder.build();
        }
    };

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i(TAG, "onNewIntent: ");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: ");
        setContentView(R.layout.activity_okhttp);
    }

    public void simpleGet(View view) {

        Request request = new Request.Builder()
                .url(BASE_URL)
                .get()
                .build();

        Call call = mClient.newCall(request);
        call.enqueue(CALLBACK);

    }

    public void postForm(View view) {
        FormBody formBody = new FormBody.Builder()
                .add("uername", "wenjian")
                .add("password", "111111")
                .build();

        Request request = new Request.Builder()
                .url(BASE_URL + "/postForm")
                .post(formBody)
                .build();

        mClient.newCall(request).enqueue(CALLBACK);
    }

    public void postMultiBody(View view) {
        File storageDirectory = Environment.getExternalStorageDirectory();


        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("username", "admin")
                .addFormDataPart("password", "admin");

        File imageDir = new File(storageDirectory, "/Pictures/");
        if (imageDir.exists()) {
            File[] files = imageDir.listFiles();
            for (File file : files) {
                builder.addFormDataPart("image", file.getName(),
                        RequestBody.create(MediaType.parse("image/jpeg"), file));
            }
        }

        MultipartBody multipartBody = builder.build();


        Request request = new Request.Builder()
                .url(BASE_URL + "/multi")
                .post(new CountingRequestBody(multipartBody, new CountingRequestBody.ProgressListener() {
                    @Override
                    public void onProgressChange(long progress, long total) {
                        Log.i(TAG, "上传进度: " + progress / total * 100f);
                    }
                }))
                .build();

        mClient.newCall(request)
                .enqueue(CALLBACK);

    }

    public void download(View view) {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setMessage("正在下载...");
        dialog.show();

        final File file = new File(this.getExternalCacheDir(), "test.zip");

        final Request request = new Request.Builder()
                .url(BASE_URL + "/public/uploads.rar")
                .build();

        mClient.newCall(request).enqueue(new DownloadCallback(file, new DownloadCallback.DownloadListener() {
            @Override
            public void onProgress(int progress, boolean done) {
                Log.i(TAG, "onProgress: " + progress);
                if (done) {
                    dialog.dismiss();
                } else {
                    dialog.setProgress(progress);
                }
            }

            @Override
            public void onFailed(Exception e) {
                dialog.dismiss();

            }
        }));


    }

    public void intercept(View view) {
        Log.i(TAG, "intercept: ");
        MyClient client = new MyClient.Builder()
                .addInterceptor(mInterceptor1)
                .addInterceptor(mInterceptor2)
                .addInterceptor(mInterceptor3)
                .addInterceptor(mInterceptor4)
                .build();
        try {
            MResponse response = client.execute(new MRequest());
        } catch (IOException e) {
            e.printStackTrace();
        }

/*
        List<User> users = AppDatabase.getInstance(this).userDao().loadAll();

        Log.i(TAG, "intercept: " + users);*/

        OkioTest.openAssets(this, "test.txt");




    }


    public String get() throws IOException {
        OkHttpClient client = new OkHttpClient.Builder().build();

        Request request = new Request.Builder()
                .url("http://www.baidu.com")
                .build();

        Call call = client.newCall(request);
        Response response = call.execute();

        return response.body().string();

    }


}
