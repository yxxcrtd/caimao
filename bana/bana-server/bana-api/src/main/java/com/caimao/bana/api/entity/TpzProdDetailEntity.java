package com.caimao.bana.api.entity;

import java.io.Serializable;
import java.util.Date;

public class TpzProdDetailEntity implements Serializable {
    private static final long serialVersionUID = -894671713484187868L;
    private Long id;
    private Long prodId;
    private Double loanRatioFrom;
    private Double loanRatioTo;
    private Long loanAmountFrom;
    private Long loanAmountTo;
    private Integer loanTermFrom;
    private Integer loanTermTo;
    private Long fee;
    private Double interestRate;
    private Double exposureRatio;
    private Double enableRatio;
    private Long stockPoolNo;
    private Double stockRatioLimit;
    private String remark;
    private Date createDatetime;
    private Date updateDatetime;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProdId() {
        return this.prodId;
    }

    public void setProdId(Long prodId) {
        this.prodId = prodId;
    }

    public Double getLoanRatioFrom() {
        return this.loanRatioFrom;
    }

    public void setLoanRatioFrom(Double loanRatioFrom) {
        this.loanRatioFrom = loanRatioFrom;
    }

    public Double getLoanRatioTo() {
        return this.loanRatioTo;
    }

    public void setLoanRatioTo(Double loanRatioTo) {
        this.loanRatioTo = loanRatioTo;
    }

    public Long getLoanAmountFrom() {
        return this.loanAmountFrom;
    }

    public void setLoanAmountFrom(Long loanAmountFrom) {
        this.loanAmountFrom = loanAmountFrom;
    }

    public Long getLoanAmountTo() {
        return this.loanAmountTo;
    }

    public void setLoanAmountTo(Long loanAmountTo) {
        this.loanAmountTo = loanAmountTo;
    }

    public Integer getLoanTermFrom() {
        return this.loanTermFrom;
    }

    public void setLoanTermFrom(Integer loanTermFrom) {
        this.loanTermFrom = loanTermFrom;
    }

    public Integer getLoanTermTo() {
        return this.loanTermTo;
    }

    public void setLoanTermTo(Integer loanTermTo) {
        this.loanTermTo = loanTermTo;
    }

    public Long getFee() {
        return this.fee;
    }

    public void setFee(Long fee) {
        this.fee = fee;
    }

    public Double getInterestRate() {
        return this.interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    public Double getExposureRatio() {
        return this.exposureRatio;
    }

    public void setExposureRatio(Double exposureRatio) {
        this.exposureRatio = exposureRatio;
    }

    public Double getEnableRatio() {
        return this.enableRatio;
    }

    public void setEnableRatio(Double enableRatio) {
        this.enableRatio = enableRatio;
    }

    public Long getStockPoolNo() {
        return this.stockPoolNo;
    }

    public void setStockPoolNo(Long stockPoolNo) {
        this.stockPoolNo = stockPoolNo;
    }

    public Double getStockRatioLimit() {
        return this.stockRatioLimit;
    }

    public void setStockRatioLimit(Double stockRatioLimit) {
        this.stockRatioLimit = stockRatioLimit;
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
}