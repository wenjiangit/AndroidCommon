package com.wenjian.commonskill.okhttp.intercept;

import java.io.IOException;

/**
 * Description: MEndInterceptor
 * Date: 2018/7/18
 *
 * @author jian.wen@ubtrobot.com
 */
public class MEndInterceptor implements MInterceptor {
    @Override
    public MResponse intercept(Chain chain) throws IOException {
        return new MResponse.Builder().header("MEndInterceptor", "网络请求结果").build();
    }
}
