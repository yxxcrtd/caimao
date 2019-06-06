package com.caimao.bana.api.entity.res.ybk;

import java.io.Serializable;
import java.util.Date;

/**
 * 分时数据
 */
public class FYBKTimeLineRes implements Serializable {
    private Date datetime;
    private String type;
    private Long yesterPrice;
    private Long curPrice;
    private Long totalAmount;
    private Long totalMoney;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getYesterPrice() {
        return yesterPrice;
    }

    public void setYesterPrice(Long yesterPrice) {
        this.yesterPrice = yesterPrice;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public Long getCurPrice() {
        return curPrice;
    }

    public void setCurPrice(Long curPrice) {
        this.curPrice = curPrice;
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