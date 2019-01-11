package com.wenjian.commonskill.okhttp;

import org.junit.Test;

import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import okhttp3.internal.io.FileSystem;
import okio.Buffer;
import okio.BufferedSource;
import okio.Okio;
import okio.Source;

import static org.junit.Assert.*;

/**
 * Description: OkioTest
 * Date: 2018/7/20
 *
 * @author jian.wen@ubtrobot.com
 */
public class OkioTest {

    @Test
    public void openAssets() {

        Buffer buffer = new Buffer();
        buffer.writeUtf8("你好,呵呵呵\n")
                .writeUtf8("d第二行\n")
                .writeUtf8("第三行代码");

        System.out.println(buffer.size());

        try {
            System.out.println(buffer.readUtf8());
            System.out.println(buffer.readUtf8Line());
            System.out.println(buffer.readUtf8Line());
            System.out.println(buffer.readUtf8Line());
        } catch (EOFException e) {
            e.printStackTrace();
        }

        System.out.println(buffer.size());

    }

    @Test
    public void read() {

        File file = new File("");
        BufferedSource bufferedSource = null;
        try {
            Source source = FileSystem.SYSTEM.source(file);
            bufferedSource = Okio.buffer(source);
            String utf8 = bufferedSource.readUtf8();
            System.out.println(utf8);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (bufferedSource != null) {
                try {
                    bufferedSource.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}