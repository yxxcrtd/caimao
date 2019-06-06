package com.caimao.gjs.api.entity;

import java.io.Serializable;

public class GJSNJSTraderIdEntity implements Serializable {
    private String firmId; //编组编号
    private String traderId; //交易员编号
    private Long userId; //用户编号
    private Integer accountType; //账号类型

    public String getFirmId() {
        return firmId;
    }

    public void setFirmId(String firmId) {
        this.firmId = firmId;
    }

    public String getTraderId() {
        return traderId;
    }

    public void setTraderId(String traderId) {
        this.traderId = traderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }
}