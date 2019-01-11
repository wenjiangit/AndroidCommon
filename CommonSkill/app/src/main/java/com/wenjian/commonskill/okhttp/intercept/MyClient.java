package com.wenjian.commonskill.okhttp.intercept;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: MyClient
 * Date: 2018/7/17
 *
 * @author jian.wen@ubtrobot.com
 */
public class MyClient {

    private final List<MInterceptor> mInterceptors;

    public MyClient(Builder builder) {
        this.mInterceptors = builder.interceptors;
    }

    public List<MInterceptor> interceptors() {
        return mInterceptors;
    }

    public MResponse execute(MRequest request) throws IOException {
        return getResponseWithInterceptorChain(request);
    }

    private MResponse getResponseWithInterceptorChain(MRequest request) throws IOException {
        List<MInterceptor> interceptors = new ArrayList<>(interceptors());
        interceptors.add(new MEndInterceptor());
        RealInterceptorChain chain = new RealInterceptorChain(request, 0, interceptors);
        return chain.proceed(request);
    }

    public static class Builder{

        private List<MInterceptor> interceptors = new ArrayList<>();

        public Builder addInterceptor(MInterceptor interceptor) {
            this.interceptors.add(interceptor);
            return this;
        }

        public MyClient build() {
            return new MyClient(this);
        }
    }
}
