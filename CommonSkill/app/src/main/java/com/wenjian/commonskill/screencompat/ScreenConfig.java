package com.wenjian.commonskill.screencompat;

import android.app.Application;

/**
 * Description: ScreenConfig
 * Date: 2018/8/22
 *
 * @author jian.wen@ubtrobot.com
 */
public class ScreenConfig {

    /**
     * 设计图宽度,以dp为单位
     */
    private int mDesignWidthDp;

    /**
     * 设计图高度,以dp为单位
     */
    private int mDesignHeightDp;

    /**
     * 以宽度为基准
     */
    private boolean mBaseOnWidth;

    private Application mApplication;

    private ScreenConfig(Builder builder) {
        this.mDesignHeightDp = builder.designHeightDp;
        this.mDesignWidthDp = builder.designWidthDp;
        this.mBaseOnWidth = builder.baseOnWidth;
        this.mApplication = builder.application;
    }

    public boolean isBaseOnWidth() {
        return mBaseOnWidth;
    }

    public Application getApplication() {
        return mApplication;
    }

    public int getDesignHeight() {
        return mDesignHeightDp;
    }

    public int getDesignWidth() {
        return mDesignWidthDp;
    }

    public static class Builder {

        int designWidthDp;

        int designHeightDp;

        Application application;

        boolean baseOnWidth = true;

        public Builder(Application application) {
            this.application = application;
        }

        public Builder designWidth(int designWidth) {
            this.designWidthDp = designWidth;
            return this;
        }

        public Builder baseOnWidth(boolean baseOnWidth) {
            this.baseOnWidth = baseOnWidth;
            return this;
        }

        public Builder designHeight(int designHeight) {
            this.designHeightDp = designHeight;
            return this;
        }

        public ScreenConfig build() {
            if (baseOnWidth) {
                if (designWidthDp == 0) {
                    throw new IllegalArgumentException("designWidthDp=0");
                }
            } else {
                if (designHeightDp == 0) {
                    throw new IllegalArgumentException("designHeightDp=0");
                }
            }
            return new ScreenConfig(this);
        }
    }


}
