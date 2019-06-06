package com.caimao.hq.api.entity;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/10/26.
 */
public class TradeAmountRes implements Serializable {
    public String exchange="";//交易所识别码
    public String prodCode="";
    public String prodName="";//产品名称
    public double lastAmount;//现量
    public String minTime="";//格式为YYYYmmddHHMMss
    private double lastPx;//最新价
    private   double lastSettle;//昨结算
    private   double settle;//结算
    private   int isGoods;//1现货，0其他
    public double averagePx;//均价

    public double getLastSettle() {
        return lastSettle;
    }

    public void setLastSettle(double lastSettle) {
        this.lastSettle = lastSettle;
    }

    public double getSettle() {
        return settle;
    }

    public void setSettle(double settle) {
        this.settle = settle;
    }

    public int getIsGoods() {
        return isGoods;
    }

    public void setIsGoods(int isGoods) {
        this.isGoods = isGoods;
    }

    public double getAveragePx() {
        return averagePx;
    }

    public void setAveragePx(double averagePx) {
        this.averagePx = averagePx;
    }

    public double getLastPx() {
        return lastPx;
    }

    public void setLastPx(double lastPx) {
        this.lastPx = lastPx;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
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

    public double getLastAmount() {
        return lastAmount;
    }

    public void setLastAmount(double lastAmount) {
        this.lastAmount = lastAmount;
    }

    public String getMinTime() {
        return minTime;
    }

    public void setMinTime(String minTime) {
        this.minTime = minTime;
    }
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
