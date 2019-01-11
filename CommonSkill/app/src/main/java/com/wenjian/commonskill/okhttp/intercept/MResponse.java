package com.wenjian.commonskill.okhttp.intercept;

import android.util.ArrayMap;
import android.util.Log;

import java.util.Map;

/**
 * Description: MResponse
 * Date: 2018/7/17
 *
 * @author jian.wen@ubtrobot.com
 */
public class MResponse {

    private Map<String, String> headers;

    public MResponse(Builder builder) {
        this.headers = builder.headers;
    }

    public Builder newBuilder() {
        return new Builder(this);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public static class Builder {

        private Map<String, String> headers;

        public Builder(){
        }

        public Builder(MResponse response) {
            this.headers = response.headers;
        }

        public Builder header(String key, String value) {
            if (headers == null) {
                headers = new ArrayMap<>();
            }
            headers.put(key, value);
            Log.i("wj", "header: " + key + " ==> " + value);
            return this;
        }

        public MResponse build() {
            return new MResponse(this);
        }
    }
}
