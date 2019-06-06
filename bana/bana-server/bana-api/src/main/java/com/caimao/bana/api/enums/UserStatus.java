package com.caimao.bana.api.enums;

/**
 * Created by Administrator on 2014/11/19.
 */
public enum UserStatus {

    NORMAL((byte)0), LOCK((byte)1);

    private byte value;

    UserStatus(byte value) {
        this.value = value;
    }

    public static UserStatus findByValue(int tradeTypeValue) {
        for (UserStatus tradeType : UserStatus.values()) {
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
