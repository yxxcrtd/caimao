package com.caimao.bana.api.entity.res.other;

import java.util.Date;
/**
 * P2P活动返钱的东东
 * Created by WangXu on 2015/6/19.
 */
public class FOtherP2PReturnFeeRes {
    private Long userId;
    private String userRealName;
    private String mobile;
    private Long investValue;
    private Date investCreated;

    public Date getInvestCreated() {
        return investCreated;
    }

    public void setInvestCreated(Date investCreated) {
        this.investCreated = investCreated;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserRealName() {
        return userRealName;
    }

    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getInvestValue() {
        return investValue;
    }

    public void setInvestValue(Long investValue) {
        this.investValue = investValue;
    }
}
