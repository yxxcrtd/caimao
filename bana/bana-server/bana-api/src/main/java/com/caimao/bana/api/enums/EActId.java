package com.caimao.bana.api.enums;

/**
 * 活动ID
 * Created by Administrator on 2015/7/31.
 */
public enum  EActId {
    EWM100(1, "二维码送100元");

    private final Integer code;
    private final String value;

    private EActId(Integer code, String value) { this.code = code;
        this.value = value;
    }

    public Integer getCode()
    {
        return this.code;
    }

    public String getValue() {
        return this.value;
    }
}
