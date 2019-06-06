/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. 
 *All Rights Reserved
 */
package com.caimao.bana.api.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yanjg 2015年5月13日
 */
public class TpzProdEntity implements Serializable {
    private static final long serialVersionUID = 8555982854598275857L;

    public TpzProdEntity() {
    }

    public Integer getAddToProd() {
        return addToProd;
    }

    public void setAddToProd(Integer addToProd) {
        this.addToProd = addToProd;
    }

    public Integer getDeferedToProd() {
        return deferedToProd;
    }

    public void setDeferedToProd(Integer deferedToProd) {
        this.deferedToProd = deferedToProd;
    }

    public Long getProdId() {
        return prodId;
    }

    public void setProdId(Long prodId) {
        this.prodId = prodId;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getProdNote() {
        return prodNote;
    }

    public void setProdNote(String prodNote) {
        this.prodNote = prodNote;
    }

    public String getProdStatus() {
        return prodStatus;
    }

    public void setProdStatus(String prodStatus) {
        this.prodStatus = prodStatus;
    }

    public String getProdGrade() {
        return prodGrade;
    }

    public void setProdGrade(String prodGrade) {
        this.prodGrade = prodGrade;
    }

    public String getProdTypeId() {
        return prodTypeId;
    }

    public void setProdTypeId(String prodTypeId) {
        this.prodTypeId = prodTypeId;
    }

    public Integer getProdLoanRatioMax() {
        return prodLoanRatioMax;
    }

    public void setProdLoanRatioMax(Integer prodLoanRatioMax) {
        this.prodLoanRatioMax = prodLoanRatioMax;
    }

    public Integer getProdLoanRatioMin() {
        return prodLoanRatioMin;
    }

    public void setProdLoanRatioMin(Integer prodLoanRatioMin) {
        this.prodLoanRatioMin = prodLoanRatioMin;
    }

    public Long getProdAmountMax() {
        return prodAmountMax;
    }

    public void setProdAmountMax(Long prodAmountMax) {
        this.prodAmountMax = prodAmountMax;
    }

    public Long getProdAmountMin() {
        return prodAmountMin;
    }

    public void setProdAmountMin(Long prodAmountMin) {
        this.prodAmountMin = prodAmountMin;
    }

    public String getProdTerms() {
        return prodTerms;
    }

    public void setProdTerms(String prodTerms) {
        this.prodTerms = prodTerms;
    }

    public String getProdBillAccords() {
        return prodBillAccords;
    }

    public void setProdBillAccords(String prodBillAccords) {
        this.prodBillAccords = prodBillAccords;
    }

    public String getProdBillType() {
        return prodBillType;
    }

    public void setProdBillType(String prodBillType) {
        this.prodBillType = prodBillType;
    }

    public String getProdDeferMode() {
        return prodDeferMode;
    }

    public void setProdDeferMode(String prodDeferMode) {
        this.prodDeferMode = prodDeferMode;
    }

    public String getInterestAccrualMode() {
        return interestAccrualMode;
    }

    public void setInterestAccrualMode(String interestAccrualMode) {
        this.interestAccrualMode = interestAccrualMode;
    }

    public String getInterestSettleMode() {
        return interestSettleMode;
    }

    public void setInterestSettleMode(String interestSettleMode) {
        this.interestSettleMode = interestSettleMode;
    }

    public Integer getInterestSettleDays() {
        return interestSettleDays;
    }

    public void setInterestSettleDays(Integer interestSettleDays) {
        this.interestSettleDays = interestSettleDays;
    }

    public String getProdRepayMode() {
        return prodRepayMode;
    }

    public void setProdRepayMode(String prodRepayMode) {
        this.prodRepayMode = prodRepayMode;
    }

    public Integer getProdRepayCloseDays() {
        return prodRepayCloseDays;
    }

    public void setProdRepayCloseDays(Integer prodRepayCloseDays) {
        this.prodRepayCloseDays = prodRepayCloseDays;
    }

    public String getProdRepayPenaltyFlag() {
        return prodRepayPenaltyFlag;
    }

    public void setProdRepayPenaltyFlag(String prodRepayPenaltyFlag) {
        this.prodRepayPenaltyFlag = prodRepayPenaltyFlag;
    }

    public Integer getProdRepayPenaltyDays() {
        return prodRepayPenaltyDays;
    }

    public void setProdRepayPenaltyDays(Integer prodRepayPenaltyDays) {
        this.prodRepayPenaltyDays = prodRepayPenaltyDays;
    }

    public String getProdAddMode() {
        return prodAddMode;
    }

    public void setProdAddMode(String prodAddMode) {
        this.prodAddMode = prodAddMode;
    }

    public Integer getProdAddNum() {
        return prodAddNum;
    }

    public void setProdAddNum(Integer prodAddNum) {
        this.prodAddNum = prodAddNum;
    }

    public String getProdDeferedMode() {
        return prodDeferedMode;
    }

    public void setProdDeferedMode(String prodDeferedMode) {
        this.prodDeferedMode = prodDeferedMode;
    }

    public Integer getProdDeferedNum() {
        return prodDeferedNum;
    }

    public void setProdDeferedNum(Integer prodDeferedNum) {
        this.prodDeferedNum = prodDeferedNum;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public Date getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    public Integer getProdLoanLimit() {
        return prodLoanLimit;
    }

    public void setProdLoanLimit(Integer prodLoanLimit) {
        this.prodLoanLimit = prodLoanLimit;
    }

    public Integer getProdLoanCurLimit() {
        return prodLoanCurLimit;
    }

    public void setProdLoanCurLimit(Integer prodLoanCurLimit) {
        this.prodLoanCurLimit = prodLoanCurLimit;
    }

    private Long prodId;
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
    private Date createDatetime;
    private Date updateDatetime;
    private Integer prodLoanLimit;
    private Integer prodLoanCurLimit;
    private Integer addToProd;
    private Integer deferedToProd;
}
