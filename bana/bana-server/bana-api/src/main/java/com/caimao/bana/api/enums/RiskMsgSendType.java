package com.caimao.bana.api.enums;

/**
 * 风险短信发送枚举
 */
public enum RiskMsgSendType {
    RISK(1, "风险线");

    private int code;
    private String name;

    RiskMsgSendType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static RiskMsgSendType findByCode(int codeValue) {
        for (RiskMsgSendType riskMsgSendType : RiskMsgSendType.values()) {
            if (riskMsgSendType.getCode() == codeValue) {
                return riskMsgSendType;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
