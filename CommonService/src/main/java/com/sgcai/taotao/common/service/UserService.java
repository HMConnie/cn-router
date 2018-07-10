package com.sgcai.taotao.common.service;

import com.alibaba.android.arouter.facade.template.IProvider;
import com.sgcai.taotao.common.to.UserTO;

/**
 * Created by hinge on 18/6/25.
 */

public interface UserService extends IProvider {
    UserTO getUserInfo();
}
