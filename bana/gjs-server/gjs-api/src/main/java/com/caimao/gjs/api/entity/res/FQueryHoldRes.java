package com.caimao.gjs.api.entity.res;

import java.io.Serializable;

/**
 * 查询持仓
 */
public class FQueryHoldRes implements Serializable {
    /**商品代码*/
    private String prodCode;
    /**商品名称*/
    private String prodName;
    /**数量*/
    private String amount;
    /**盈亏*/
    private String surplus;
    /**盈亏率*/
    private String surplusRate;
    /**成本价*/
    private String costsPrice;
    /**成本市值*/
    private String costsValue;
    /**当前市值*/
    private String marketValue;
    /**买卖类型*/
    private String tradeType;
    /**最新价格*/
    private String newPrice;
    /**可用数量*/
    private String useAmount;
    /**保本价*/
    private String protectPrice;
    /**交易所*/
    private String exchange;

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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSurplus() {
        return surplus;
    }

    public void setSurplus(String surplus) {
        this.surplus = surplus;
    }

    public String getSurplusRate() {
        return surplusRate;
    }

    public void setSurplusRate(String surplusRate) {
        this.surplusRate = surplusRate;
    }

    public String getCostsPrice() {
        return costsPrice;
    }

    public void setCostsPrice(String costsPrice) {
        this.costsPrice = costsPrice;
    }

    public String getCostsValue() {
        return costsValue;
    }

    public void setCostsValue(String costsValue) {
        this.costsValue = costsValue;
    }

    public String getMarketValue() {
        return marketValue;
    }

    public void setMarketValue(String marketValue) {
        this.marketValue = marketValue;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(String newPrice) {
        this.newPrice = newPrice;
    }

    public String getUseAmount() {
        return useAmount;
    }

    public void setUseAmount(String useAmount) {
        this.useAmount = useAmount;
    }

    public String getProtectPrice() {
        return protectPrice;
    }

    public void setProtectPrice(String protectPrice) {
        this.protectPrice = protectPrice;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }
}