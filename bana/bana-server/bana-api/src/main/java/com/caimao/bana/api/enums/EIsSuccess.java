/*
*EIsSuccess.java
*Created on 2015/5/13 10:18
*Copyright (c) 2015 北京火币天下网络技术有限公司. All Rights Reserved
*/
package com.caimao.bana.api.enums;

/**
 * @author Administrator
 * @version 1.0.1
 */
public enum EIsSuccess {
    NO("0", "否"), YES("1", "是");

    private String code;
    private String value;

    private EIsSuccess(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return this.code;
    }

    public String getValue() {
        return this.value;
    }

}
