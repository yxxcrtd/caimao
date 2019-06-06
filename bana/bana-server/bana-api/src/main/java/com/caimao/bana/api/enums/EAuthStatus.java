package com.caimao.bana.api.enums;

/**
 * 用户认证常量配置
 * Created by WangXu on 2015/4/22.
 */
public enum EAuthStatus {
    YES("1", "验证成功"),

    NO("2", "验证失败"),

    INIT("0", "初始状态"),

    WAIT_CHECK("3", "待审核");

    private String code;
    private String value;

    private EAuthStatus(String code, String value) { this.code = code;
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