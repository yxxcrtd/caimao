/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. All Rights Reserved
 */
package com.caimao.bana.api.enums;

/**
 * @author Administrator $Id$
 * 
 */
public enum EZeusOperateLogType {
    INIT(0, "增加"),
    REPAYMENT(1, "修改"),
    END(2, "删除");

    private Integer code;
    private String value;

    private EZeusOperateLogType(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getValue() {
        return this.value;
    }
}
