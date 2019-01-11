package com.wenjian.commonskill.utils;

import okio.ByteString;

/**
 * Description: HashUtil
 * Date: 2018/7/20
 *
 * @author jian.wen@ubtrobot.com
 */
public class HashUtil {

    public static String md5(String src) {
        return ByteString.encodeUtf8(src).md5().hex();
    }

    public static String sha1(String src) {
        return ByteString.encodeUtf8(src).sha1().hex();
    }

    public static String sha256(String src) {
        return ByteString.encodeUtf8(src).sha256().hex();
    }

    public static String base64(String src) {
        return ByteString.encodeUtf8(src).base64();
    }

}
