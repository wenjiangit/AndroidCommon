package com.wenjian.commonskill;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.wenjian.commonskill.cache.DiskCacheManager;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ExecutorService mService = Executors.newSingleThreadExecutor();
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = findViewById(R.id.imageView);
    }

    public void putstring(View view) {
        mService.execute(new Runnable() {
            @Override
            public void run() {
                DiskCacheManager.getInstance().putString("haha", "test stringasadad");
            }
        });
    }

    public void getstring(View view) {
        mService.execute(new Runnable() {
            @Override
            public void run() {
                String haha = DiskCacheManager.getInstance().getString("haha");
                Log.d(TAG, "run: " + haha);
            }
        });
    }

    public void putImage(View view) {
        mService.execute(new Runnable() {
            @Override
            public void run() {

                try {
                    URL url = new URL("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1528724228169&di=a9934bf95cc51c765dd86a91db3ce3f3&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F9d82d158ccbf6c8154bdd5ccb63eb13533fa4008.jpg");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    InputStream inputStream = connection.getInputStream();
                    DiskCacheManager.getInstance().putStream("stream", inputStream);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getImage(View view) {
        mService.execute(new Runnable() {
            @Override
            public void run() {
                InputStream inputStream = DiskCacheManager.getInstance().getStream("stream");
                final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mImageView.setImageBitmap(bitmap);
                    }
                });
            }
        });
    }

    public void delete(View view) {
        DiskCacheManager.getInstance().delete();
    }

    public void getSize(View view) {
        long cacheSize = DiskCacheManager.getInstance().getCacheSize();
        Log.i(TAG, "getSize: " + cacheSize / 1024 + "KB");
    }
}
