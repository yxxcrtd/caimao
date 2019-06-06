package com.caimao.bana.api.entity.zeus;

import java.io.Serializable;
import java.util.Date;

/**
 * 持仓信息
 */
public class ZeusHomsAccountHoldEntity implements Serializable {
    private Long id;
    private Long userId;
    private String realName;
    private String mobile;
    private String homsFundAccount;
    private String homsCombineId;
    private Byte exchangeType;
    private String stockCode;
    private String stockName;
    private String currentAmount;
    private String enableAmount;
    private String costBalance;
    private String marketValue;
    private String buyAmount;
    private String sellAmount;
    private String updated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public Byte getExchangeType() {
        return exchangeType;
    }

    public void setExchangeType(Byte exchangeType) {
        this.exchangeType = exchangeType;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(String currentAmount) {
        this.currentAmount = currentAmount;
    }

    public String getEnableAmount() {
        return enableAmount;
    }

    public void setEnableAmount(String enableAmount) {
        this.enableAmount = enableAmount;
    }

    public String getCostBalance() {
        return costBalance;
    }

    public void setCostBalance(String costBalance) {
        this.costBalance = costBalance;
    }

    public String getMarketValue() {
        return marketValue;
    }

    public void setMarketValue(String marketValue) {
        this.marketValue = marketValue;
    }

    public String getBuyAmount() {
        return buyAmount;
    }

    public void setBuyAmount(String buyAmount) {
        this.buyAmount = buyAmount;
    }

    public String getSellAmount() {
        return sellAmount;
    }

    public void setSellAmount(String sellAmount) {
        this.sellAmount = sellAmount;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }
}
