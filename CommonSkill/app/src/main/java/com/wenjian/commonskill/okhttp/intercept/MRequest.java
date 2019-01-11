package com.wenjian.commonskill.okhttp.intercept;

import android.util.Log;

/**
 * Description: MRequest
 * Date: 2018/7/17
 *
 * @author jian.wen@ubtrobot.com
 */
public class MRequest {


    public void addHeader(String key, String value) {
        Log.i("wj", "addHeader: from " + key + " ==> " + value);
    }

}
