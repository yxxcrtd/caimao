package com.caimao.bana.api.enums;

/**
 * 用户邀请的绑定状态
 */
public enum UserInviteStatus {

    INIT((byte)0), BIND((byte)1),CANCEL((byte)2);

    private byte value;

    UserInviteStatus(byte value) {
        this.value = value;
    }

    public static UserInviteStatus findByValue(int tradeTypeValue) {
        for (UserInviteStatus tradeType : UserInviteStatus.values()) {
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
