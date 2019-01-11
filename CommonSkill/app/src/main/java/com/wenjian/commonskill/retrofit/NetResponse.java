package com.wenjian.commonskill.retrofit;

/**
 * Description: NetResponse
 * Date: 2018/7/25
 *
 * @author jian.wen@ubtrobot.com
 */
public class NetResponse<T> {

    public final int code;

    public final String msg;

    public final T data;

    public NetResponse(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }


    @Override
    public String toString() {
        return "NetResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
