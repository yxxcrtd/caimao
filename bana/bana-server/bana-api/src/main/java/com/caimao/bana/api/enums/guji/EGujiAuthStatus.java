package com.caimao.bana.api.enums.guji;

/**
 * 股计用户是否认证
 * Created by WangXu on 2015/4/23.
 */
public enum EGujiAuthStatus {
    NO(1, "未认证"),
    OK(2, "已认证"),
    AUDIT(3, "待审核");

    private final Integer code;
    private final String value;

    EGujiAuthStatus(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public static EGujiAuthStatus findByCode(Integer code) {
        for (EGujiAuthStatus e : EGujiAuthStatus.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getValue() {
        return this.value;
    }
}
