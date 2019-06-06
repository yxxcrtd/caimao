/*
*AddScoreType.java
*Created on 2015/5/25 15:53
*Copyright (c) 2015 北京火币天下网络技术有限公司. All Rights Reserved
*/
package com.caimao.bana.api.enums;

/**
 * 增加积分类型
 *
 * @author rpw
 * @version 1.0.1
 */
public enum AddScoreType {

    COMMISSION_RETURN(0), INVITE_RETURN(1);

    private int value;

    AddScoreType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
