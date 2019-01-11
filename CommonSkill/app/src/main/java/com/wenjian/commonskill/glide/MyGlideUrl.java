package com.wenjian.commonskill.glide;

import com.bumptech.glide.load.model.GlideUrl;

/**
 * Description: 解决因为token参数不同导致缓存失效
 * Date: 2018/7/10
 *
 * @author jian.wen@ubtrobot.com
 */

public class MyGlideUrl extends GlideUrl {

    private String mUrl;

    public MyGlideUrl(String url) {
        super(url);
        this.mUrl = url;
    }

    @Override
    public String getCacheKey() {
        return mUrl.replace(findTokenParam(), "");
    }

    private String findTokenParam() {
        String result = "";
        int tokenIndex = mUrl.contains("?token=") ? mUrl.indexOf("?token=") : mUrl.indexOf("&token=");
        if (tokenIndex != -1) {
            int nextAndIndex = mUrl.indexOf("&", tokenIndex + 1);
            if (nextAndIndex != -1) {
                result = mUrl.substring(tokenIndex + 1, nextAndIndex + 1);
            } else {
                result = mUrl.substring(tokenIndex);
            }
        }
        return result;
    }


}
