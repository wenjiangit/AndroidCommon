package com.wenjian.commonskill.cache;

import android.support.v4.util.LruCache;

import com.wenjian.commonskill.utils.DigestUtil;

/**
 * Description: 参考glide的SafeKeyGenerator
 * Date: 2018/7/12
 *
 * @author jian.wen@ubtrobot.com
 */

class SafeKeyGenerator {

    private final LruCache<String, String> loadIdToSafeHash = new LruCache<>(1000);

    String getSafeKey(String key) {
        String safeKey;
        synchronized (loadIdToSafeHash) {
            safeKey = loadIdToSafeHash.get(key);
        }
        if (safeKey == null) {
            safeKey = DigestUtil.sha1(key.getBytes());
        }
        synchronized (loadIdToSafeHash) {
            loadIdToSafeHash.put(key, safeKey);
        }
        return safeKey;
    }

}
