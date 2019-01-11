package com.wenjian.commonskill.retrofit;


import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Description: RxService
 * Date: 2018/7/24
 *
 * @author jian.wen@ubtrobot.com
 */
public interface RxService {


    @GET("/weatherApi")
    Observable<NetResponse<Object>> getWeather(@Query("city") String city);

    @GET("/journalismApi")
    Single<Response<ResponseBody>> getNews();

    @GET("/meituApi")
    Observable<Response<String>> getMeitu(@Query("page") int page);
}
