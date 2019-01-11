package com.wenjian.commonskill.okhttp.intercept;

import java.io.IOException;

/**
 * Description: MInterceptor
 * Date: 2018/7/17
 *
 * @author jian.wen@ubtrobot.com
 */
public interface MInterceptor {

    MResponse intercept(Chain chain) throws IOException;

    interface Chain{
        MRequest request();

        MResponse proceed(MRequest request) throws IOException;
    }
}
