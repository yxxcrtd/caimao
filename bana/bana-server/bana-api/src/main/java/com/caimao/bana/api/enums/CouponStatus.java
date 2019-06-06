package com.caimao.bana.api.enums;

/**
 * 免息券获取渠道类型
 */
public enum CouponStatus {
    STATUS_DEFAULT(0, "正常"), STATUS_EXHAUST(1, "领光"), STATUS_SHELVES(2, "下架");

    private int value;
    private String name;

    CouponStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static CouponStatus findByValue(Byte status) {
        for (CouponStatus couponStatus : CouponStatus.values()) {
            if (couponStatus.getValue() == status) {
                return couponStatus;
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
