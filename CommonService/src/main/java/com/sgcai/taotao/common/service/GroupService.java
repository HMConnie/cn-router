package com.sgcai.taotao.common.service;

import com.alibaba.android.arouter.facade.template.IProvider;
import com.sgcai.taotao.common.to.GroupTO;

/**
 * Created by hinge on 18/6/25.
 */

public interface GroupService extends IProvider {
    GroupTO getGroupInfo();

    String hello(String name);
}
