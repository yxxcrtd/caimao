package com.caimao.bana.api.enums;

/**
 * 用户注册来源
 * Created by WangXu on 2015/5/18.
 */
public enum EUserInit {
    WEB("1", "网上注册"),
    MOBILE("2", "手机端注册"),
    API("3", "API注册"),
    YBK("4", "邮币卡注册"),
    GJS("5", "贵金属注册"),
    OSS("9", "后台运营"),
    HUOBI("11", "火币");

    private String code;
    private String value;

    private EUserInit(String code, String value) { this.code = code;
        this.value = value;
    }

    public String getCode()
    {
        return this.code;
    }

    public String getValue() {
        return this.value;
    }
}
