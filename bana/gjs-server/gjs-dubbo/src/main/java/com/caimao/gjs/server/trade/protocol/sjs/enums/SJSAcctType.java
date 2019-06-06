package com.caimao.gjs.server.trade.protocol.sjs.enums;

/**
 * 客户类型
 */
public enum SJSAcctType {
    ACCT_TYPE_1("1", "自营账户"),
    ACCT_TYPE_2("2", "法人户!"),
    ACCT_TYPE_3("3", "个人户!");

    private String code;
    private String value;

    SJSAcctType(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public static SJSAcctType findByCode(String code) {
        for (SJSAcctType sCode : SJSAcctType.values()) {
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

