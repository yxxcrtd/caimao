package com.caimao.bana.api.entity.ybk;

import java.io.Serializable;

/**
 * 综合指数
 */
public class YBKSummaryEntity implements Serializable {
    private Integer exchangeId;
    private String shortName;
    private String exchangeName;
    private Long yesterBalancePrice;
    private Long openPrice;
    private Long curPrice;
    private Long currentGains;
    private Long totalAmount;
    private Long totalMoney;
    private Long highPrice;
    private Long lowPrice;

    public Integer getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(Integer exchangeId) {
        this.exchangeId = exchangeId;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public Long getYesterBalancePrice() {
        return yesterBalancePrice;
    }

    public void setYesterBalancePrice(Long yesterBalancePrice) {
        this.yesterBalancePrice = yesterBalancePrice;
    }

    public Long getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(Long openPrice) {
        this.openPrice = openPrice;
    }

    public Long getCurPrice() {
        return curPrice;
    }

    public void setCurPrice(Long curPrice) {
        this.curPrice = curPrice;
    }

    public Long getCurrentGains() {
        return currentGains;
    }

    public void setCurrentGains(Long currentGains) {
        this.currentGains = currentGains;
    }

    public Long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Long getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Long totalMoney) {
        this.totalMoney = totalMoney;
    }

    public Long getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(Long highPrice) {
        this.highPrice = highPrice;
    }

    public Long getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(Long lowPrice) {
        this.lowPrice = lowPrice;
    }
}