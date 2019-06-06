package com.caimao.bana.api.enums.ybk;

/**
 * 注册用使用卡类型
 */
public enum ECardType {
    SFZ("1", "身份证"),
    JGZ("2", "军官证"),
    HZ("3", "护照"),
    TBZ("4", "台胞证");

    private final String code;
    private final String value;

    ECardType(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
