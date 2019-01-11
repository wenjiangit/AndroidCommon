package com.wenjian.commonskill.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;

import com.wenjian.commonskill.R;
import com.wenjian.commonskill.utils.BitmapUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class BitmapActivity extends AppCompatActivity {

    @BindView(R.id.imageView2)
    ImageView mImageView2;
    @BindView(R.id.button2)
    Button mButton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitmap);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button2)
    void load() {
        Bitmap bitmap = BitmapUtil.decodeBitmapFromRes(this, R.mipmap.wp5, 400, 400);

        Timber.i("w=%d,h=%d", bitmap.getWidth(), bitmap.getHeight());

        long maxMemory = Runtime.getRuntime().maxMemory();
        Timber.i("maxMemory=%dKB", maxMemory/1024);
        mImageView2.setImageBitmap(bitmap);
    }


}
