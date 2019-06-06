package com.caimao.bana.api.enums;

/**
 * 用户登陆成功与否的状态值
 * Created by WangXu on 2015/5/18.
 */
public enum EUserLoginIsSuccess {
    FAILURE("0", "登陆失败"),
    SUCCESS("1", "登陆成功");

    private String code;
    private String value;

    private EUserLoginIsSuccess(String code, String value) { this.code = code;
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
