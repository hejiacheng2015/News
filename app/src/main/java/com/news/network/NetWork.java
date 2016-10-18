package com.news.network;

import com.news.network.api.NetWorkApi;
import com.news.network.api.NetWorkApi2;

import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Administrator on 2016/10/13.
 */
public class NetWork {
    private static NetWorkApi netWorkApi;
    private static NetWorkApi2 netWorkApi2;
    private static OkHttpClient okHttpClient = new OkHttpClient();
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();

    public static Retrofit GetRetrofit(String httpUrl){
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(httpUrl)
//                    .addConverterFactory(gsonConverterFactory)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(rxJavaCallAdapterFactory)
                .build();
        return retrofit;
    }

    public static NetWorkApi getNetWorkApi() {
        if (netWorkApi == null) {
            netWorkApi = GetRetrofit("http://api.tianapi.com/").create(NetWorkApi.class);
        }
        return netWorkApi;
    }

    public static NetWorkApi2 getNetWorkApi2() {
        if (netWorkApi2 == null) {
            netWorkApi2 = GetRetrofit("http://apis.baidu.com/").create(NetWorkApi2.class);
        }
        return netWorkApi2;
    }

}
