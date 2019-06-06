package com.caimao.bana.api.entity;

import java.io.Serializable;

public class TpzUserTradeEntity implements Serializable {
    private static final long serialVersionUID = 1470339707939049045L;
    private Long userId;

    private Integer errorCount;

    private String userTradePwd;

    private String userTradePwdStrength;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(Integer errorCount) {
        this.errorCount = errorCount;
    }

    public String getUserTradePwd() {
        return userTradePwd;
    }

    public void setUserTradePwd(String userTradePwd) {
        this.userTradePwd = userTradePwd;
    }

    public String getUserTradePwdStrength() {
        return userTradePwdStrength;
    }

    public void setUserTradePwdStrength(String userTradePwdStrength) {
        this.userTradePwdStrength = userTradePwdStrength;
    }
}