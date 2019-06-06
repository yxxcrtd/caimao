/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司.
 *All Rights Reserved
 */
package com.caimao.bana.api.enums;

/**
 * 短信验证码验证状态
 * @author yanjg
 * 2015年4月28日
 */
public enum ESmsStatus {
    SEND("0", "待发送"),
    SEND_SUCCESS("1", "发送成功"),
    SEND_FAIL("9", "发送失败");

    private String code;
    private String value;

    private ESmsStatus(String code, String value) { this.code = code;
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
