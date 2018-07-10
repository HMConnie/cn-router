package com.sgcai.router.common.retrofit.model;

/**
 * 请求头信息
 */

public class HeaderInfo {
    public String name;//请求头名称
    public String value;//请求头值
    public boolean buildSign; //是否参与生成sign

    public HeaderInfo(String name, String value, boolean buildSign) {
        this.name = name;
        this.value = value;
        this.buildSign = buildSign;
    }
}
