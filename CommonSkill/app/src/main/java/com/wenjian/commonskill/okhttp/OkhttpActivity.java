package com.wenjian.commonskill.okhttp;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.wenjian.commonskill.R;

import java.io.File;
import java.io.IOException;
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

public class OkhttpActivity extends AppCompatActivity {

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
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                builder.addFormDataPart("image",file.getName(),
                        RequestBody.create(MediaType.parse("image/jpeg"), file));
            }
        }

        MultipartBody multipartBody = builder.build();


        Request request = new Request.Builder()
                .url(BASE_URL + "/multi")
                .post(multipartBody)
                .build();

        mClient.newCall(request)
                .enqueue(CALLBACK);

    }
}
