package com.wenjian.commonskill.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.wenjian.commonskill.BaseActivity;
import com.wenjian.commonskill.R;
import com.wenjian.commonskill.cache.DisCache;
import com.wenjian.commonskill.cache.DiskCacheManager;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author ubt
 */
public class DiskCacheActivity extends BaseActivity {

    @BindView(R.id.iv_show)
    ImageView mIvShow;

    private DisCache mDisCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disk_cache);
        mUnbinder = ButterKnife.bind(this);
        mDisCache = DiskCacheManager.getInstance();
    }


    @OnClick(R.id.btn_put_str)
    void putStr() {
        mDisCache.putString("key1", "nidajhdajkdafka;fafafa1");
    }


    @OnClick(R.id.btn_get_str)
    void getStr() {
        String string = mDisCache.getString("key1");
        toast(string);
    }

    @OnClick(R.id.btn_put_stream)
    void putStream() {
        try {
            InputStream inputStream = getAssets().open("img/meinv.png");
            mDisCache.putStream("stream1", inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @OnClick(R.id.btn_get_stream)
    void getStream() {
        InputStream stream = mDisCache.getStream("stream1");
        Bitmap bitmap = BitmapFactory.decodeStream(stream);
        mIvShow.setImageBitmap(bitmap);
    }

    @OnClick(R.id.btn_get_size)
    void getSize() {
        long cacheSize = mDisCache.getCacheSize();
        toast(cacheSize / 1024 + "KB");
    }

    @OnClick(R.id.btn_clear)
    void clear() {
        mDisCache.clear();
    }

}
