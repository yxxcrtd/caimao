/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. 
 *All Rights Reserved
 */
package com.caimao.bana.api.entity.res;

/**
 * @author yanjg 2015年6月5日
 */
public class F831209Res {
    private Long contractNo;
    private String prodName;
    private String userRealName;
    private String mobile;
    private Long orderAmount;
    private Long loanAmount;
    private Double interestRate;
    private String workDate;
    private String refUserRealName;
    private String refMobile;

    public Long getContractNo() {
        return this.contractNo;
    }

    public void setContractNo(Long contractNo) {
        this.contractNo = contractNo;
    }

    public String getProdName() {
        return this.prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getUserRealName() {
        return this.userRealName;
    }

    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getOrderAmount() {
        return this.orderAmount;
    }

    public void setOrderAmount(Long orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Long getLoanAmount() {
        return this.loanAmount;
    }

    public void setLoanAmount(Long loanAmount) {
        this.loanAmount = loanAmount;
    }

    public Double getInterestRate() {
        return this.interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    public String getWorkDate() {
        return this.workDate;
    }

    public void setWorkDate(String workDate) {
        this.workDate = workDate;
    }

    public String getRefUserRealName() {
        return this.refUserRealName;
    }

    public void setRefUserRealName(String refUserRealName) {
        this.refUserRealName = refUserRealName;
    }

    public String getRefMobile() {
        return this.refMobile;
    }

    public void setRefMobile(String refMobile) {
        this.refMobile = refMobile;
    }
}
