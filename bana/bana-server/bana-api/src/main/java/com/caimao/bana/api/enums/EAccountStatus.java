package com.caimao.bana.api.enums;

/**
 * 用户资产状态枚举
 * Created by WangXu on 2015/4/23.
 */
public enum EAccountStatus {
    MANUALBLOCK("0", "手动锁定"),
    NORMAL("1", "正常"),
    BLOCK("2", "程序自动锁定"),
    UNBLOCK("3", "手动解锁");

    private final String code;
    private final String value;

    private EAccountStatus(String code, String value) { this.code = code;
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