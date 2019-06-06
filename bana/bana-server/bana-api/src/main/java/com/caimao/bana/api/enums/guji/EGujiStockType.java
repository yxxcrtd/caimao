package com.caimao.bana.api.enums.guji;

/**
 * 股票持仓类型
 * Created by WangXu on 2015/4/23.
 */
public enum EGujiStockType {
    XJ("XJ", "现金"),
    DP("DP", "大盘"),
    GP("GP", "股票");

    private final String code;
    private final String value;

    EGujiStockType(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public static EGujiStockType findByCode(String code) {
        for (EGujiStockType e : EGujiStockType.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

    public String getCode() {
        return this.code;
    }

    public String getValue() {
        return this.value;
    }
}
