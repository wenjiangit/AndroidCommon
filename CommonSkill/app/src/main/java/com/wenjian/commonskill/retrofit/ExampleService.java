package com.wenjian.commonskill.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Description: ExampleService
 * Date: 2018/7/24
 *
 * @author jian.wen@ubtrobot.com
 */
public interface ExampleService {

    @GET("/weatherApi")
    Call<ResponseBody> getWeather(@Query("city") String city);

    @GET("/journalismApi")
    Call<ResponseBody> getNews();

     @GET("/meituApi")
    Call<NetResponse<Object>> getMeitu(@Query("page")int page);




}
