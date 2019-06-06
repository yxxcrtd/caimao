package com.caimao.gjs.api.entity.res;

import java.io.Serializable;

/**
 * 查询历史交易
 */
public class FQueryHistoryTradeRes implements Serializable {
    /**委托编号*/
    private String orderNo;
    /**成交编号*/
    private String serialNo;
    /**商品编码*/
    private String prodCode;
    /**商品名称*/
    private String prodName;
    /**交易类型*/
    private String tradeType;
    /**成交价格*/
    private String price;
    /**成交数量*/
    private String amount;
    /**手续费*/
    private String feeMoney;
    /**成交时间*/
    private String processedTime;
    /**成交额*/
    private String totalMoney;
    /**开平标志*/
    private String offsetFlag;
    /**交易日期*/
    private String date;
    /**是否强平 1是 0否*/
    private String isForce;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFeeMoney() {
        return feeMoney;
    }

    public void setFeeMoney(String feeMoney) {
        this.feeMoney = feeMoney;
    }

    public String getProcessedTime() {
        return processedTime;
    }

    public void setProcessedTime(String processedTime) {
        this.processedTime = processedTime;
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getOffsetFlag() {
        return offsetFlag;
    }

    public void setOffsetFlag(String offsetFlag) {
        this.offsetFlag = offsetFlag;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIsForce() {
        return isForce;
    }

    public void setIsForce(String isForce) {
        this.isForce = isForce;
    }
}