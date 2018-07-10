package com.sgcai.router.common.retrofit;

import android.net.ParseException;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Request;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * Created by hinge on 17/4/10.
 */

public abstract class NetWorkSubscriber<T> extends Subscriber<T> {


    /**
     * 处理网络请求错误
     *
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        Throwable throwable = e;
        //获取最根源的异常
        while (throwable.getCause() != null) {
            e = throwable;
            throwable = throwable.getCause();
        }

        if (e instanceof HttpException) {             //HTTP错误
            HttpException httpException = (HttpException) e;
            onError(new HttpTimeException(e, httpException.code()));
        } else {
            if (e instanceof HttpTimeException) {    //服务器返回的错误
                if (((HttpTimeException) e).getCode() == 401) {
                    Request request =  ((HttpTimeException) e).getOriginal();
                    Type genType = getClass().getGenericSuperclass();
                    Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
                    Class clazz = (Class) params[0];
                } else {
                    onError((HttpTimeException) e);
                }
            } else if (e instanceof JsonParseException
                    || e instanceof JSONException
                    || e instanceof ParseException) {// json解析错误
                onError(new HttpTimeException(e));
            } else {
                onError(new HttpTimeException(e));  //未知错误
            }
        }
    }


    /**
     * 错误回调
     */
    protected abstract void onError(HttpTimeException ex);


    @Override
    public void onCompleted() {

    }

}