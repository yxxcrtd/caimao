package com.caimao.bana.api.entity.zeus;

import java.io.Serializable;
import java.util.Date;

/**
 * HOMS系统流水
 */
public class BanaHomsFinanceHistoryEntity implements Serializable {
    private Long id;
    private Date transDate;
    private Long transNo;
    private String transBizType;
    private String stockCode;
    private String stockName;
    private String account;
    private Long accountUnitNo;
    private String accountUnitName;
    private Long transAmount;
    private Long postAmount;
    private String entrustDirection;
    private Long entrustPrice;
    private Long entrustAmount;
    private Long tradeFee;
    private Long stampDuty;
    private Long transferFee;
    private Long commission;
    private Long handlingFee;
    private Long secCharges;
    private String currency;
    private String transSubject;
    private Long subjectTransAmount;
    private Long subjectPostAmount;
    private String remark;
    private Long technicalServices;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTransDate() {
        return transDate;
    }

    public void setTransDate(Date transDate) {
        this.transDate = transDate;
    }

    public Long getTransNo() {
        return transNo;
    }

    public void setTransNo(Long transNo) {
        this.transNo = transNo;
    }

    public String getTransBizType() {
        return transBizType;
    }

    public void setTransBizType(String transBizType) {
        this.transBizType = transBizType;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Long getAccountUnitNo() {
        return accountUnitNo;
    }

    public void setAccountUnitNo(Long accountUnitNo) {
        this.accountUnitNo = accountUnitNo;
    }

    public String getAccountUnitName() {
        return accountUnitName;
    }

    public void setAccountUnitName(String accountUnitName) {
        this.accountUnitName = accountUnitName;
    }

    public Long getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(Long transAmount) {
        this.transAmount = transAmount;
    }

    public Long getPostAmount() {
        return postAmount;
    }

    public void setPostAmount(Long postAmount) {
        this.postAmount = postAmount;
    }

    public String getEntrustDirection() {
        return entrustDirection;
    }

    public void setEntrustDirection(String entrustDirection) {
        this.entrustDirection = entrustDirection;
    }

    public Long getEntrustPrice() {
        return entrustPrice;
    }

    public void setEntrustPrice(Long entrustPrice) {
        this.entrustPrice = entrustPrice;
    }

    public Long getEntrustAmount() {
        return entrustAmount;
    }

    public void setEntrustAmount(Long entrustAmount) {
        this.entrustAmount = entrustAmount;
    }

    public Long getTradeFee() {
        return tradeFee;
    }

    public void setTradeFee(Long tradeFee) {
        this.tradeFee = tradeFee;
    }

    public Long getStampDuty() {
        return stampDuty;
    }

    public void setStampDuty(Long stampDuty) {
        this.stampDuty = stampDuty;
    }

    public Long getTransferFee() {
        return transferFee;
    }

    public void setTransferFee(Long transferFee) {
        this.transferFee = transferFee;
    }

    public Long getCommission() {
        return commission;
    }

    public void setCommission(Long commission) {
        this.commission = commission;
    }

    public Long getHandlingFee() {
        return handlingFee;
    }

    public void setHandlingFee(Long handlingFee) {
        this.handlingFee = handlingFee;
    }

    public Long getSecCharges() {
        return secCharges;
    }

    public void setSecCharges(Long secCharges) {
        this.secCharges = secCharges;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTransSubject() {
        return transSubject;
    }

    public void setTransSubject(String transSubject) {
        this.transSubject = transSubject;
    }

    public Long getSubjectTransAmount() {
        return subjectTransAmount;
    }

    public void setSubjectTransAmount(Long subjectTransAmount) {
        this.subjectTransAmount = subjectTransAmount;
    }

    public Long getSubjectPostAmount() {
        return subjectPostAmount;
    }

    public void setSubjectPostAmount(Long subjectPostAmount) {
        this.subjectPostAmount = subjectPostAmount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getTechnicalServices() {
        return technicalServices;
    }

    public void setTechnicalServices(Long technicalServices) {
        this.technicalServices = technicalServices;
    }
}