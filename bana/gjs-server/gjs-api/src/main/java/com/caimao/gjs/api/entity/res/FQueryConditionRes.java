package com.caimao.gjs.api.entity.res;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 查询条件单
 */
public class FQueryConditionRes implements Serializable {
    /**申请日期*/
    private String applyDate;
    /**条件单编号*/
    private String orderNo;
    /**商品代码*/
    private String prodCode;
    /**商品名称*/
    private String prodName;
    /**交易类型*/
    private String tradeType;
    /**价格*/
    private String price;
    /**数量*/
    private String amount;
    /**触发价*/
    private String touchPrice;
    /**条件*/
    private String condition;
    /**状态*/
    private String state;
    /**创建时间*/
    private String setCreateTime;
    /**撤单时间*/
    private String cancelTime;
    /**委托时间*/
    private String execTime;
    /**有效日期*/
    private String validDate;
    /**委托单号*/
    private String entrustNo;

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

    public String getTouchPrice() {
        return touchPrice;
    }

    public void setTouchPrice(String touchPrice) {
        this.touchPrice = touchPrice;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
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
}