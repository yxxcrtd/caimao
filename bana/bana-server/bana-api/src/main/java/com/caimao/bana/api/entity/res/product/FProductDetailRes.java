/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. All Rights Reserved
 */
package com.caimao.bana.api.entity.res.product;

import java.io.Serializable;

/**
 * @author zxd $Id$
 * 
 */
public class FProductDetailRes implements Serializable{
    private static final long serialVersionUID = 6526898445744493271L;
    private Long id;
    private Long prodId;
    private double loanRatioFrom;
    private double loanRatioTo;
    private Long loanAmountFrom;
    private Long loanAmountTo;
    private Integer loanTermFrom;
    private Integer loanTermTo;
    private Long fee;
    private double interestRate;
    private double exposureRatio;
    private double enableRatio;
    private Long stockPoolNo;
    private double stockRatioLimit;
    private String remark;

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getLoanRatioFrom() {
        return this.loanRatioFrom;
    }

    public void setLoanRatioFrom(double loanRatioFrom) {
        this.loanRatioFrom = loanRatioFrom;
    }

    public double getLoanRatioTo() {
        return this.loanRatioTo;
    }

    public Long getProdId() {
        return this.prodId;
    }

    public void setProdId(Long prodId) {
        this.prodId = prodId;
    }

    public void setLoanRatioTo(double loanRatioTo) {
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

    public double getInterestRate() {
        return this.interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public double getExposureRatio() {
        return this.exposureRatio;
    }

    public void setExposureRatio(double exposureRatio) {
        this.exposureRatio = exposureRatio;
    }

    public double getEnableRatio() {
        return this.enableRatio;
    }

    public void setEnableRatio(double enableRatio) {
        this.enableRatio = enableRatio;
    }

    public Long getStockPoolNo() {
        return this.stockPoolNo;
    }

    public void setStockPoolNo(Long stockPoolNo) {
        this.stockPoolNo = stockPoolNo;
    }

    public double getStockRatioLimit() {
        return this.stockRatioLimit;
    }

    public void setStockRatioLimit(double stockRatioLimit) {
        this.stockRatioLimit = stockRatioLimit;
    }

}
