package com.caimao.bana.api.entity.ybk;

import java.io.Serializable;
import java.util.Date;

/**
 * 邮币卡分时线
 */
public class YBKTimeLineEntity implements Serializable {
    private String exchangeName;
    private String code;
    private Date datetime;
    private Long yesterBalancePrice;
    private Long openPrice;
    private Long curPrice;
    private Long currentGains;
    private Long totalAmount;
    private Long totalMoney;
    private Long highPrice;
    private Long lowPrice;
    private Long todayAmount;
    private Long todayMoney;

    public YBKTimeLineEntity(){

    }

    public YBKTimeLineEntity(String exchangeName,String code,Date datetime,Long yesterBalancePrice,Long openPrice,Long curPrice,Long currentGains,Long totalAmount,Long totalMoney,Long highPrice,Long lowPrice){
        this.exchangeName = exchangeName;
        this.code = code;
        this.datetime = datetime;
        this.yesterBalancePrice = yesterBalancePrice;
        this.openPrice = openPrice;
        this.curPrice = curPrice;
        this.currentGains = currentGains;
        this.totalAmount = totalAmount;
        this.totalMoney = totalMoney;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
    }

    public Long getTodayAmount() {
        return todayAmount;
    }

    public void setTodayAmount(Long todayAmount) {
        this.todayAmount = todayAmount;
    }

    public Long getTodayMoney() {
        return todayMoney;
    }

    public void setTodayMoney(Long todayMoney) {
        this.todayMoney = todayMoney;
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

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
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
