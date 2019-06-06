package com.caimao.bana.api.enums;

/**
 * 免息券获取渠道类型
 */
public enum CouponChannelType{
    TYPE_DEPOSIT(1, "充值"), TYPE_WITH_FUNDING(2, "融资");

    private int value;
    private String name;

    CouponChannelType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static CouponChannelType findByValue(int typeValue) {
        for (CouponChannelType ChannelType : CouponChannelType.values()) {
            if (ChannelType.getValue() == typeValue) {
                return ChannelType;
            }
        }
        return null;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
