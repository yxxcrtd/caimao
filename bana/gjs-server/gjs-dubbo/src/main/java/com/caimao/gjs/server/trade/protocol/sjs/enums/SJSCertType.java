package com.caimao.gjs.server.trade.protocol.sjs.enums;

/**
 * 证件类型
 */
public enum SJSCertType {
    CERT_TYPE_H("h", "护照"),
    CERT_TYPE_J("j", "军官证"),
    CERT_TYPE_S("s", "身份证"),
    CERT_TYPE_Y("y", "营业执照");

    private String code;
    private String value;

    SJSCertType(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public static SJSCertType findByCode(String code) {
        for (SJSCertType sCode : SJSCertType.values()) {
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

