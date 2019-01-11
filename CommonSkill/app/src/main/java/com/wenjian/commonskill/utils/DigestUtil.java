package com.wenjian.commonskill.utils;

import android.support.annotation.Nullable;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Description: DigestUtil
 * Date: 2018/7/12
 *
 * @author jian.wen@ubtrobot.com
 */

public class DigestUtil {

    private static final char[] HEX_CHAR_ARRAY = "0123456789abcdef".toCharArray();
    /**
     * 32 bytes from sha-256 -> 64 hex chars.
     */
    private static final char[] SHA_256_CHARS = new char[64];
    /**
     * 20 bytes from sha-1 -> 40 chars.
     */
    private static final char[] SHA_1_CHARS = new char[40];

    private static final char[] MD5_CHARS = new char[32];

    /**
     * MD5算法加密,生成32位字符长度的字符串
     *
     * @param source 加密源
     * @return 加密后的字符串
     */
    public static String md5(byte[] source) {
        return encrypt(source, Encryption.MD5);
    }

    /**
     * 加密
     *
     * @param source     数据源
     * @param encryption 算法类型
     * @return 加密串
     */
    @Nullable
    private static String encrypt(byte[] source, Encryption encryption) {
        String result = null;
        try {
            MessageDigest md = MessageDigest.getInstance(encryption.type);
            md.update(source);
            switch (encryption) {
                case MD5:
                    result = md5BytesToHex(md.digest());
                    break;
                case SHA_1:
                    result = sha1BytesToHex(md.digest());
                    break;
                case SHA_256:
                    result = sha256BytesToHex(md.digest());
                    break;
                default:
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Returns the hex string of the given byte array representing a md5 hash.
     */
    private static String md5BytesToHex(byte[] bytes) {
        synchronized (MD5_CHARS) {
            return bytesToHex(bytes, MD5_CHARS);
        }
    }

    /**
     * Returns the hex string of the given byte array representing a SHA1 hash.
     */
    private static String sha1BytesToHex(byte[] bytes) {
        synchronized (SHA_1_CHARS) {
            return bytesToHex(bytes, SHA_1_CHARS);
        }
    }

    /**
     * Returns the hex string of the given byte array representing a SHA256 hash.
     */
    private static String sha256BytesToHex(byte[] bytes) {
        synchronized (SHA_256_CHARS) {
            return bytesToHex(bytes, SHA_256_CHARS);
        }
    }

    /**
     * 字节数组转为16进制字符串
     * Taken from:
     * http://stackoverflow.com/questions/9655181/convert-from-byte-array-to-hex-string-in-java/9655275#9655275
     *
     * @param bytes    字节数组
     * @param hexChars buffer数组
     * @return 加密字符串
     */
    private static String bytesToHex(byte[] bytes, char[] hexChars) {
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_CHAR_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_CHAR_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * sha1算法加密,生成40位字符长度的字符串
     *
     * @param source 加密源
     * @return 加密后的字符串
     */
    public static String sha1(byte[] source) {
        return encrypt(source, Encryption.SHA_1);
    }

    /**
     * sha256算法加密,生成64位字符长度的字符串
     *
     * @param source 加密源
     * @return 加密后的字符串
     */
    public static String sha256(byte[] source) {
        return encrypt(source, Encryption.SHA_256);
    }

    /**
     * 加密类型
     */
    public enum Encryption {

        MD5("MD5"), SHA_256("SHA-256"), SHA_1("SHA-1");

        private String type;

        Encryption(String type) {
            this.type = type;
        }
    }
}
