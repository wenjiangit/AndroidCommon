package com.wenjian.commonskill.cache;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.jakewharton.disklrucache.DiskLruCache;
import com.wenjian.commonskill.AppConfigure;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;


/**
 * Description: 基于DiskLruCache的磁盘缓存管理
 * 参考:https://blog.csdn.net/guolin_blog/article/details/28863651
 * Date: 2018/6/11
 *
 * @author jian.wen@ubtrobot.com
 */

public class DiskCacheManager {

    private static final String TAG = "DiskCacheManager";

    /**
     * /sdcard/Android/data/<application package>/cache/disklru
     * 可以根据实际业务进行更改
     */
    private static final String CACHE_DIR_NAME = "disklru";

    /**
     * 这里如果设置成app版本号,那么每次版本更新都会删除全部缓存
     * 也可以设置成一个固定值(1),那么版本更新并不会删除缓存
     * 根据实际情况设置
     */
    private static final int APP_VERSION = 1;

    /**
     * 同一个key可以对应多少个缓存文件,一般为1
     */
    private static final int VALUE_COUNT = 1;

    /**
     * 最大的缓存大小,10M比较合适
     */
    private static final int MAX_SIZE = 10 * 1024 * 1024;

    private DiskLruCache mDiskLruCache;

    private DiskCacheManager() {
        openCacheConnection();
    }

    /**
     * 开启缓存连接
     */
    private void openCacheConnection() {
        File diskCacheDir = getDiskCacheDir(AppConfigure.getApp(), CACHE_DIR_NAME);
        if (!diskCacheDir.exists()) {
            diskCacheDir.mkdirs();
        }

        try {
            mDiskLruCache = DiskLruCache.open(diskCacheDir, APP_VERSION, VALUE_COUNT, MAX_SIZE);
        } catch (IOException e) {
            Log.e(TAG, "create disLruCache error: ", e);
        }
    }

    private File getDiskCacheDir(Context context, String uniqueName) {
        File externalCacheDir;
        if (Objects.equals(Environment.getExternalStorageState(), Environment.MEDIA_MOUNTED)) {
            externalCacheDir = context.getExternalCacheDir();
        } else {
            externalCacheDir = context.getCacheDir();
        }
        return new File(externalCacheDir, uniqueName);
    }

    public static DiskCacheManager getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * 存储字符串
     *
     * @param key   缓存的键
     * @param value 需要存储的字符串
     */
    public void putString(String key, String value) {
        reopenIfNeed();
        String cacheKey = makeCacheKey(key);
        try {
            DiskLruCache.Editor editor = mDiskLruCache.edit(cacheKey);
            if (editor != null) {
                editor.set(0, value);
                editor.commit();
            }
            mDiskLruCache.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 在必要的时候重新打开缓存
     */
    private void reopenIfNeed() {
        if (!mDiskLruCache.isClosed()) {
            return;
        }
        openCacheConnection();
    }

    /**
     * 通过MD5摘要算法生成的合格的文件名
     *
     * @param key 缓存的键
     * @return 生成的32位的字符串
     */
    private String makeCacheKey(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = DiskCacheHelper.bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    /**
     * 存储二进制数据
     *
     * @param key 缓存的键
     * @param in  输入流
     */
    public void putStream(String key, InputStream in) {
        reopenIfNeed();
        String cacheKey = makeCacheKey(key);
        try {
            DiskLruCache.Editor editor = mDiskLruCache.edit(cacheKey);
            if (editor != null) {
                DiskCacheHelper.convert(in, editor.newOutputStream(0));
                editor.commit();
            }
            mDiskLruCache.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从本地获取输入流
     *
     * @param key 缓存的键
     * @return 输入流
     */
    public InputStream getStream(String key) {
        reopenIfNeed();
        String cacheKey = makeCacheKey(key);
        try {
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(cacheKey);
            if (snapshot != null) {
                return snapshot.getInputStream(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取字符串
     *
     * @param key 缓存的键
     * @return 存储的字符串
     */
    public String getString(String key) {
        reopenIfNeed();
        String cacheKey = makeCacheKey(key);
        try {
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(cacheKey);
            if (snapshot != null) {
                return snapshot.getString(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取总缓存大小
     *
     * @return size
     */
    public long getCacheSize() {
        return mDiskLruCache.size();
    }

    /**
     * 删除所有缓存
     */
    public void delete() {
        try {
            mDiskLruCache.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class Holder {
        private static final DiskCacheManager INSTANCE = new DiskCacheManager();
    }

}
