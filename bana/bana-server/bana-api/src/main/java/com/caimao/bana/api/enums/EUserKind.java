package com.caimao.bana.api.enums;

/**
 * 用户类型
 * Created by WangXu on 2015/5/18.
 */
public enum EUserKind {
    INVESTOR("01", "投资人"),
    FINANCIALPLANNER("10", "理财人"),
    INVESTORANDFINANCIALPLANNER("11", "投资人&理财人"),
    BORROWER("12", "借款人");

    private String code;
    private String value;

    private EUserKind(String code, String value) { this.code = code;
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
