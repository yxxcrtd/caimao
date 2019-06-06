/*
*Account.java
*Created on 2015/5/8 14:36
*Copyright (c) 2015 北京火币天下网络技术有限公司. All Rights Reserved
*/
package com.caimao.zeus.admin.entity;

import java.io.Serializable;

/**
 * 权限实体类
 */
public class UserRule implements Serializable {
    private Long userId;
    private String rules;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }
}
