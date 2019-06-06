package com.caimao.bana.api.enums.guji;

/**
 * 股票推荐类型
 * Created by WangXu on 2015/4/23.
 */
public enum EGujiShareType {
    JC(1, "建仓"),
    TC(2, "调仓"),
    DP(3, "大盘");

    private final Integer code;
    private final String value;

    EGujiShareType(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public static EGujiShareType findByCode(Integer code) {
        for (EGujiShareType e : EGujiShareType.values()) {
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
