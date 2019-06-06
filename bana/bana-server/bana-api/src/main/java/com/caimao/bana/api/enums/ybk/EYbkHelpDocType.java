package com.caimao.bana.api.enums.ybk;

/**
 * 邮币卡帮助文档类型
 */
public enum EYbkHelpDocType {
    MCJS(9, "名词解释"),
    OPEN(1, "开户问题"),
    TRADE(2, "交易问题"),
    APP(3, "客户端问题"),
    SHEN(4, "申购问题"),
    TUO(5, "托管问题"),
    QIAN(7, "银商签约教程"),
    OTHER(8, "其他问题");

    private final Integer code;
    private final String value;

    EYbkHelpDocType(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public static EYbkHelpDocType findByCode(Integer code) {
        for (EYbkHelpDocType couponReceiveStatus : EYbkHelpDocType.values()) {
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
