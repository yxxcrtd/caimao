/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. 
 *All Rights Reserved
 */
package com.caimao.bana.api.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author yanjg 2015年5月13日
 */
public class TpzLoanApplyEntity implements Serializable {
    private static final long serialVersionUID = -8584127078571262309L;
    private Long orderNo;
    private Long userId;
    private String userRealName;
    private Long pzAccountId;
    private String loanApplyAction;
    private Date applyDatetime;
    private String contractBeginDate;
    private String contractEndDate;
    private String beginInterestDate;
    private Integer loanRatio;
    private BigDecimal interestRate;
    private Long cashAmount;
    private Long freezeAmount;
    private Long orderAmount;
    private Long prodId;
    private Long relContractNo;
    private String verifyUser;
    private Date verifyDatetime;
    private String verifyStatus;
    private String orderAbstract;
    private String remark;
    private Long fee;
    private String prodBillType;
    private String interestAccrualMode;
    private String interestSettleMode;
    private Integer interestSettleDays;
    private Integer prodTerm;
    private Short loanType;
    private Short applyType;
    private Long p2pContractNo;

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

    public String getUserRealName() {
        return this.userRealName;
    }

    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }

    public Long getPzAccountId() {
        return this.pzAccountId;
    }

    public void setPzAccountId(Long pzAccountId) {
        this.pzAccountId = pzAccountId;
    }

    public String getLoanApplyAction() {
        return this.loanApplyAction;
    }

    public void setLoanApplyAction(String loanApplyAction) {
        this.loanApplyAction = loanApplyAction;
    }

    public Date getApplyDatetime() {
        return this.applyDatetime;
    }

    public void setApplyDatetime(Date applyDatetime) {
        this.applyDatetime = applyDatetime;
    }

    public String getContractBeginDate() {
        return this.contractBeginDate;
    }

    public void setContractBeginDate(String contractBeginDate) {
        this.contractBeginDate = contractBeginDate;
    }

    public String getContractEndDate() {
        return this.contractEndDate;
    }

    public void setContractEndDate(String contractEndDate) {
        this.contractEndDate = contractEndDate;
    }

    public String getBeginInterestDate() {
        return this.beginInterestDate;
    }

    public void setBeginInterestDate(String beginInterestDate) {
        this.beginInterestDate = beginInterestDate;
    }

    public Integer getLoanRatio() {
        return this.loanRatio;
    }

    public void setLoanRatio(Integer loanRatio) {
        this.loanRatio = loanRatio;
    }

    public BigDecimal getInterestRate() {
        return this.interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public Long getCashAmount() {
        return this.cashAmount;
    }

    public void setCashAmount(Long cashAmount) {
        this.cashAmount = cashAmount;
    }

    public Long getFreezeAmount() {
        return this.freezeAmount;
    }

    public void setFreezeAmount(Long freezeAmount) {
        this.freezeAmount = freezeAmount;
    }

    public Long getOrderAmount() {
        return this.orderAmount;
    }

    public void setOrderAmount(Long orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Long getProdId() {
        return this.prodId;
    }

    public void setProdId(Long prodId) {
        this.prodId = prodId;
    }

    public Long getRelContractNo() {
        return this.relContractNo;
    }

    public void setRelContractNo(Long relContractNo) {
        this.relContractNo = relContractNo;
    }

    public String getVerifyUser() {
        return this.verifyUser;
    }

    public void setVerifyUser(String verifyUser) {
        this.verifyUser = verifyUser;
    }

    public Date getVerifyDatetime() {
        return this.verifyDatetime;
    }

    public void setVerifyDatetime(Date verifyDatetime) {
        this.verifyDatetime = verifyDatetime;
    }

    public String getVerifyStatus() {
        return this.verifyStatus;
    }

    public void setVerifyStatus(String verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    public String getOrderAbstract() {
        return this.orderAbstract;
    }

    public void setOrderAbstract(String orderAbstract) {
        this.orderAbstract = orderAbstract;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getFee() {
        return this.fee;
    }

    public void setFee(Long fee) {
        this.fee = fee;
    }

    public String getProdBillType() {
        return this.prodBillType;
    }

    public void setProdBillType(String prodBillType) {
        this.prodBillType = prodBillType;
    }

    public String getInterestAccrualMode() {
        return this.interestAccrualMode;
    }

    public void setInterestAccrualMode(String interestAccrualMode) {
        this.interestAccrualMode = interestAccrualMode;
    }

    public String getInterestSettleMode() {
        return this.interestSettleMode;
    }

    public void setInterestSettleMode(String interestSettleMode) {
        this.interestSettleMode = interestSettleMode;
    }

    public Integer getInterestSettleDays() {
        return this.interestSettleDays;
    }

    public void setInterestSettleDays(Integer interestSettleDays) {
        this.interestSettleDays = interestSettleDays;
    }

    public Integer getProdTerm() {
        return this.prodTerm;
    }

    public void setProdTerm(Integer prodTerm) {
        this.prodTerm = prodTerm;
    }

    public Short getLoanType() {
        return loanType;
    }

    public void setLoanType(Short loanType) {
        this.loanType = loanType;
    }

    public Short getApplyType() {
        return applyType;
    }

    public void setApplyType(Short applyType) {
        this.applyType = applyType;
    }

    public Long getP2pContractNo() {
        return p2pContractNo;
    }

    public void setP2pContractNo(Long p2pContractNo) {
        this.p2pContractNo = p2pContractNo;
    }
}
