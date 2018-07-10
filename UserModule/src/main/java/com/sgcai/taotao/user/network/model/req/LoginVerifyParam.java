package com.sgcai.taotao.user.network.model.req;


import com.sgcai.router.common.retrofit.model.BaseParam;

/**
 * Created by Q on 2017/11/9.
 * 登录验证接口参数
 * mobile 手机号
 * password 密码
 */

public class LoginVerifyParam extends BaseParam {
    public String mobile;
    public String password;

    public LoginVerifyParam(String mobile, String password){
        this.mobile = mobile;
        this.password = password;
    }
}
