package com.caimao.bana.api.enums;

/**
 * 账户锁定状态原因
 * Created by WangXu on 2015/4/23.
 */
public enum EAccountStatusReason {
    AUTOBLOCK("1", "非法修改数据库被锁定"),
    MANUALUNBLOCK("2", "查错以后手动解锁");

    private final String code;
    private final String value;

    private EAccountStatusReason(String code, String value) { this.code = code;
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