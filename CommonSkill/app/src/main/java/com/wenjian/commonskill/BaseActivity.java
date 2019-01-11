package com.wenjian.commonskill;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.wenjian.commonskill.screencompat.ScreenAdapter;

import butterknife.Unbinder;
import tech.linjiang.pandora.Pandora;

/**
 * Description: BaseActivity
 * Date: 2018/7/13
 *
 * @author jian.wen@ubtrobot.com
 */

public abstract class BaseActivity extends AppCompatActivity implements ScreenAdapter{

    protected Unbinder mUnbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActionBar();
    }


    protected void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getIntent().getStringExtra("title"));
        }
    }

    @Override
    public boolean needCompat() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        //只有添加了这个actionbar的返回键才能生效
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.debug_toggle, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_open:
                Pandora.get().open();
                return true;
            case R.id.action_close:
                Pandora.get().close();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
