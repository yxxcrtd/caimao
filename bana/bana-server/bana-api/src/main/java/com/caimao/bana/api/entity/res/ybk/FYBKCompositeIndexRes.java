package com.caimao.bana.api.entity.res.ybk;

import com.caimao.bana.api.entity.ybk.YBKActivityEntity;

import java.io.Serializable;
import java.util.List;

/**
 * 交易所综合指数数据
 */
public class FYBKCompositeIndexRes implements Serializable {
    private Integer exchangeId;
    private String shortName;
    private String exchangeName;
    private Long yesterBalancePrice;
    private Long currentPrice;
    private Long changeRate;
    private Long totalAmount;
    private Long totalMoney;
    private Long highPrice;
    private Long lowPrice;
    private Integer shenGouNum;
    private Integer isOpen;
    List<YBKActivityEntity> activityList;

    public List<YBKActivityEntity> getActivityList() {
        return activityList;
    }

    public void setActivityList(List<YBKActivityEntity> activityList) {
        this.activityList = activityList;
    }

    public Integer getShenGouNum() {
        return shenGouNum;
    }

    public void setShenGouNum(Integer shenGouNum) {
        this.shenGouNum = shenGouNum;
    }

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

    public Long getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(Long currentPrice) {
        this.currentPrice = currentPrice;
    }

    public Long getChangeRate() {
        return changeRate;
    }

    public void setChangeRate(Long changeRate) {
        this.changeRate = changeRate;
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

    public Integer getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(Integer isOpen) {
        this.isOpen = isOpen;
    }
}