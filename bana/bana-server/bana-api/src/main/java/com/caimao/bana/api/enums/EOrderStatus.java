package com.caimao.bana.api.enums;

/**
 * Created by WangXu on 2015/4/23.
 */
public enum EOrderStatus {
    ING("02", "待处理"),

    SUCCESS("03", "成功"),

    FAIL("04", "失败"),

    CACAL("05", "已取消"),

    WAIT_SURE("06", "待确认");

    private String code;
    private String value;

    private EOrderStatus(String code, String value) { this.code = code;
        this.value = value;
    }

    public String getCode()
    {
        return this.code;
    }

    public String getValue() {
        return this.value;
    }

    public static String getOrderStatus(String code)
    {
        for (EOrderStatus e : values()) {
            if ((code != null) && (e.getCode().equals(code))) {
                return e.getValue();
            }
        }
        return null;
    }
}