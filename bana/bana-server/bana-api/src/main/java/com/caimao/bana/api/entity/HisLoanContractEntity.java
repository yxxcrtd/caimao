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
public class HisLoanContractEntity implements Serializable {
    private static final long serialVersionUID = -1025844857331832968L;
    private Long contractNo;
    private Long pzAccountId;
    private String contractType;
    private Long userId;
    private String userRealName;
    private String contractBeginDate;
    private String contractEndDate;
    private String beginInterestDate;
    private Long cashAmount;
    private Integer loanRatio;
    private BigDecimal interestRate;
    private String interestSettleMode;
    private String interestAccrualMode;
    private Long loanAmount;
    private Long repayAmount;
    private Date contractSignDatetime;
    private Date contractStopDatetime;
    private String contractStatus;
    private Long settledInterest;
    private Long accruedInterest;
    private String lastSettleInterestDate;
    private String nextSettleInterestDate;
    private String counterpartIdcard;
    private String counterpartName;
    private String counterpartFundAccount;
    private Long counterpartUserId;
    private Long relContractNo;
    private Long prodId;
    private String idcard;
    private String remark;
    private Date createDatetime;
    private Date updateDatetime;
    private Long fee;
    private String prodBillType;
    private Integer interestSettleDays;
    private Integer prodTerm;

    public Long getContractNo() {
        return this.contractNo;
    }

    public void setContractNo(Long contractNo) {
        this.contractNo = contractNo;
    }

    public Long getPzAccountId() {
        return this.pzAccountId;
    }

    public void setPzAccountId(Long pzAccountId) {
        this.pzAccountId = pzAccountId;
    }

    public String getContractType() {
        return this.contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
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

    public Long getCashAmount() {
        return this.cashAmount;
    }

    public void setCashAmount(Long cashAmount) {
        this.cashAmount = cashAmount;
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

    public String getInterestSettleMode() {
        return this.interestSettleMode;
    }

    public void setInterestSettleMode(String interestSettleMode) {
        this.interestSettleMode = interestSettleMode;
    }

    public String getInterestAccrualMode() {
        return this.interestAccrualMode;
    }

    public void setInterestAccrualMode(String interestAccrualMode) {
        this.interestAccrualMode = interestAccrualMode;
    }

    public Long getLoanAmount() {
        return this.loanAmount;
    }

    public void setLoanAmount(Long loanAmount) {
        this.loanAmount = loanAmount;
    }

    public Long getRepayAmount() {
        return this.repayAmount;
    }

    public void setRepayAmount(Long repayAmount) {
        this.repayAmount = repayAmount;
    }

    public Date getContractSignDatetime() {
        return this.contractSignDatetime;
    }

    public void setContractSignDatetime(Date contractSignDatetime) {
        this.contractSignDatetime = contractSignDatetime;
    }

    public Date getContractStopDatetime() {
        return this.contractStopDatetime;
    }

    public void setContractStopDatetime(Date contractStopDatetime) {
        this.contractStopDatetime = contractStopDatetime;
    }

    public String getContractStatus() {
        return this.contractStatus;
    }

    public void setContractStatus(String contractStatus) {
        this.contractStatus = contractStatus;
    }

    public Long getSettledInterest() {
        return this.settledInterest;
    }

    public void setSettledInterest(Long settledInterest) {
        this.settledInterest = settledInterest;
    }

    public Long getAccruedInterest() {
        return this.accruedInterest;
    }

    public void setAccruedInterest(Long accruedInterest) {
        this.accruedInterest = accruedInterest;
    }

    public String getLastSettleInterestDate() {
        return this.lastSettleInterestDate;
    }

    public void setLastSettleInterestDate(String lastSettleInterestDate) {
        this.lastSettleInterestDate = lastSettleInterestDate;
    }

    public String getNextSettleInterestDate() {
        return this.nextSettleInterestDate;
    }

    public void setNextSettleInterestDate(String nextSettleInterestDate) {
        this.nextSettleInterestDate = nextSettleInterestDate;
    }

    public String getCounterpartIdcard() {
        return this.counterpartIdcard;
    }

    public void setCounterpartIdcard(String counterpartIdcard) {
        this.counterpartIdcard = counterpartIdcard;
    }

    public String getCounterpartName() {
        return this.counterpartName;
    }

    public void setCounterpartName(String counterpartName) {
        this.counterpartName = counterpartName;
    }

    public String getCounterpartFundAccount() {
        return this.counterpartFundAccount;
    }

    public void setCounterpartFundAccount(String counterpartFundAccount) {
        this.counterpartFundAccount = counterpartFundAccount;
    }

    public Long getCounterpartUserId() {
        return this.counterpartUserId;
    }

    public void setCounterpartUserId(Long counterpartUserId) {
        this.counterpartUserId = counterpartUserId;
    }

    public Long getRelContractNo() {
        return this.relContractNo;
    }

    public void setRelContractNo(Long relContractNo) {
        this.relContractNo = relContractNo;
    }

    public Long getProdId() {
        return this.prodId;
    }

    public void setProdId(Long prodId) {
        this.prodId = prodId;
    }

    public String getIdcard() {
        return this.idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateDatetime() {
        return this.createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public Date getUpdateDatetime() {
        return this.updateDatetime;
    }

    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    public Integer getProdTerm() {
        return this.prodTerm;
    }

    public void setProdTerm(Integer prodTerm) {
        this.prodTerm = prodTerm;
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

    public Integer getInterestSettleDays() {
        return this.interestSettleDays;
    }

    public void setInterestSettleDays(Integer interestSettleDays) {
        this.interestSettleDays = interestSettleDays;
    }
}
