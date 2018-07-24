package com.sgcai.router.common.retrofit;


import com.sgcai.router.common.utils.GlobalConstants;

import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络请求生成器
 */

public class ServiceGenerator {


    private Retrofit mRetrofit;

    public ServiceGenerator() {

        /**创建okhttp，拼接log拦截器、cache拦截器、请求头拦截器**/
        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new RequestInterceptor())// 网络请求拦截器
                .connectTimeout(10, TimeUnit.SECONDS) // 设置超时时间
                .retryOnConnectionFailure(true) //失败重连
                .connectionPool(new ConnectionPool(5, 5, TimeUnit.SECONDS))//ConnectionPool的keepAliveDuration时间，让每次连接5秒后就关闭。
                .build();

        /**创建retrofit，加入gson转化器、rxjava适配器**/
        mRetrofit = new Retrofit.Builder()
                .baseUrl(GlobalConstants.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    /**
     * @param clazz 接口
     * @param <T>
     * @return
     */
    public <T> T createService(Class<T> clazz) {
        return mRetrofit.create(clazz);
    }

}
