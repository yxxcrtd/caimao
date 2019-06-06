package com.caimao.bana.api.enums;

/**
 * Created by WangXu on 2015/5/18.
 */
public enum EUserIsTrust {
    NO("0", "未实名认证"),
    YES("1", "已经实名认证");

    private String code;
    private String value;

    private EUserIsTrust(String code, String value) { this.code = code;
        this.value = value; }

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
