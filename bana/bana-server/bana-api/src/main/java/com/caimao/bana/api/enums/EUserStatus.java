package com.caimao.bana.api.enums;

/**
 * 用户状态
 * Created by WangXu on 2015/5/18.
 */
public enum EUserStatus {
    NORMAL("1", "正常"),
    BLOCK("0", "锁定");

    private String code;
    private String value;

    private EUserStatus(String code, String value) { this.code = code;
        this.value = value;
    }

    public String getCode()
    {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}