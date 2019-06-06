package com.caimao.bana.api.entity.res.ybk;

import java.io.Serializable;
import java.util.Date;

/**
 * 交易所商品行情返回数据
 */
public class FYBKCollectionRankingRes implements Serializable {
    private String code;
    private String name;
    private Long changeRate;
    private Long currentPrice;
    private Long openPrice;
    private Long highPrice;
    private Long closePrice;
    private Long lowPrice;
    private Long totalAmount;
    private Long totalMoney;
    private Date updateTime;
    private String exchangeShortName;
    private String exchangeName;

    public String getExchangeShortName() {
        return exchangeShortName;
    }

    public void setExchangeShortName(String exchangeShortName) {
        this.exchangeShortName = exchangeShortName;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getChangeRate() {
        return changeRate;
    }

    public void setChangeRate(Long changeRate) {
        this.changeRate = changeRate;
    }

    public Long getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(Long currentPrice) {
        this.currentPrice = currentPrice;
    }

    public Long getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(Long openPrice) {
        this.openPrice = openPrice;
    }

    public Long getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(Long highPrice) {
        this.highPrice = highPrice;
    }

    public Long getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(Long closePrice) {
        this.closePrice = closePrice;
    }

    public Long getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(Long lowPrice) {
        this.lowPrice = lowPrice;
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}