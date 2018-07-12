package com.sgcai.router.common.utils;

import android.app.Activity;

import com.sgcai.router.common.BuildConfig;
import com.sgcai.router.common.app.App;
import com.umeng.analytics.MobclickAgent;

import java.nio.charset.Charset;
import java.util.Set;

import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;

/**
 * Created by hinge on 17/11/10.
 */

public class EMUtil {
    private EMUtil() {

    }

    /**
     * 上报友盟错误列表
     */
    public static void reportUMError(int code, String result, Request original) {
        if (original == null || BuildConfig.DEBUG) return;
        try {
            StringBuilder stringBuilder = new StringBuilder();
            Activity currentActivity = App.getInstance().getNewsLifecycleHandler().getCurrentActivity();
            if (currentActivity != null) {
                stringBuilder.append("activity = " + currentActivity.getClass().getName() + '\n');
            }
            stringBuilder.append("code = " + code + '\n').append("result = " + result + '\n');
            stringBuilder.append("url = " + original.url() + '\n' + "method = " + original.method() + '\n');
            Headers headers = original.headers();
            if (headers != null && headers.size() > 0) {
                stringBuilder.append("heads = { " + '\n');
                Set<String> names = headers.names();
                for (String name : names) {
                    stringBuilder.append("\t").append(name).append("=").append(headers.get(name) + '\n');
                }
                stringBuilder.append(" } " + '\n');
            }
            RequestBody requestBody = original.body();
            if (requestBody != null) {
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);
                String body = buffer.readString(Charset.forName("UTF-8"));
                stringBuilder.append("body = { ").append(body).append(" }\n");
            }

            MobclickAgent.reportError(App.getInstance(), stringBuilder.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 上报友盟错误列表
     */
    public static void reportUMError(Throwable throwable) {
        if (BuildConfig.DEBUG) return;

        MobclickAgent.reportError(App.getInstance(), throwable);
    }


}
