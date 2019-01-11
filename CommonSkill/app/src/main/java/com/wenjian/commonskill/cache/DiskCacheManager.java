package com.wenjian.commonskill.cache;

import android.content.Context;
import android.os.Environment;

import com.jakewharton.disklrucache.DiskLruCache;
import com.wenjian.commonskill.AppConfigure;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;


/**
 * Description: 基于DiskLruCache的磁盘缓存管理
 * 参考:https://blog.csdn.net/guolin_blog/article/details/28863651
 * Date: 2018/6/11
 *
 * @author jian.wen@ubtrobot.com
 */

public class DiskCacheManager implements DisCache {
    /**
     * /sdcard/Android/data/<application package>/cache/test
     * 可以根据实际业务进行更改
     */
    private static final String CACHE_DIR_NAME = "disk_cache_manager";

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
    private static final int MAX_SIZE = 100 * 1024 * 1024;
    private final File mDiskCacheDir;
    private DiskLruCache mDiskLruCache;

    private SafeKeyGenerator mKeyGenerator;

    private DiskCacheManager() {
        mKeyGenerator = new SafeKeyGenerator();
        mDiskCacheDir = getDiskCacheDir(AppConfigure.getApp(), CACHE_DIR_NAME);
        if (!mDiskCacheDir.exists()) {
            if (!mDiskCacheDir.mkdirs()) {
                throw new RuntimeException("创建缓存文件夹失败,可能是没有申请权限");
            }
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

    public static DisCache getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * 获取总缓存大小
     *
     * @return size
     */
    @Override
    public long getCacheSize() {
        try {
            return getDiskLruCache().size();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 存储字符串
     *
     * @param key   缓存的键
     * @param value 需要存储的字符串
     */
    @Override
    public void putString(String key, String value) {
        String cacheKey = mKeyGenerator.getSafeKey(key);
        try {
            DiskLruCache.Editor editor = getDiskLruCache().edit(cacheKey);
            if (editor != null) {
                editor.set(0, value);
                editor.commit();
                getDiskLruCache().flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private DiskLruCache getDiskLruCache() throws IOException {
        if (mDiskLruCache == null) {
            mDiskLruCache = DiskLruCache.open(mDiskCacheDir, APP_VERSION, VALUE_COUNT, MAX_SIZE);
        }
        return mDiskLruCache;
    }

    /**
     * 获取字符串
     *
     * @param key 缓存的键
     * @return 存储的字符串
     */
    @Override
    public String getString(String key) {
        String cacheKey = mKeyGenerator.getSafeKey(key);
        try {
            DiskLruCache.Snapshot snapshot = getDiskLruCache().get(cacheKey);
            if (snapshot != null) {
                return snapshot.getString(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 存储二进制数据
     *
     * @param key 缓存的键
     * @param in  输入流
     */
    @Override
    public void putStream(String key, InputStream in) {
        String cacheKey = mKeyGenerator.getSafeKey(key);
        try {
            DiskLruCache.Editor editor = getDiskLruCache().edit(cacheKey);
            if (editor != null) {
                boolean success = DiskCacheHelper.convert(in, editor.newOutputStream(0));
                if (success) {
                    editor.commit();
                } else {
                    editor.abort();
                }
                getDiskLruCache().flush();
            }
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
    @Override
    public InputStream getStream(String key) {
        String cacheKey = mKeyGenerator.getSafeKey(key);
        try {
            DiskLruCache.Snapshot snapshot = getDiskLruCache().get(cacheKey);
            if (snapshot != null) {
                return snapshot.getInputStream(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean delete(String key) {
        String safeKey = mKeyGenerator.getSafeKey(key);
        try {
            return getDiskLruCache().remove(safeKey);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void clear() {
        try {
            getDiskLruCache().delete();
            resetDiskCache();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 重置LruCache
     */
    private void resetDiskCache() {
        mDiskLruCache = null;
    }

    private static class Holder {
        private static final DiskCacheManager INSTANCE = new DiskCacheManager();
    }

}
