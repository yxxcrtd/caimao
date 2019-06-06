/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. 
 *All Rights Reserved
 */
package com.caimao.bana.api.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class TpzHomsAccountChildEntity implements Serializable {
    private Long id;
    private String homsFundAccount;
    private String homsCombineId;
    private String homsCombineName;
    private String homsCombineStatus;
    private String combineNo;
    private String assetId;
    private String clientName;
    private String assetunitName;
    private BigDecimal counterFee;
    private String operatorNo;
    private String operatorPwd;
    private String remark;
    private String tradeLimit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHomsFundAccount() {
        return homsFundAccount;
    }

    public void setHomsFundAccount(String homsFundAccount) {
        this.homsFundAccount = homsFundAccount;
    }

    public String getHomsCombineId() {
        return homsCombineId;
    }

    public void setHomsCombineId(String homsCombineId) {
        this.homsCombineId = homsCombineId;
    }

    public String getHomsCombineName() {
        return homsCombineName;
    }

    public void setHomsCombineName(String homsCombineName) {
        this.homsCombineName = homsCombineName;
    }

    public String getHomsCombineStatus() {
        return homsCombineStatus;
    }

    public void setHomsCombineStatus(String homsCombineStatus) {
        this.homsCombineStatus = homsCombineStatus;
    }

    public String getCombineNo() {
        return combineNo;
    }

    public void setCombineNo(String combineNo) {
        this.combineNo = combineNo;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getAssetunitName() {
        return assetunitName;
    }

    public void setAssetunitName(String assetunitName) {
        this.assetunitName = assetunitName;
    }

    public BigDecimal getCounterFee() {
        return counterFee;
    }

    public void setCounterFee(BigDecimal counterFee) {
        this.counterFee = counterFee;
    }

    public String getOperatorNo() {
        return operatorNo;
    }

    public void setOperatorNo(String operatorNo) {
        this.operatorNo = operatorNo;
    }

    public String getOperatorPwd() {
        return operatorPwd;
    }

    public void setOperatorPwd(String operatorPwd) {
        this.operatorPwd = operatorPwd;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTradeLimit() {
        return tradeLimit;
    }

    public void setTradeLimit(String tradeLimit) {
        this.tradeLimit = tradeLimit;
    }
}