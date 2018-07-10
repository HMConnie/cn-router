package com.sgcai.taotao.user.service;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sgcai.router.common.utils.RouterHub;
import com.sgcai.taotao.common.service.UserService;
import com.sgcai.taotao.common.to.UserTO;


/**
 * Created by hinge on 18/6/25.
 */

@Route(path = RouterHub.USER_COMMUNITION_SERVICE)
public class UserServiceImpl implements UserService {

    @Override
    public UserTO getUserInfo() {
        UserTO userTO = new UserTO();
        userTO.setUserName("123");
        return userTO;
    }

    @Override
    public void init(Context context) {

    }
}
