package com.caimao.bana.api.entity.res.loan;

import java.math.BigDecimal;

import com.caimao.bana.api.utils.DateUtil;


public class FLoanApplyRes  {
    private Long orderNo;
    private Long relContractNo;
    private String applyDatetime;
    private String loanApplyAction;
    private Long orderAmount;
    private Long prodId;
    private Integer loanRatio;
    private BigDecimal interestRate;
    private String contractBeginDate;
    private String contractEndDate;
    private String beginInterestDate;
    private Long cashAmount;
    private Long freezeAmount;
    private String verifyDatetime;
    private String verifyStatus;
    private String orderAbstract;
    private Long fee;
    private String prodBillType;
    private String interestAccrualMode;
    private String interestSettleMode;
    private Integer interestSettleDays;
    private Integer prodTerm;
    private String prodName;
    private String prodTypeId;

    public Long getOrderNo() {
        return this.orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public Long getRelContractNo() {
        return this.relContractNo;
    }

    public void setRelContractNo(Long relContractNo) {
        this.relContractNo = relContractNo;
    }

    public String getApplyDatetime() {
        return this.applyDatetime;
    }

    public void setApplyDatetime(String applyDatetime) {
        this.applyDatetime = DateUtil.trimMillis(applyDatetime);
    }

    public String getLoanApplyAction() {
        return this.loanApplyAction;
    }

    public void setLoanApplyAction(String loanApplyAction) {
        this.loanApplyAction = loanApplyAction;
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

    public String getContractBeginDate() {
        return this.contractBeginDate;
    }

    public void setContractBeginDate(String contractBeginDate) {
        this.contractBeginDate = DateUtil.convertDateNumToStr(contractBeginDate);
    }

    public String getContractEndDate() {
        return this.contractEndDate;
    }

    public void setContractEndDate(String contractEndDate) {
        this.contractEndDate = DateUtil.convertDateNumToStr(contractEndDate);
    }

    public String getBeginInterestDate() {
        return this.beginInterestDate;
    }

    public void setBeginInterestDate(String beginInterestDate) {
        this.beginInterestDate = DateUtil.convertDateNumToStr(beginInterestDate);
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

    public String getVerifyDatetime() {
        return this.verifyDatetime;
    }

    public void setVerifyDatetime(String verifyDatetime) {
        this.verifyDatetime = DateUtil.trimMillis(verifyDatetime);
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

    public String getProdName() {
        return this.prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getProdTypeId() {
        return this.prodTypeId;
    }

    public void setProdTypeId(String prodTypeId) {
        this.prodTypeId = prodTypeId;
    }
}
