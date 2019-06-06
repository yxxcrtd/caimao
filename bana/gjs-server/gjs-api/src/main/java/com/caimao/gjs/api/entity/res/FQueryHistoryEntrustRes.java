package com.caimao.gjs.api.entity.res;

import java.io.Serializable;

/**
 * 查询历史委托
 */
public class FQueryHistoryEntrustRes implements Serializable {
    /**订单编号*/
    private String orderNo;
    /**商品代码*/
    private String prodCode;
    /**品种名称*/
    private String prodName;
    /**交易类型*/
    private String tradeType;
    /**委托价格*/
    private String price;
    /**委托数量*/
    private String amount;
    /**成交数量*/
    private String processedAmount;
    /**保证金*/
    private String bailMoney;
    /**手续费*/
    private String feeMoney;
    /**委托单状态 A未成交 B部分成交 C全部成交 D已撤单*/
    private String state;
    /**委托日期*/
    private String date;
    /**是否强平 1是 0否*/
    private String isForce;
    /**撤销时间*/
    private String cancelTime;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
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

    public String getProcessedAmount() {
        return processedAmount;
    }

    public void setProcessedAmount(String processedAmount) {
        this.processedAmount = processedAmount;
    }

    public String getBailMoney() {
        return bailMoney;
    }

    public void setBailMoney(String bailMoney) {
        this.bailMoney = bailMoney;
    }

    public String getFeeMoney() {
        return feeMoney;
    }

    public void setFeeMoney(String feeMoney) {
        this.feeMoney = feeMoney;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public String getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(String cancelTime) {
        this.cancelTime = cancelTime;
    }
}