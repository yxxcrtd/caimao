package com.caimao.bana.api.enums;

/**
 * Created by WangXu on 2015/4/23.
 */
public enum ECheckStatus {
    UNCHECK("00", "未对账"),

    CHECKING("01", "对账中"),

    CHECK_S("02", "对账通过"),

    CHECK_F("03", "对账不通过"),

    NO_CHECK("04", "无需对账");

    private String code;
    private String value;

    private ECheckStatus(String code, String value) { this.code = code;
        this.value = value;
    }

    public String getCode()
    {
        return this.code;
    }

    public String getValue() {
        return this.value;
    }

    public static String getCheckStatus(String code)
    {
        for (ECheckStatus e : values()) {
            if ((code != null) && (e.getCode().equals(code))) {
                return e.getValue();
            }
        }
        return null;
    }
}