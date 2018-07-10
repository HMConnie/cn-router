package com.sgcai.taotao.user.network.api;


import com.sgcai.taotao.user.network.model.resp.UserResult;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Q on 2017/11/9.
 * 用户信息
 */

public interface UserServices {

    /**
     * 登录验证
     */
    @POST("/user/loginVerify")
    @FormUrlEncoded
    Observable<UserResult> login(@FieldMap Map<String, String> map);


}
