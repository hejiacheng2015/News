package com.news.network.api;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2016/10/14.
 */
public interface NetWorkApi2 {
    @GET("txapi/world/world")
    Observable<String> getRecom(@Header("apikey") String apikey,
                              @Query("num") int num,
                              @Query("page") int page);

    @GET("txapi/tiyu/tiyu/")
    Observable<String> getSport(@Header("apikey") String apikey,
                              @Query("num") int num,
                              @Query("page") int page);

    @GET("appangel/shenzhentong/shenzhentong")
    Observable<String> getCard(@Header("apikey") String apikey,
                               @Query("id") String id,
                               @Query("format") String format);
}
