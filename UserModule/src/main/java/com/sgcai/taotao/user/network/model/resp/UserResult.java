package com.sgcai.taotao.user.network.model.resp;

import java.io.Serializable;

/**
 * Created by Q on 2017/11/9.
 * 注册（保存用户信息）接口返回值
 */

public class UserResult implements Serializable {

    /**
     * data : {"userId":"ee33f0761b79420f8b412e0d79ec32aa","password":"25f9e794323b453885f5181f1b624d0b","accessToken":"794323b453885f5181f1b6","expiredIn":1510295073,"refreshToken":"794323b453885f5181f1b6"}
     */

    public DataBean data;


    public static class DataBean implements Serializable {
        /**
         * userId : ee33f0761b79420f8b412e0d79ec32aa
         * password : 25f9e794323b453885f5181f1b624d0b
         * accessToken : 794323b453885f5181f1b6
         * expiredIn : 1510295073
         * refreshToken : 794323b453885f5181f1b6
         */

        public String userId;
        public String password;
        public String accessToken;
        public int expiredIn;
        public String refreshToken;
        public String customerService;
        public String userType;//非团长用户，此属性为空

        @Override
        public String toString() {
            return "DataBean{" +
                    "userId='" + userId + '\'' +
                    ", password='" + password + '\'' +
                    ", accessToken='" + accessToken + '\'' +
                    ", expiredIn=" + expiredIn +
                    ", refreshToken='" + refreshToken + '\'' +
                    ", customerService='" + customerService + '\'' +
                    ", userType='" + userType + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "UserResult{" +
                "data=" + data +
                '}';
    }
}
