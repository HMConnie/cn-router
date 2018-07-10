package com.sgcai.router.app.service;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sgcai.router.common.utils.EventBusTags;
import com.sgcai.router.common.utils.RouterHub;
import com.sgcai.taotao.common.service.AppService;

import org.simple.eventbus.EventBus;


/**
 * Created by hinge on 18/6/26.
 */

@Route(path = RouterHub.APP_COMMUNITION_SERVICE)
public class AppServiceImpl implements AppService {

    @Override
    public void loginCallback(String str) {
        EventBus.getDefault().post(str, EventBusTags.APP_CALLBACK_TAG);
    }

    @Override
    public void init(Context context) {

    }
}
