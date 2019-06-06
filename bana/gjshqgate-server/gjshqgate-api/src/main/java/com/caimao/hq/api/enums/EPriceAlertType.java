package com.caimao.hq.api.enums;

/**
 * 贵金属价格提醒类型枚举
 */
public enum EPriceAlertType {
    DY("1", "大于价格"),
    XY("2", "小于价格"),
    ZF("3", "涨跌幅");

    private String value;
    private String name;

    EPriceAlertType(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public static EPriceAlertType findByValue(String status) {
        for (EPriceAlertType priceAlertType : EPriceAlertType.values()) {
            if (priceAlertType.getValue().equals(status)) {
                return priceAlertType;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
