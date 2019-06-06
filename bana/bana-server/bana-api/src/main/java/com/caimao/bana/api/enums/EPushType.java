package com.caimao.bana.api.enums;

public enum EPushType {
    USER("0", "用户"),
    ACCOUNT("1", "账户"),
    PAY("2", "支付"),
    HOMS("3", "HOMS"),
    LOAN("4", "融资"),
    P2P("5", "P2P"),
    RISK("9", "风控"),
    NOTICE("10", "公告"),
    TRADE("11", "交易"),
    PRICEALERT("12", "价格提醒"),
    TRADENOTICE("13", "成交提醒"),
    RISKNOTICE("14", "持仓预警"),
    OPENACCOUNT("15", "开户成功"),
    FORCEPC("16", "强制平仓"),
    TRANSFERMONEY("17", "转账提醒");

    private String code;
    private String value;

    private EPushType(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public static EPushType findByCode(String code) {
        for (EPushType ePushType : EPushType.values()) {
            if (ePushType.getCode().equals(code)) {
                return ePushType;
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