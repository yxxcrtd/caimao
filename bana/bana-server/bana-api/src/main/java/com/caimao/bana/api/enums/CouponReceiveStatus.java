package com.caimao.bana.api.enums;

/**
 * 免息券获取渠道类型
 */
public enum CouponReceiveStatus {
    STATUS_DEFAULT(0, "未使用"), STATUS_USED(1, "已使用"), STATUS_EXPIRED(2, "过期"), STATUS_SHELVES(3, "下架");

    private int value;
    private String name;

    CouponReceiveStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static CouponReceiveStatus findByValue(Byte status) {
        for (CouponReceiveStatus couponReceiveStatus : CouponReceiveStatus.values()) {
            if (couponReceiveStatus.getValue() == status) {
                return couponReceiveStatus;
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
