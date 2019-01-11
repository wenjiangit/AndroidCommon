package com.wenjian.commonskill.okhttp;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.internal.io.FileSystem;
import okio.BufferedSource;
import okio.Okio;
import okio.Source;

/**
 * Description: OkioTest
 * Date: 2018/7/19
 *
 * @author jian.wen@ubtrobot.com
 */
public class OkioTest {


    public static void openAssets(Context context, String path) {
        InputStream inputStream = null;
        try {
            inputStream = context.getAssets().open(path);
            read(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void read(InputStream is) {
        try {
            BufferedSource bufferedSource = Okio.buffer(Okio.source(is));
            String utf8 = bufferedSource.readUtf8();
            Log.i("wj", "read: " + utf8);
            bufferedSource.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            Source source = FileSystem.SYSTEM.source(new File(""));
            BufferedSource bufferedSource = Okio.buffer(source);
            String line = bufferedSource.readUtf8Line();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
