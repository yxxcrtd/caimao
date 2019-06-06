package com.caimao.bana.api.enums;

/**
 * 邮币卡交易所交易日类型
 * Created by WangXu on 2015/4/23.
 */
public enum EYbkTradeDayType {
    NORMAL(1, "周一至周五(除节假日)"),

    WORK(2, "周一至周五"),

    ALLDAY(3, "全天"),

    TOSATURDAY(4, "周一至周六(除节假日)");

    private Integer code;
    private String value;

    private EYbkTradeDayType(Integer code, String value) { this.code = code;
        this.value = value;
    }

    public Integer getCode()
    {
        return this.code;
    }

    public String getValue() {
        return this.value;
    }

    public static String getYbkTradeDayType(Integer code) {
        for (EYbkTradeDayType e : values()) {
            if ((code != null) && (e.getCode().equals(code))) {
                return e.getValue();
            }
        }
        return null;
    }
}