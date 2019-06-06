package com.caimao.hq.api.entity;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

public class SJSSnapshot extends Snapshot implements Serializable {

    private static final long serialVersionUID = 522889572773714584L;
    private String boardCode;//板块代码

    private double orderAmount;//订货数量
    private String bidGrp;//委托买盘,多个委托盘用|分割   档位,价格,数量以“,”号分割
    private String offerGrp;//委托卖盘 多个委托盘用|分割   档位,价格,数量以“,”号分割
    private double businessBalanceHand;//成交金额，按手计算
    private double lastWeight;//成交（双边）重量
    private double highLimit;//涨停板
    private double lowLimit;//跌停停板
    private String sequenceNo;//行情序号
    private double closePx;//收盘价

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getBoardCode() {
        return boardCode;
    }

    public void setBoardCode(String boardCode) {
        this.boardCode = boardCode;
    }


    public double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(double orderAmount) {
        this.orderAmount = orderAmount;
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


    public double getBusinessBalanceHand() {
        return businessBalanceHand;
    }

    public void setBusinessBalanceHand(double businessBalanceHand) {
        this.businessBalanceHand = businessBalanceHand;
    }

    public double getLastWeight() {
        return lastWeight;
    }

    public void setLastWeight(double lastWeight) {
        this.lastWeight = lastWeight;
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

    public String getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(String sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public double getClosePx() {
        return closePx;
    }

    public void setClosePx(double closePx) {
        this.closePx = closePx;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
