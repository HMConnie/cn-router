package com.sgcai.router.common.retrofit;


import android.text.TextUtils;


import com.sgcai.router.common.retrofit.model.NetWorkErrorResult;
import com.sgcai.router.common.utils.AppUtil;
import com.sgcai.router.common.utils.EMUtil;
import com.sgcai.router.common.utils.GsonUtil;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

import okhttp3.Request;


/**
 * 自定义错误信息，统一处理返回处理
 * Created by WZG on 2016/7/16.
 */
public class HttpTimeException extends RuntimeException {
    private static final String MESSAGE_NO_NET_ERROR = "网络断开，请检查网络";
    private static final String MESSAGE_CONNIECT_NET_ERROR = "网络不给力，稍后重试";
    private static final String MESSAGE_UNKWON_NET_ERROR = "服务器忙坏了，等会儿再试吧";
    private int mCode;
    private String mMessage;
    private String mReason;
    private Request original;

    public Request getOriginal() {
        return original;
    }

    /***
     * 解析网络超时异常
     */
    private static String parseError(Throwable throwable) {
        if (throwable != null) {
            if ((throwable instanceof ConnectException
                    || throwable instanceof SocketTimeoutException
                    || throwable instanceof TimeoutException
                    || throwable instanceof SocketException)) {
                return MESSAGE_CONNIECT_NET_ERROR;
            } else {
                EMUtil.reportUMError(throwable);
                return MESSAGE_UNKWON_NET_ERROR;
            }
        } else {
            return MESSAGE_UNKWON_NET_ERROR;
        }

    }

    public HttpTimeException(Throwable throwable) {
        this(throwable, 0);

    }

    public HttpTimeException(Throwable throwable, int code) {
        this(code, parseError(throwable), null);
    }


    public HttpTimeException(int code, String result, Request original) {
        this.mCode = code;
        this.original = original;
        if (!AppUtil.isConnected()) {
            mMessage = MESSAGE_NO_NET_ERROR;
        } else {
            if (original != null) {
                NetWorkErrorResult netWorkErrorResult = GsonUtil.fromJsontoBean(result, NetWorkErrorResult.class);
                mReason = netWorkErrorResult != null ? netWorkErrorResult.msg : MESSAGE_UNKWON_NET_ERROR;
                mMessage = netWorkErrorResult != null ? netWorkErrorResult.msgText : MESSAGE_UNKWON_NET_ERROR;
            } else {
                mMessage = TextUtils.isEmpty(result) ? MESSAGE_UNKWON_NET_ERROR : result;
            }
        }


    }


    public int getCode() {
        return mCode;
    }

    public String getMessage() {
        return mMessage;
    }

    public String getReason() {
        return mReason;
    }

}


