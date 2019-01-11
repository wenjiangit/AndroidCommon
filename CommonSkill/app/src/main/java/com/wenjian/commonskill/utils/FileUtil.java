package com.wenjian.commonskill.utils;

import android.support.annotation.Nullable;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.internal.io.FileSystem;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import okio.Source;

/**
 * Description: 使用okio操作文件
 * Date: 2018/7/20
 *
 * @author jian.wen@ubtrobot.com
 */
public class FileUtil {

    /**
     * 读取文件内容,返回字符串
     *
     * @param file 目标文件
     * @return 字符串
     */
    @Nullable
    public static String readFile(File file) {
        BufferedSource source = null;
        try {
            source = Okio.buffer(FileSystem.SYSTEM.source(file));
            return source.readUtf8();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeQuietly(source);
        }
        return null;
    }

    /**
     * 关闭可关闭的资源
     *
     * @param closeable 可关闭的对象
     */
    public static void closeQuietly(@Nullable Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (RuntimeException rethrow){
                throw rethrow;
            } catch (IOException e) {
                //ignore
            }
        }
    }

    /**
     * 将输入流写入到文件
     *
     * @param inputStream 输入流
     * @param file        目标文件
     * @return 写入成功返回true, 否则false
     */
    public static boolean writeStream2file(InputStream inputStream, File file) {
        BufferedSink bufferedSink = null;
        Source source = null;
        try {
            bufferedSink = Okio.buffer(FileSystem.SYSTEM.sink(file));
            source = Okio.source(inputStream);
            bufferedSink.writeAll(source);
            bufferedSink.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeQuietly(bufferedSink);
            closeQuietly(source);
        }
        return false;
    }

    /**
     * 写入字符串到文件
     *
     * @param file 目标文件
     * @param src  需要写入的字符串
     * @return 写入成功返回true, 否则false
     */
    public static boolean writeString2file(String src, File file) {
        BufferedSink sink = null;
        try {
            sink = Okio.buffer(FileSystem.SYSTEM.sink(file));
            sink.writeUtf8(src);
            sink.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeQuietly(sink);
        }
        return false;
    }

    /**
     * 将输入流转化为字符串
     *
     * @param inputStream 输入流
     * @return 转换结果
     */
    @Nullable
    public static String stream2string(InputStream inputStream) {
        BufferedSource bufferedSource = Okio.buffer(Okio.source(inputStream));
        try {
            return bufferedSource.readUtf8();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeQuietly(bufferedSource);
        }
        return null;
    }

    /**
     * 读取文件转化为输入流
     *
     * @param file 文件
     * @return 输入流
     */
    @Nullable
    public static InputStream readFile2stream(File file) {
        BufferedSource source = null;
        try {
            source = Okio.buffer(FileSystem.SYSTEM.source(file));
            return source.inputStream();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            closeQuietly(source);
        }
        return null;
    }


}
