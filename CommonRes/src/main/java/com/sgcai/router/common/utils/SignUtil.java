package com.sgcai.router.common.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by hinge on 17/4/6.
 */

public class SignUtil {

    private SignUtil() {

    }

    /**
     * 获取到加密的sign
     */
    public static String getSign(Map<String, String> params) {
        String localSign = "";
        try {
            TreeMap<String, String> map = new TreeMap<>(params);
            Set<Map.Entry<String, String>> set = map.entrySet();
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> param : set) {
                sb.append(param.getKey()).append("=").append(param.getValue()).append("&");
            }
            sb.append("key").append("=").append("6543210123456789");
            localSign = md5Digest(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return localSign;

    }

    private static String md5Digest(String res) {
        if (res == null || "".equals(res)) {
            return null;
        }
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        byte[] strTemp;
        try {
            strTemp = res.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e1) {
            return null;
        }
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            String dd = new String(str);
            return dd;
        } catch (Exception e) {
            return null;
        }
    }

}
