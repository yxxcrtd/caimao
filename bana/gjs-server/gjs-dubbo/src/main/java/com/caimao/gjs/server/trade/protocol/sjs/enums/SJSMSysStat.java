package com.caimao.gjs.server.trade.protocol.sjs.enums;

/**
 * 二级系统状态
 */
public enum SJSMSysStat {
    M_SYS_STAT_0("0", "初始化中"),
    M_SYS_STAT_1("1", "初始化完成"),
    M_SYS_STAT_2("2", "数据准备中"),
    M_SYS_STAT_3("3", "数据准备完成"),
    M_SYS_STAT_4("4", "清算处理中"),
    M_SYS_STAT_5("5", "清算处理完成"),
    M_SYS_STAT_6("6", "日终登帐中"),
    M_SYS_STAT_7("7", "日终登帐完成");

    private String code;
    private String value;

    SJSMSysStat(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public static SJSMSysStat findByCode(String code) {
        for (SJSMSysStat sCode : SJSMSysStat.values()) {
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

