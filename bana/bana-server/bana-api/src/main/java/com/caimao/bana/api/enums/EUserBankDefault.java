package com.caimao.bana.api.enums;

/**
 * Created by WangXu on 2015/5/7.
 */
public enum EUserBankDefault {
    YES("1", "是主卡"),

    NO("0", "非主卡");

    private String code;
    private String value;

    private EUserBankDefault(String code, String value) { this.code = code;
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
