package com.news.network.api;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2016/10/14.
 */
public interface NetWorkApi {

    @GET("huabian/")
    Observable<String> getEnjoy(@Query("key") String apikey,
                              @Query("num") int num,
                              @Query("page") int page);

    @GET("social/")
    Observable<String> getSocial(@Query("key") String apikey,
                              @Query("num") int num,
                              @Query("page") int page);

    @GET("keji/")
    Observable<String> getTechno(@Query("key") String apikey,
                              @Query("num") int num,
                              @Query("page") int page);

    @GET("guonei/")
    Observable<String> getChina(@Query("key") String apikey,
                                 @Query("num") int num,
                                 @Query("page") int page);

    @GET("apple/")
    Observable<String> getApple(@Query("key") String apikey,
                                @Query("num") int num,
                                @Query("page") int page);

}
