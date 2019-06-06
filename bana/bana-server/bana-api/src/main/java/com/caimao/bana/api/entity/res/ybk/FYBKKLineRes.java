package com.caimao.bana.api.entity.res.ybk;

import java.io.Serializable;
import java.util.Date;

/**
 * 日K行情数据
 */
public class FYBKKLineRes implements Serializable {
    private Date date;
    private Date updateTime;
    private Long openPrice;
    private Long highPrice;
    private Long lowPrice;
    private Long curPrice;
    private Long closePrice;
    private Long totalAmount;
    private Long currentGains;
    private Long totalMoney;

    public Long getCurrentGains() {
        return currentGains;
    }

    public void setCurrentGains(Long currentGains) {
        this.currentGains = currentGains;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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

    public Long getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(Long lowPrice) {
        this.lowPrice = lowPrice;
    }

    public Long getCurPrice() {
        return curPrice;
    }

    public void setCurPrice(Long curPrice) {
        this.curPrice = curPrice;
    }

    public Long getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(Long closePrice) {
        this.closePrice = closePrice;
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
}