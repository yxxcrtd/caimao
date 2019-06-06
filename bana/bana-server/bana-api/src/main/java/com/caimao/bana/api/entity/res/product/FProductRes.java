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
public class FProductRes implements Serializable{
    private static final long serialVersionUID = -840486799675629657L;
    private String prodName;
    private String prodNote;
    private String prodStatus;
    private String prodGrade;
    private String prodTypeId;
    private Integer prodLoanRatioMax;
    private Integer prodLoanRatioMin;
    private Long prodAmountMax;
    private Long prodAmountMin;
    private String prodTerms;
    private String prodBillAccords;
    private String prodBillType;
    private String prodDeferMode;
    private String interestAccrualMode;
    private String interestSettleMode;
    private Integer interestSettleDays;
    private String prodRepayMode;
    private Integer prodRepayCloseDays;
    private String prodRepayPenaltyFlag;
    private Integer prodRepayPenaltyDays;
    private String prodAddMode;
    private Integer prodAddNum;
    private String prodDeferedMode;
    private Integer prodDeferedNum;
    private Integer prodLoanLimit;
    private Integer prodLoanCurLimit;
    private Integer addToProd;
    private Integer deferedToProd;

    public String getProdName() {
        return this.prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getProdNote() {
        return this.prodNote;
    }

    public void setProdNote(String prodNote) {
        this.prodNote = prodNote;
    }

    public String getProdStatus() {
        return this.prodStatus;
    }

    public void setProdStatus(String prodStatus) {
        this.prodStatus = prodStatus;
    }

    public String getProdGrade() {
        return this.prodGrade;
    }

    public void setProdGrade(String prodGrade) {
        this.prodGrade = prodGrade;
    }

    public String getProdTypeId() {
        return this.prodTypeId;
    }

    public void setProdTypeId(String prodTypeId) {
        this.prodTypeId = prodTypeId;
    }

    public Integer getProdLoanRatioMax() {
        return this.prodLoanRatioMax;
    }

    public void setProdLoanRatioMax(Integer prodLoanRatioMax) {
        this.prodLoanRatioMax = prodLoanRatioMax;
    }

    public Integer getProdLoanRatioMin() {
        return this.prodLoanRatioMin;
    }

    public void setProdLoanRatioMin(Integer prodLoanRatioMin) {
        this.prodLoanRatioMin = prodLoanRatioMin;
    }

    public Long getProdAmountMax() {
        return this.prodAmountMax;
    }

    public void setProdAmountMax(Long prodAmountMax) {
        this.prodAmountMax = prodAmountMax;
    }

    public Long getProdAmountMin() {
        return this.prodAmountMin;
    }

    public void setProdAmountMin(Long prodAmountMin) {
        this.prodAmountMin = prodAmountMin;
    }

    public String getProdTerms() {
        return this.prodTerms;
    }

    public void setProdTerms(String prodTerms) {
        this.prodTerms = prodTerms;
    }

    public String getProdBillAccords() {
        return this.prodBillAccords;
    }

    public void setProdBillAccords(String prodBillAccords) {
        this.prodBillAccords = prodBillAccords;
    }

    public String getProdBillType() {
        return this.prodBillType;
    }

    public void setProdBillType(String prodBillType) {
        this.prodBillType = prodBillType;
    }

    public String getProdDeferMode() {
        return this.prodDeferMode;
    }

    public void setProdDeferMode(String prodDeferMode) {
        this.prodDeferMode = prodDeferMode;
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

    public String getProdRepayMode() {
        return this.prodRepayMode;
    }

    public void setProdRepayMode(String prodRepayMode) {
        this.prodRepayMode = prodRepayMode;
    }

    public Integer getProdRepayCloseDays() {
        return this.prodRepayCloseDays;
    }

    public void setProdRepayCloseDays(Integer prodRepayCloseDays) {
        this.prodRepayCloseDays = prodRepayCloseDays;
    }

    public String getProdRepayPenaltyFlag() {
        return this.prodRepayPenaltyFlag;
    }

    public void setProdRepayPenaltyFlag(String prodRepayPenaltyFlag) {
        this.prodRepayPenaltyFlag = prodRepayPenaltyFlag;
    }

    public Integer getProdRepayPenaltyDays() {
        return this.prodRepayPenaltyDays;
    }

    public void setProdRepayPenaltyDays(Integer prodRepayPenaltyDays) {
        this.prodRepayPenaltyDays = prodRepayPenaltyDays;
    }

    public String getProdAddMode() {
        return this.prodAddMode;
    }

    public void setProdAddMode(String prodAddMode) {
        this.prodAddMode = prodAddMode;
    }

    public Integer getProdAddNum() {
        return this.prodAddNum;
    }

    public void setProdAddNum(Integer prodAddNum) {
        this.prodAddNum = prodAddNum;
    }

    public String getProdDeferedMode() {
        return this.prodDeferedMode;
    }

    public void setProdDeferedMode(String prodDeferedMode) {
        this.prodDeferedMode = prodDeferedMode;
    }

    public Integer getProdDeferedNum() {
        return this.prodDeferedNum;
    }

    public void setProdDeferedNum(Integer prodDeferedNum) {
        this.prodDeferedNum = prodDeferedNum;
    }

    public Integer getProdLoanLimit() {
        return this.prodLoanLimit;
    }

    public void setProdLoanLimit(Integer prodLoanLimit) {
        this.prodLoanLimit = prodLoanLimit;
    }

    public Integer getProdLoanCurLimit() {
        return this.prodLoanCurLimit;
    }

    public void setProdLoanCurLimit(Integer prodLoanCurLimit) {
        this.prodLoanCurLimit = prodLoanCurLimit;
    }

    public Integer getAddToProd() {
        return this.addToProd;
    }

    public void setAddToProd(Integer addToProd) {
        this.addToProd = addToProd;
    }

    public Integer getDeferedToProd() {
        return this.deferedToProd;
    }

    public void setDeferedToProd(Integer deferedToProd) {
        this.deferedToProd = deferedToProd;
    }
}
