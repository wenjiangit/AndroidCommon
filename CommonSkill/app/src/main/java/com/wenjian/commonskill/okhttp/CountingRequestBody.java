package com.wenjian.commonskill.okhttp;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * Description: 监听上传进度
 * Date: 2018/6/20
 *
 * @author jian.wen@ubtrobot.com
 */

public class CountingRequestBody extends RequestBody {

    private RequestBody mDelegate;
    private ProgressListener mProgressListener;

    public CountingRequestBody(RequestBody origin, ProgressListener progressListener) {
        mDelegate = origin;
        mProgressListener = progressListener;
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return mDelegate.contentType();
    }

    @Override
    public long contentLength() {
        try {
            return mDelegate.contentLength();
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public void writeTo(@NonNull BufferedSink sink) throws IOException {
        CountingSink countingSink = new CountingSink(sink);
        BufferedSink buffer = Okio.buffer(countingSink);
        mDelegate.writeTo(buffer);
        buffer.flush();
    }

    public interface ProgressListener {

        void onProgressChange(long progress, long total);
    }

    private class CountingSink extends ForwardingSink {

        private long byteWrite;

        CountingSink(Sink delegate) {
            super(delegate);
        }

        @Override
        public void write(@NonNull Buffer source, long byteCount) throws IOException {
            super.write(source, byteCount);
            byteWrite += byteCount;
            mProgressListener.onProgressChange(byteWrite, contentLength());
        }
    }


}
