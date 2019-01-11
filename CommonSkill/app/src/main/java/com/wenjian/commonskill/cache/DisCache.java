package com.wenjian.commonskill.cache;

import java.io.InputStream;

/**
 * Description: DisCache
 * Date: 2018/7/12
 *
 * @author jian.wen@ubtrobot.com
 */

public interface DisCache {

    long getCacheSize();

    void putString(String key, String value);

    String getString(String key);

    void putStream(String key, InputStream stream);

    InputStream getStream(String key);

    boolean delete(String key);

    void clear();

}
