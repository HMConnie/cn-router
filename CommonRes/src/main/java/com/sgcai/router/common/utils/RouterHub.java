package com.sgcai.router.common.utils;

/**
 * ARouter路由路径管理类
 */

public interface RouterHub {

    String APP = "/app";//宿主app
    String GROUP = "/group";//group 模块
    String USER = "/user";//user 模块

    String APP_MAIN_ACTIVITY = APP + "/mainActivity";//MainActivity的路径
    String APP_SPLASH_ACTIVITY = APP + "/SplashActivity";//SplashActivity的路径
    String GROUP_GROUPLIST_ACTIVITY = GROUP + "/GroupListActivity";//GroupListActivity的路径
    String GROUP_GROUPDETAIL_ACTIVITY = GROUP + "/GroupDetailActivity";//GroupDetailActivity的路径
    String USER_LOGIN_ACTIVITY = USER + "/LoginActivity";//LoginActivity的路径


    String SERVICE = "/service";//service 服务解耦组件模块
    String APP_COMMUNITION_SERVICE = APP + SERVICE + "/app";//注意service的前路径不能相同， 否则编译报错 例如:app,group,user
    String GROUP_COMMUNITION_SERVICE = GROUP + SERVICE + "/group";
    String USER_COMMUNITION_SERVICE = USER + SERVICE + "/user";

}
