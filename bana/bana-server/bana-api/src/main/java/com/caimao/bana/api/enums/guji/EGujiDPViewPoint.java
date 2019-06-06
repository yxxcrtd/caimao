package com.caimao.bana.api.enums.guji;

/**
 * 股计，对大盘的看法观点
 * Created by Administrator on 2016/1/11.
 */
public enum EGujiDPViewPoint {
    JDKD(1, "极度看多"),
    JSKD(2, "谨慎看多"),
    ZXKF(3, "中性看法"),
    JSKK(4, "谨慎看空"),
    JDKK(5, "极度看空");

    private final Integer code;
    private final String value;

    EGujiDPViewPoint(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public static EGujiDPViewPoint findByCode(Integer code) {
        for (EGujiDPViewPoint e : EGujiDPViewPoint.values()) {
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
