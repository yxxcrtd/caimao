package com.caimao.hq.api.entity;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/10/20.
 */
public class SnapshotTrickRes extends   BaseBean implements Serializable {

    public String prodCode="";
    public double lastPx;//最新价
    public String bidGrp="";//委托买盘,多个委托盘用|分割   档位,价格,数量以“,”号分割
    public String offerGrp="";//委托卖盘 多个委托盘用|分割   档位,价格,数量以“,”号分割
    public double highLimit;//涨停板
    public double lowLimit;//跌停停板
    public String prodName="";//产品名称
    public String exchange="";//交易所识别码
    public double pxChange;//价格涨跌
    public double pxChangeRate;//涨跌幅
    private double preclosePx;//昨收盘
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

    public double getPreclosePx() {
        return preclosePx;
    }

    public void setPreclosePx(double preclosePx) {
        this.preclosePx = preclosePx;
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

    public double getLastPx() {
        return lastPx;
    }

    public void setLastPx(double lastPx) {
        this.lastPx = lastPx;
    }

    public String getBidGrp() {
        return bidGrp;
    }

    public void setBidGrp(String bidGrp) {
        this.bidGrp = bidGrp;
    }

    public String getOfferGrp() {
        return offerGrp;
    }

    public void setOfferGrp(String offerGrp) {
        this.offerGrp = offerGrp;
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

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
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
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
