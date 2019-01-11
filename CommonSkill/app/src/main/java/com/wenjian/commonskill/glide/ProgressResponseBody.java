package com.wenjian.commonskill.glide;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Description: ProgressResponseBody
 * Date: 2018/7/11
 *
 * @author jian.wen@ubtrobot.com
 */

public class ProgressResponseBody extends ResponseBody {

    private ResponseBody mSource;

    private ProgressListener mProgressListener;

    private BufferedSource mBufferedSource;

    private String mUrl;

    public ProgressResponseBody(String url, ResponseBody source) {
        mSource = source;
        mUrl = url;
        this.mProgressListener = ProgressInterceptor.getListenerMap().get(url);
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return mSource.contentType();
    }

    @Override
    public long contentLength() {
        return mSource.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (mBufferedSource == null) {
            mBufferedSource = Okio.buffer(new ProgressSource(mSource.source()));
        }
        return mBufferedSource;
    }

    private class ProgressSource extends ForwardingSource {

        int currentProgress = -1;
        private long mTotalReadBytes;

        ProgressSource(Source delegate) {
            super(delegate);
        }

        @Override
        public long read(@NonNull Buffer sink, long byteCount) throws IOException {
            long readBytes = super.read(sink, byteCount);
            long fullLength = mSource.contentLength();
            if (readBytes == -1) {
                mTotalReadBytes = fullLength;
            } else {
                mTotalReadBytes += readBytes;
            }

            int progress = (int) (100f * mTotalReadBytes / fullLength);

            Log.i("wj1", mUrl + " download progress: " + progress);
            if (mProgressListener != null && progress != currentProgress) {
                mProgressListener.onProgress(progress);
                Log.i("wj", mUrl + " download progress: " + progress);
            }

            if (mProgressListener != null && mTotalReadBytes == fullLength) {
                mProgressListener.onProgress(100);
                mProgressListener = null;
            }
            currentProgress = progress;
            return readBytes;
        }

    }
}
