package com.wenjian.commonskill.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestOptions;
import com.wenjian.commonskill.BaseActivity;
import com.wenjian.commonskill.R;
import com.wenjian.commonskill.glide.ProgressInterceptor;
import com.wenjian.commonskill.glide.ProgressListener;
import com.wenjian.commonskill.glide.ProgressViewTarget;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * @author ubt
 */
public class GlideActivity extends BaseActivity {

    public static final String PNG1 = "http://img5.imgtn.bdimg.com/it/u=2171371372,3068864321&fm=27&gp=0.jpg";
    public static final String PNG2 = "http://img4.imgtn.bdimg.com/it/u=3036260638,1523624176&fm=27&gp=0.jpg";
    public static final String GIF = "http://p1.pstatp.com/large/166200019850062839d3.gif";

    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();


    @BindView(R.id.iv_show)
    ImageView mIvShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glide);
        mUnbinder = ButterKnife.bind(this);
    }


    @OnClick(R.id.btn_simple)
    void simpleLoad() {
        Glide.with(this)
                .load(PNG1)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(mIvShow);
    }


    @OnClick(R.id.btn_circle_crop)
    void circleCrop() {
        Glide.with(this)
                .load(PNG2)
                .apply(RequestOptions.circleCropTransform())
                .into(mIvShow);
    }


    @OnClick(R.id.btn_center_crop)
    void centerCrop() {
        Glide.with(this)
                .load(PNG1)
                .apply(RequestOptions.centerCropTransform())
                .into(mIvShow);
    }

    @OnClick(R.id.btn_fit_center)
    void fitCenter() {
        Glide.with(this)
                .load(PNG2)
                .apply(RequestOptions.fitCenterTransform())
                .into(mIvShow);
    }

    @OnClick(R.id.btn_blur)
    void blur() {
        Glide.with(this)
                .load(PNG1)
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(20, 5)))
                .into(mIvShow);
    }


    @OnClick(R.id.btn_download_only)
    void downloadOnly() {

        EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                FutureTarget<File> futureTarget = Glide.with(GlideActivity.this)
                        .downloadOnly()
                        .load(GIF)
                        .submit();
                try {
                    final File file = futureTarget.get();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toast(file.getPath());
                            Log.i("wj", "run: " + file.getAbsolutePath());
                        }
                    });

                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @OnClick(R.id.btn_download_with_progress)
    void downloadProgress() {

        ProgressInterceptor.registerListener(GIF, new ProgressListener() {
            @Override
            public void onProgress(int progress) {
                Log.i("wj", "onProgress: " + progress);
            }
        });

        Glide.with(this)
                .load(GIF)
                .apply(RequestOptions
                        .diskCacheStrategyOf(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true))
                .into(new ProgressViewTarget(mIvShow, GIF));
    }


}
