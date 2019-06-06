/*
*AddScoreType.java
*Created on 2015/5/25 15:53
*Copyright (c) 2015 北京火币天下网络技术有限公司. All Rights Reserved
*/
package com.caimao.zeus.admin.enums;

/**
 * 资金变动类型
 */
public enum TransType {

    ADMIN_CHANGE(1, "后台变更"), P2P_SYS_LOAN(2, "P2P系统出资");

    private int code;
    private String value;

    TransType(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public static TransType findByCode(int typeCode) {
        for (TransType transType : TransType.values()) {
            if (transType.getCode() == typeCode) {
                return transType;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
