package com.sgcai.router.group.service;

import android.content.Context;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sgcai.router.common.utils.RouterHub;
import com.sgcai.router.group.R;
import com.sgcai.taotao.common.service.GroupService;
import com.sgcai.taotao.common.to.GroupTO;


/**
 * Created by hinge on 18/6/25.
 */
@Route(path = RouterHub.GROUP_COMMUNITION_SERVICE)
public class GroupServiceImpl implements GroupService {
    private Context mContext;

    @Override
    public void init(Context context) {
        this.mContext = context;
        Log.e(GroupServiceImpl.class.getSimpleName(), context.getClass().getName());
    }

    @Override
    public GroupTO getGroupInfo() {
        GroupTO groupTO = new GroupTO();
        String name = mContext.getResources().getString(R.string.group_app_name);
        groupTO.setName(name);
        return groupTO;
    }

    @Override
    public String hello(String name) {
        return "hello:" + name;
    }


}
