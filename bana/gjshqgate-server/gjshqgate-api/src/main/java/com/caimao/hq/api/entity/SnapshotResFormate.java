package com.caimao.hq.api.entity;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/10/20.
 */
public class SnapshotResFormate extends   BaseBean implements Serializable {

    public double openPx;//开盘价
    public double pxChange;//价格涨跌
    public double pxChangeRate;//涨跌幅
    public double highPx;//最高价
    public double lowPx;;//最低价
    public double businessAmount;//成交数量
    public double preclosePx;//昨收盘
    public double businessBalance;//成交金额，按手计算
    public double highLimit;//涨停板
    public double lowLimit;//跌停停板
    public double lastAmount;//现量
    private double lastPx;//最新价
    public String minTime="";//格式为YYYYmmddHHMMss
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

    public String getMinTime() {
        return minTime;
    }

    public void setMinTime(String minTime) {
        this.minTime = minTime;
    }

    public double getLastPx() {
        return lastPx;
    }

    public void setLastPx(double lastPx) {
        this.lastPx = lastPx;
    }


    public double getOpenPx() {
        return openPx;
    }

    public void setOpenPx(double openPx) {
        this.openPx = openPx;
    }

    public double getPxChange() {
        return pxChange;
    }

    public void setPxChange(double pxChange) {
        this.pxChange = pxChange;
    }

    public double getPxChangeRate() {
        return pxChangeRate;
    }

    public void setPxChangeRate(double pxChangeRate) {
        this.pxChangeRate = pxChangeRate;
    }

    public double getHighPx() {
        return highPx;
    }

    public void setHighPx(double highPx) {
        this.highPx = highPx;
    }

    public double getLowPx() {
        return lowPx;
    }

    public void setLowPx(double lowPx) {
        this.lowPx = lowPx;
    }

    public double getAveragePx() {
        return averagePx;
    }

    public void setAveragePx(double averagePx) {
        this.averagePx = averagePx;
    }

    public double getBusinessAmount() {
        return businessAmount;
    }

    public void setBusinessAmount(double businessAmount) {
        this.businessAmount = businessAmount;
    }

    public double getPreclosePx() {
        return preclosePx;
    }

    public void setPreclosePx(double preclosePx) {
        this.preclosePx = preclosePx;
    }

    public double getBusinessBalance() {
        return businessBalance;
    }

    public void setBusinessBalance(double businessBalance) {
        this.businessBalance = businessBalance;
    }

    public double getHighLimit() {
        return highLimit;
    }

    public void setHighLimit(double highLimit) {
        this.highLimit = highLimit;
    }

    public double getLowLimit() {
        return lowLimit;
    }

    public void setLowLimit(double lowLimit) {
        this.lowLimit = lowLimit;
    }

    public double getLastAmount() {
        return lastAmount;
    }

    public void setLastAmount(double lastAmount) {
        this.lastAmount = lastAmount;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
