package com.sgcai.router.common.utils;

/**
 * Created by hinge on 18/6/25.
 */

public interface GlobalConstants {

    String BUNDLE_STR_KEY = "BUNDLE_STR_KEY";
    String BUNDLE_STR_NEW_KEY = "BUNDLE_STR_NEW_KEY";

    /*** BaseParam 过滤的属性名**/
    String SERIALVERSION_UID = "serialVersionUID";
    String CHANGE = "$change";

    String HEAD_INFOS = "headerInfos";
    String BASE_URL = "http://testapi.benbenzone.xyz";
    String CLIENT_TYPE = "CUSTOMER_ANDROID";
    String APPID = "59a2bcab4b6b11e7a4a5000b2f82eca7";
    int LAUNCHER_ADVERTISEMENT_DELAY = 1;
    int RC_INIT_PERMISSION = 100;

    /**
     * 友盟appkey和channel
     */
    String UMENG_APPKEY = "UMENG_APPKEY";
    String UMENG_CHANNEL = "UMENG_CHANNEL";

    interface SpName {
        String BENBEN_SP_FILE = "BENBEN_SP_FILE";
        String CHANNEL_ID = "channelId";
        String DB_VERSION = "DB_VERSION";
        String GUIDE_VERSION = "GUIDE_VERSION";
        String LOGIN_STATUS = "LOGIN_STATUS";
        String LOGIN_USER_EXPIREDIN = "LOGIN_USER_EXPIREDIN";
        String LOGIN_USER = "LOGIN_USER";
        String LOGIN_USER_INFO = "LOGIN_USER_INFO";
        String USER_GUIDE = "USER_GUIDE";
        String PUSH_STATE = "PUSH_STATE";
        String SQUARE_PUBLISH_CONTENT = "SQUARE_PUBLISH_CONTENT";
        String SQUARE_PUBLISH_ARTCILE = "SQUARE_PUBLISH_ARTCILE";
    }

    interface EventCode {
        int PERMISSION_INFO = 0x1003;
        int NETWORK_CHANGE_EVENT = 0x1005;
    }
}
