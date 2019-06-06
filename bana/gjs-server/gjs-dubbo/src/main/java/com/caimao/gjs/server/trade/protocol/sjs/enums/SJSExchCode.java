package com.caimao.gjs.server.trade.protocol.sjs.enums;

/**
 * 交易类型
 */
public enum SJSExchCode {
    EXCH_CODE_4011("4011", "现货买入"),
    EXCH_CODE_4012("4012", "现货卖出"),
    EXCH_CODE_4021("4021", "远期开多仓"),
    EXCH_CODE_4022("4022", "远期开空仓"),
    EXCH_CODE_4041("4041", "延期开多仓"),
    EXCH_CODE_4042("4042", "延期开空仓"),
    EXCH_CODE_4043("4043", "延期平多仓"),
    EXCH_CODE_4044("4044", "延期开空仓"),
    EXCH_CODE_4045("4045", "延期收金"),
    EXCH_CODE_4046("4046", "延期交金"),
    EXCH_CODE_4047("4047", "延期中立仓收金"),
    EXCH_CODE_4048("4048", "延期中立仓交金");

    private String code;
    private String value;

    SJSExchCode(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public static SJSExchCode findByCode(String code) {
        for (SJSExchCode sCode : SJSExchCode.values()) {
            if (sCode.getCode().equals(code)) {
                return sCode;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}

