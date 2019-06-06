/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. 
 *All Rights Reserved
 */
package com.caimao.bana.api.entity.req;

/**
 * @author yanjg 2015年5月13日
 */
public class F830207Req {
    public F830207Req() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(Long cashAmount) {
        this.cashAmount = cashAmount;
    }

    public Long getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(Long loanAmount) {
        this.loanAmount = loanAmount;
    }

    public Long getProdId() {
        return prodId;
    }

    public void setProdId(Long prodId) {
        this.prodId = prodId;
    }

    public Integer getProdTerm() {
        return prodTerm;
    }

    public void setProdTerm(Integer prodTerm) {
        this.prodTerm = prodTerm;
    }

    public String getUserTradePwd() {
        return userTradePwd;
    }

    public void setUserTradePwd(String userTradePwd) {
        this.userTradePwd = userTradePwd;
    }

    public String getOrderAbstract() {
        return orderAbstract;
    }

    public void setOrderAbstract(String orderAbstract) {
        this.orderAbstract = orderAbstract;
    }

    public Integer getPreDeposit() {
        return preDeposit;
    }

    public void setPreDeposit(Integer preDeposit) {
        this.preDeposit = preDeposit;
    }

    private Long userId;
    private Long cashAmount;
    private Long loanAmount;
    private Long prodId;
    private Integer prodTerm;
    private String userTradePwd;
    private String orderAbstract;
    private Integer preDeposit;
}
