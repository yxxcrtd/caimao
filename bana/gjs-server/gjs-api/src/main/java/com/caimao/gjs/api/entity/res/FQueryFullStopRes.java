package com.caimao.gjs.api.entity.res;

import java.io.Serializable;

/**
 * 查询止盈止损单
 */
public class FQueryFullStopRes implements Serializable {
    /**申请日期*/
    private String applyDate;
    /**单号*/
    private String orderNo;
    /**商品代码*/
    private String prodCode;
    /**商品名称*/
    private String prodName;
    /**交易类型 B买 S卖*/
    private String tradeType;
    /**止盈价*/
    private String upPrice;
    /**止损价*/
    private String downPrice;
    /**数量*/
    private String amount;
    /**状态 N未触发 Y已触发 C已撤单*/
    private String state;
    /**设置时间 格式 HHMMSS*/
    private String setCreateTime;
    /**撤单时间 格式 HHMMSS*/
    private String cancelTime;
    /**触发时间 格式 HHMMSS*/
    private String execTime;
    /**有效日期*/
    private String validDate;
    /**委托单号*/
    private String entrustNo;
    /**委托数量*/
    private String entrustNum;

    public String getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(String applyDate) {
        this.applyDate = applyDate;
    }

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

    public String getUpPrice() {
        return upPrice;
    }

    public void setUpPrice(String upPrice) {
        this.upPrice = upPrice;
    }

    public String getDownPrice() {
        return downPrice;
    }

    public void setDownPrice(String downPrice) {
        this.downPrice = downPrice;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSetCreateTime() {
        return setCreateTime;
    }

    public void setSetCreateTime(String setCreateTime) {
        this.setCreateTime = setCreateTime;
    }

    public String getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(String cancelTime) {
        this.cancelTime = cancelTime;
    }

    public String getExecTime() {
        return execTime;
    }

    public void setExecTime(String execTime) {
        this.execTime = execTime;
    }

    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(String validDate) {
        this.validDate = validDate;
    }

    public String getEntrustNo() {
        return entrustNo;
    }

    public void setEntrustNo(String entrustNo) {
        this.entrustNo = entrustNo;
    }

    public String getEntrustNum() {
        return entrustNum;
    }

    public void setEntrustNum(String entrustNum) {
        this.entrustNum = entrustNum;
    }
}