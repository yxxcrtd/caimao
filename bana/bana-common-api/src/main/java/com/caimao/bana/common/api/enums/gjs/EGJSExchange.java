package com.caimao.bana.common.api.enums.gjs;

/**
 * <p>
 *     贵金属交易所枚举类型
 * </p>
 * Created by Administrator on 2015/10/10.
 */
public enum EGJSExchange {
    NJS("NJS", "南交所"),
    SJS("SJS", "上金所"),
    LIFFE("LIFFE", "伦敦交易所");

    private String code;
    private String name;

    EGJSExchange(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static EGJSExchange findByCode(String code) {
        for (EGJSExchange sCode : EGJSExchange.values()) {
            if (sCode.getCode().equals(code)) {
                return sCode;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
