/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司.
 *All Rights Reserved
 */
package com.caimao.bana.api.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author yanjg
 *         2015年5月13日
 */
public class TpzInterestBillEntity {
    private static final long serialVersionUID = -6814296979035607094L;
    private Long orderNo;
    private Long userId;
    private Long contractNo;
    private Long orderAmount;
    private String billAbstract;
    private Long loanAmount;
    private BigDecimal interestRate;
    private String interestSettleFlag;
    private String workDate;
    private Date createDatetime;

    public Long getOrderNo() {
        return this.orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getContractNo() {
        return this.contractNo;
    }

    public void setContractNo(Long contractNo) {
        this.contractNo = contractNo;
    }

    public Long getOrderAmount() {
        return this.orderAmount;
    }

    public void setOrderAmount(Long orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getBillAbstract() {
        return this.billAbstract;
    }

    public void setBillAbstract(String billAbstract) {
        this.billAbstract = billAbstract;
    }

    public Long getLoanAmount() {
        return this.loanAmount;
    }

    public void setLoanAmount(Long loanAmount) {
        this.loanAmount = loanAmount;
    }

    public BigDecimal getInterestRate() {
        return this.interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public String getInterestSettleFlag() {
        return this.interestSettleFlag;
    }

    public void setInterestSettleFlag(String interestSettleFlag) {
        this.interestSettleFlag = interestSettleFlag;
    }

    public String getWorkDate() {
        return this.workDate;
    }

    public void setWorkDate(String workDate) {
        this.workDate = workDate;
    }

    public Date getCreateDatetime() {
        return this.createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }
}
