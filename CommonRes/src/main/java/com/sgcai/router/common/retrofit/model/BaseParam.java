package com.sgcai.router.common.retrofit.model;

import android.text.TextUtils;

import com.sgcai.router.common.retrofit.RequestInterceptor;
import com.sgcai.router.common.utils.GlobalConstants;
import com.sgcai.router.common.utils.SignUtil;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by hinge on 17/4/7.
 */

public class BaseParam implements Serializable {

    public List<HeaderInfo> headerInfos = null; // 请求头信息

    public BaseParam() {
    }

    /**
     * 传入可以为空的属性值
     *
     * @return
     */
    public Map<String, String> getBodyParams() {
        Map<String, String> params = new HashMap<>();
        try {
            Field[] fields = this.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String name = field.getName();
                if (!TextUtils.isEmpty(name)) {
                    //过滤掉头信息属性
                    if (GlobalConstants.HEAD_INFOS.equals(name) || GlobalConstants.SERIALVERSION_UID.equals(name) || GlobalConstants.CHANGE.equals(name)) {
                        continue;
                    }
                    Object obj = field.get(this);
                    if (obj != null && !TextUtils.isEmpty(String.valueOf(obj))) {
                        String value = String.valueOf(obj).replace("+", "＋");
                        params.put(name, value);
                    }
                }


            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return params;
    }

    /**
     * .header("merchantNo", "" + merchantNo)
     * .header("timestamp", "" + timeStamp)
     * .header("sign", sign)
     */
    public RequestInterceptor getInterceptor() {
        final Map<String, String> signMap = new HashMap<>();//sign加密所需的参数
        final Map<String, String> headers = new HashMap<>();//http请求所需的请求头

        long timeStamp = System.currentTimeMillis();
        String channelId = "channelId";
        signMap.put("timestamp", timeStamp + ""); // 拼接参与sign时间戳
        signMap.put("channelId", channelId); // 拼接参与sign下载渠道ID

        headers.put("timestamp", timeStamp + ""); // 拼接时间戳头信息
        headers.put("channelId", channelId); // 分配给客户端的下载渠道ID
        headers.put("clientType", GlobalConstants.CLIENT_TYPE); // 拼接客户端类型
        headers.put("appId", GlobalConstants.APPID); // 拼接APP_ID
        headers.put("ver", "1.0"); // app的版本名

        if (headerInfos != null && headerInfos.size() > 0) {
            for (HeaderInfo headinfo : headerInfos) {
                if (headinfo.buildSign) {
                    signMap.put(headinfo.name, headinfo.value); //添加是否参与sign的头信息
                } else {
                    headers.put(headinfo.name, headinfo.value);
                }
            }
        }
        Map<String, String> buildParams = getBodyParams();
        if (buildParams != null) {
            signMap.putAll(buildParams);//添加参数体，参与生成sign
        }
        String sign = SignUtil.getSign(signMap);
        headers.put("sign", sign);//将sign拼接到请求头中

        /**创建请求拦截器**/
        return new RequestInterceptor(headers);
    }
}
