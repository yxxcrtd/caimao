package com.caimao.bana.api.enums;

/**
 * P2P投资的状态
 * Created by WangXu on 2015/4/23.
 */
public enum EP2PInvestStatus {
    INIT(0, "未满标"),
    FULL(1, "满标"),
    FAIL(2, "已流标"),
    INCOME(3, "收益中"),
    END(4, "已还款"),
    CANCEL(5, "取消");

    private Integer code;
    private String value;

    private EP2PInvestStatus(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getValue() {
        return this.value;
    }

}