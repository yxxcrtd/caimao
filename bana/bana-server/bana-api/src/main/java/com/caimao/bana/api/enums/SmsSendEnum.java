package com.caimao.bana.api.enums;

/**
 * 发送短信状态枚举类型
 */
public enum SmsSendEnum {

    INIT((byte)0), SUCCESS((byte)1),FAIL((byte)4);

    private byte value;

    SmsSendEnum(byte value) {
        this.value = value;
    }

    public static SmsSendEnum findByValue(int tradeTypeValue) {
        for (SmsSendEnum tradeType : SmsSendEnum.values()) {
            if (tradeType.getValue() == tradeTypeValue) {
                return tradeType;
            }
        }
        return null;
    }

    public byte getValue() {
        return value;
    }

    public void setValue(byte value) {
        this.value = value;
    }
}
