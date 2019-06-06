package com.caimao.bana.api.enums;

/**
 * 邮币卡交易所状态
 * Created by WangXu on 2015/4/23.
 */
public enum EYbkExchangeStatus {
    NOTSHOW(1, "不显示"),

    NORMAL(2, "正常"),

    DEL(3, "删除");

    private Integer code;
    private String value;

    private EYbkExchangeStatus(Integer code, String value) { this.code = code;
        this.value = value;
    }

    public Integer getCode()
    {
        return this.code;
    }

    public String getValue() {
        return this.value;
    }

    public static String getYbkExchangeStatus(Integer code) {
        for (EYbkExchangeStatus e : values()) {
            if ((code != null) && (e.getCode().equals(code))) {
                return e.getValue();
            }
        }
        return null;
    }
}