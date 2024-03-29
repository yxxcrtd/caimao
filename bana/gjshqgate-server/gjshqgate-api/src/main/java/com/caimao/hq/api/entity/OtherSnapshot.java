package com.caimao.hq.api.entity;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

/**
 * Created by yzc on 2015/12/4.
 */
public class OtherSnapshot extends Snapshot implements Serializable {

    private String boardCode;//板块代码

    private double orderAmount;//订货数量
    private String bidGrp;//委托买盘,多个委托盘用|分割   档位,价格,数量以“,”号分割
    private String offerGrp;//委托卖盘 多个委托盘用|分割   档位,价格,数量以“,”号分割


    private String  tradeDate;//交易核心日期
    private String  tradeTime;//交易核心时间
    private double businessBalanceHand;//成交金额，按手计算


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

    public String getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }

    public String getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(String tradeTime) {
        this.tradeTime = tradeTime;
    }

    public double getBusinessBalanceHand() {
        return businessBalanceHand;
    }

    public void setBusinessBalanceHand(double businessBalanceHand) {
        this.businessBalanceHand = businessBalanceHand;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
