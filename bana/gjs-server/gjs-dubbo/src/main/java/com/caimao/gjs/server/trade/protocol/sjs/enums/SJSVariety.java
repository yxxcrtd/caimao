package com.caimao.gjs.server.trade.protocol.sjs.enums;

/**
 * 证件类型
 */
public enum SJSVariety {
    VARIETY_201("201", "Au99.99"),
    VARIETY_202("202", "Au99.95"),
    VARIETY_203("203", "Pt99.95"),
    VARIETY_204("204", "Au50g"),
    VARIETY_206("206", "Ag99.9"),
    VARIETY_207("207", "Au100g");

    private String code;
    private String value;

    SJSVariety(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public static SJSVariety findByCode(String code) {
        for (SJSVariety sCode : SJSVariety.values()) {
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

