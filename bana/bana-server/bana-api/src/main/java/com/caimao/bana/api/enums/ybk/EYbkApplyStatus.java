package com.caimao.bana.api.enums.ybk;

/**
 * 邮币卡申请状态枚举
 */
public enum EYbkApplyStatus {
    INIT(1, "待审核"),
    AUDIT(2, "审核中"),
    OK(3, "开通成功"),
    FAIL(4, "不予通过");

    private final Integer code;
    private final String value;

    EYbkApplyStatus(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public static EYbkApplyStatus findByCode(Integer code) {
        for (EYbkApplyStatus couponReceiveStatus : EYbkApplyStatus.values()) {
            if (couponReceiveStatus.getCode().equals(code)) {
                return couponReceiveStatus;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
