package com.caimao.gjs.api.enums;

/**
 * <p>
 *     贵金属交易所枚举类型
 * </p>
 */
public enum EGJSTradeType {
    /**B 现货买入*/
    NJS_BUY("B", "现货买入"),
    /**S 现货卖出*/
    NJS_SELL("S", "现货卖出"),
    /**4011 现货买入*/
    SJS_BUY("4011", "现货买入"),
    /**4012 现货卖出*/
    SJS_SELL("4012", "现货卖出"),
    /**4041 延期开多仓*/
    SJS_BUY_YQKDC("4041", "延期开多仓"),
    /**4042 延期开空仓*/
    SJS_SELL_YQKKC("4042", "延期开空仓"),
    /**4043 延期平多仓*/
    SJS_BUY_YQPDC("4043", "延期平多仓"),
    /**4044 延期平空仓*/
    SJS_SELL_YQPKC("4044", "延期平空仓");

    private String code;
    private String name;

    EGJSTradeType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static EGJSTradeType findByCode(String code) {
        for (EGJSTradeType sCode : EGJSTradeType.values()) {
            if (sCode.getCode().equals(code)) {
                return sCode;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}