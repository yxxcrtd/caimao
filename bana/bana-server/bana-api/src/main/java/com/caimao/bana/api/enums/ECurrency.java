package com.caimao.bana.api.enums;

/**
 * Created by WangXu on 2015/4/23.
 */
public enum ECurrency {
    CNY("CNY", "人民币"), HKD("HKD", "港币"), USD("USD", "美元");

    private String code;
    private String value;

    private ECurrency(String code, String value) { this.code = code;
        this.value = value;
    }

    public String getCode()
    {
        return this.code;
    }

    public String getValue() {
        return this.value;
    }
}