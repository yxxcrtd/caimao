package com.caimao.bana.api.enums;

/**
 * Created by WangXu on 2015/4/28.
 */
public enum EVerifyStatus {
    WAIT_APPROVAL("0", "待审"),
    PASS("1", "审核通过"),
    ADJUSTEDPASS("2", "调整后审核通过"),
    NOTPASS("3", "审核不通过");

    private final String code;
    private final String value;

    private EVerifyStatus(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return this.code;
    }

    public String getValue() {
        return this.value;
    }
}