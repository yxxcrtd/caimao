package com.caimao.bana.api.enums;

/**
 * 提现绑定银行卡状态
 * Created by WangXu on 2015/5/7.
 */
public enum EBankCardStatus {
    INIT("0", "初始状态(待验)"),

    YES("1", "验证通过"),

    NO("2", "验证不通过"),

    REPLACED("3", "由主卡变副卡");

    private String code;
    private String value;

    private EBankCardStatus(String code, String value) { this.code = code;
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