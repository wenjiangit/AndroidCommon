package com.wenjian.commonskill;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.VelocityTracker;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wenjian.commonskill.okhttp.OkhttpActivity;
import com.wenjian.commonskill.ui.AnimatorActivity;
import com.wenjian.commonskill.ui.BitmapActivity;
import com.wenjian.commonskill.ui.IntentServiceActivity;
import com.wenjian.commonskill.ui.RetrofitActivity;
import com.wenjian.commonskill.ui.DigestActivity;
import com.wenjian.commonskill.ui.DiskCacheActivity;
import com.wenjian.commonskill.ui.GlideActivity;
import com.wenjian.commonskill.ui.ScrollerActivity;
import com.wenjian.commonskill.ui.VelocityTrackerActivity;
import com.wenjian.commonskill.window.WindowActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author ubt
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final List<Category> CATEGORY = new ArrayList<>();

    static {
        registerComponent("LRU磁盘缓存", DiskCacheActivity.class);
        registerComponent("Okhttp上传下载", OkhttpActivity.class);
        registerComponent("摘要算法", DigestActivity.class);
        registerComponent("Glide高级", GlideActivity.class);
        registerComponent("Retrofit详解", RetrofitActivity.class);
        registerComponent("动画高级", AnimatorActivity.class);
        registerComponent("滑动速度追踪", VelocityTrackerActivity.class);
        registerComponent("Scroller", ScrollerActivity.class);
        registerComponent("Window", WindowActivity.class);
        registerComponent("IntentService", IntentServiceActivity.class);
        registerComponent("BitmapActivity", BitmapActivity.class);

    }

    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    private Unbinder mUnbinder;

    private static void registerComponent(String title, Class<?> clz) {
        CATEGORY.add(new Category(title, clz));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUnbinder = ButterKnife.bind(this);
        setupRecycler();
    }

    private void setupRecycler() {
        MainAdapter adapter = new MainAdapter(CATEGORY);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Category item = (Category) adapter.getItem(position);
                Intent intent = new Intent(MainActivity.this, item.clz);
                intent.putExtra("title", item.title);
                startActivity(intent);
            }
        });
        mRecycler.setHasFixedSize(true);
        mRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecycler.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }

    }

    private static class MainAdapter extends BaseQuickAdapter<Category, BaseViewHolder> {

        MainAdapter(@Nullable List<Category> data) {
            super(R.layout.rv_item_main, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, Category item) {
            helper.setText(R.id.tv_title, item.title);
        }
    }

    static class Category {
        String title;
        Class<?> clz;

        Category(String title, Class<?> clz) {
            this.title = title;
            this.clz = clz;
        }

    }
}
