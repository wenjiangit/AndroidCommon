package com.wenjian.commonskill.okhttp.intercept;

import java.io.IOException;
import java.util.List;

/**
 * Description: RealInterceptorChain
 * Date: 2018/7/17
 *
 * @author jian.wen@ubtrobot.com
 */
public class RealInterceptorChain implements MInterceptor.Chain {

    private MRequest mRequest;

    private int index;

    private List<MInterceptor> mInterceptors;

    public RealInterceptorChain(MRequest request, int index, List<MInterceptor> interceptors) {
        mRequest = request;
        this.index = index;
        mInterceptors = interceptors;
    }

    @Override
    public MRequest request() {
        return mRequest;
    }

    @Override
    public MResponse proceed(MRequest request) throws IOException {
        if (index >= mInterceptors.size()) {
            throw new AssertionError();
        }
        RealInterceptorChain next = new RealInterceptorChain(request(), index + 1, mInterceptors);
        MInterceptor interceptor = mInterceptors.get(index);
        MResponse response = interceptor.intercept(next);
        if (response == null) {
            throw new NullPointerException("response is null");
        }
        return response;
    }
}
