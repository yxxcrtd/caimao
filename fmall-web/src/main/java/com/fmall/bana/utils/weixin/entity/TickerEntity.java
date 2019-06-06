package com.fmall.bana.utils.weixin.entity;

/**
 * 股票ticker信息
 * Created by Administrator on 2016/1/5.
 */
public class TickerEntity {
    private String name;    //股票名字
    private String code;    //股票代码
    private String openPrice;   //今日开盘价
    private String closePrice;  //昨日收盘价
    private float curPrice;    //当前价格
    private String highPrice;   //今日最高价
    private String lowPrice;    //今日最低价
    private String totalNum;    //成交的股票数
    private String totalMoney;  //成交金额
    private float increase; // 涨幅

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(String openPrice) {
        this.openPrice = openPrice;
    }

    public String getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(String closePrice) {
        this.closePrice = closePrice;
    }

    public float getCurPrice() {
        return curPrice;
    }

    public void setCurPrice(float curPrice) {
        this.curPrice = curPrice;
    }

    public String getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(String highPrice) {
        this.highPrice = highPrice;
    }

    public String getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(String lowPrice) {
        this.lowPrice = lowPrice;
    }

    public String getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(String totalNum) {
        this.totalNum = totalNum;
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }

    public float getIncrease() {
        return increase;
    }

    public void setIncrease(float increase) {
        this.increase = increase;
    }

}
