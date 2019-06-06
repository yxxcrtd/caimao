package com.caimao.gjs.server.trade.protocol.sjs.enums;

/**
 * 交易所状态
 */
public enum SJSBSysStat {
    B_SYS_STAT_0("0", "初始化中"),
    B_SYS_STAT_1("1", "初始化完成"),
    B_SYS_STAT_2("2", "开市"),
    B_SYS_STAT_3("3", "收市"),
    B_SYS_STAT_4("4", "正在结算"),
    B_SYS_STAT_5("5", "结算完成"),
    B_SYS_STAT_6("6", "T+0财务处理完成"),
    B_SYS_STAT_7("7", "日终登帐完成");

    private String code;
    private String value;

    SJSBSysStat(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public static SJSBSysStat findByCode(String code) {
        for (SJSBSysStat sCode : SJSBSysStat.values()) {
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

