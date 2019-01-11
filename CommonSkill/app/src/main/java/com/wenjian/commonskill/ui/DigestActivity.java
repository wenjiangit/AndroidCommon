package com.wenjian.commonskill.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wenjian.commonskill.BaseActivity;
import com.wenjian.commonskill.R;
import com.wenjian.commonskill.utils.DigestUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author ubt
 */
public class DigestActivity extends BaseActivity {

    @BindView(R.id.editText)
    EditText mEditText;
    @BindView(R.id.textView)
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_digest);
        mUnbinder = ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_md5)
    void md5() {
        String md5 = DigestUtil.md5(mEditText.getText().toString().getBytes());
        mTextView.setText(md5);
    }

    @OnClick(R.id.btn_sha1)
    void sha1() {
        String sha1 = DigestUtil.sha1(mEditText.getText().toString().getBytes());
        mTextView.setText(sha1);
    }

    @OnClick(R.id.btn_sha256)
    void sha256() {
        String sha256 = DigestUtil.sha256(mEditText.getText().toString().getBytes());
        mTextView.setText(sha256);
    }


}
