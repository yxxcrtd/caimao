package com.caimao.bana.api.entity.res.loan;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 普通借贷申请与借贷P2P申请合二为一的返回对象
 * Created by Administrator on 2015/7/24.
 */
public class FLoanP2PApplyRes implements Serializable {
    private Long userId;
    private Long orderNo;
    private Date applyDatetime;
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
    private String orderAbstract;
    private String verifyStatus;
    private Long fee;
    private String prodBillType;
    private Integer prodTerm;
    private String prodName;
    private String prodType;
    private String investNum;

    public String getInvestNum() {
        return investNum;
    }

    public void setInvestNum(String investNum) {
        this.investNum = investNum;
    }

    public String getProdType() {
        return prodType;
    }

    public void setProdType(String prodType) {
        this.prodType = prodType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public String getLoanApplyAction() {
        return loanApplyAction;
    }

    public void setLoanApplyAction(String loanApplyAction) {
        this.loanApplyAction = loanApplyAction;
    }

    public String getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(String verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    public Long getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Long orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Long getProdId() {
        return prodId;
    }

    public void setProdId(Long prodId) {
        this.prodId = prodId;
    }

    public Integer getLoanRatio() {
        return loanRatio;
    }

    public void setLoanRatio(Integer loanRatio) {
        this.loanRatio = loanRatio;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public Date getApplyDatetime() {
        return applyDatetime;
    }

    public void setApplyDatetime(Date applyDatetime) {
        this.applyDatetime = applyDatetime;
    }

    public String getContractBeginDate() {
        return contractBeginDate;
    }

    public void setContractBeginDate(String contractBeginDate) {
        this.contractBeginDate = contractBeginDate;
    }

    public String getContractEndDate() {
        return contractEndDate;
    }

    public void setContractEndDate(String contractEndDate) {
        this.contractEndDate = contractEndDate;
    }

    public String getBeginInterestDate() {
        return beginInterestDate;
    }

    public void setBeginInterestDate(String beginInterestDate) {
        this.beginInterestDate = beginInterestDate;
    }

    public Long getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(Long cashAmount) {
        this.cashAmount = cashAmount;
    }

    public Long getFreezeAmount() {
        return freezeAmount;
    }

    public void setFreezeAmount(Long freezeAmount) {
        this.freezeAmount = freezeAmount;
    }

    public String getOrderAbstract() {
        return orderAbstract;
    }

    public void setOrderAbstract(String orderAbstract) {
        this.orderAbstract = orderAbstract;
    }

    public Long getFee() {
        return fee;
    }

    public void setFee(Long fee) {
        this.fee = fee;
    }

    public String getProdBillType() {
        return prodBillType;
    }

    public void setProdBillType(String prodBillType) {
        this.prodBillType = prodBillType;
    }

    public Integer getProdTerm() {
        return prodTerm;
    }

    public void setProdTerm(Integer prodTerm) {
        this.prodTerm = prodTerm;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }
}
