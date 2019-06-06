package com.caimao.hq.api.entity;

import java.io.Serializable;

/**
 * Created by yzc on 2015/11/11.
 */
public class MultiDaySnapshotRes  extends   BaseBean implements Serializable {

    public double closePx;//收盘价
    public double preClosePx;//昨收盘价
    public String minTime;//格式为YYYYmmddHHMM对于其它周日的K线，返回格式为YYYYmmdd
    public double businessAmount;//成交数量
    public String prodCode;//产品代码
    private double lastPx;//最新价
    private   double lastSettle;//昨结算
    private   double settle;//结算
    private   int isGoods;//1现货，0其他
    public double averagePx;//均价

    public double getLastPx() {
        return lastPx;
    }

    public void setLastPx(double lastPx) {
        this.lastPx = lastPx;
    }

    public int getIsGoods() {
        return isGoods;
    }

    public void setIsGoods(int isGoods) {
        this.isGoods = isGoods;
    }

    public double getClosePx() {
        return closePx;
    }

    public void setClosePx(double closePx) {
        this.closePx = closePx;
    }

    public double getPreClosePx() {
        return preClosePx;
    }

    public void setPreClosePx(double preClosePx) {
        this.preClosePx = preClosePx;
    }

    public String getMinTime() {
        return minTime;
    }

    public void setMinTime(String minTime) {
        this.minTime = minTime;
    }

    public double getBusinessAmount() {
        return businessAmount;
    }

    public void setBusinessAmount(double businessAmount) {
        this.businessAmount = businessAmount;
    }

    public double getAveragePx() {
        return averagePx;
    }

    public void setAveragePx(double averagePx) {
        this.averagePx = averagePx;
    }

    public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

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
}
