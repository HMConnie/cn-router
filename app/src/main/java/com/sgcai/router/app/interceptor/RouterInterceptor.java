package com.sgcai.router.app.interceptor;

import android.content.Context;
import android.util.Log;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;

/**
 * Created by hinge on 18/6/25.
 */
@Interceptor(priority = 8, name = "RouterInterceptor")
public class RouterInterceptor implements IInterceptor {

    @Override
    public void process(Postcard postcard, InterceptorCallback callback) {
        Log.e(RouterInterceptor.class.getSimpleName(), postcard.toString());
        callback.onContinue(postcard);
    }

    @Override
    public void init(Context context) {
        Log.e(RouterInterceptor.class.getSimpleName(), "========init=========");
    }
}
