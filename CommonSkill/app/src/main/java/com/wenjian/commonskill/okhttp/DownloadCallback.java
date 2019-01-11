package com.wenjian.commonskill.okhttp;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Description: DownloadCallback
 * Date: 2018/7/16
 *
 * @author jian.wen@ubtrobot.com
 */

public class DownloadCallback implements Callback {

    private static final String TAG = "DownloadCallback";

    private File mFile;

    private DownloadListener mListener;

    private int currentProgress = -1;

    public DownloadCallback(File file) {
        this(file, null);
    }

    public DownloadCallback(File file, DownloadListener listener) {
        mFile = file;
        mListener = listener;
    }

    @Override
    public void onFailure(@NonNull Call call, @NonNull IOException e) {
        Log.e(TAG, "onFailure: ", e);
        if (mListener != null) {
            mListener.onFailed(e);
        }
    }

    @Override
    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
        ResponseBody body = response.body();
        if (!response.isSuccessful() || body == null) {
            return;
        }
        long contentLength = body.contentLength();
        int start = 0;
        try {
            InputStream is = body.byteStream();
            FileOutputStream fis = new FileOutputStream(mFile);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer)) != -1) {
                fis.write(buffer, 0, len);
                start += len;

                int progress = (int) (100f * start / contentLength);

                if (mListener != null && progress != currentProgress) {
                    mListener.onProgress(progress, false);
                }
                if (mListener != null && start == contentLength) {
                    mListener.onProgress(100, true);
                }
                currentProgress = progress;
            }
            fis.flush();
            fis.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public interface DownloadListener {

        void onProgress(int progress,boolean done);

        void onFailed(Exception e);

    }

}
