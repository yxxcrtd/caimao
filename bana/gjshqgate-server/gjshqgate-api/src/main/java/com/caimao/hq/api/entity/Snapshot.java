package com.caimao.hq.api.entity;

import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/10/14.
 */
public  class   Snapshot  implements Serializable,Cloneable {
    public double closePx;//收盘价
    public String snapshotId;
    public String exchange;//交易所识别码
    public String prodCode;//产品代码
    public String prodName;//产品名称
    public String minTime;//格式为YYYYmmddHHMMss
    public String optDate;//添加时间
    public String status;//数据状态
    public String beginDate ;//按日期查询
    public String endDate ;
    public int beginNumber;//分页查询
    public int endNumber;
    private double lastAmount;//现量
    public String cycleTimeDate="";//根据周期格式化的时间
    public String redisKey="";
    public String redisKeyHistory="";
    public CandleCycle cycle=null;
    private double lastPx;//最新价
    private double pxChange;//价格涨跌
    private double pxChangeRate;//涨跌幅
    private double highPx;//最高价
    private double lowPx;;//最低价
    private double businessBalance;//成交金额，按手计算
    private double businessAmount;//成交数量
    private double preclosePx;//昨收盘
    private double openPx;//开盘价
    private double averagePx;//均价
    private   double lastSettle;//昨结算
    private   double settle;//结算
    private   int isGoods;//1现货，2期货
    private String  apdRecvTime;//行情到达时间
    private String  tradeDate;//交易核心日期
    private String  tradeTime;//交易核心时间

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

    public String getRedisKeyHistory() {
        return redisKeyHistory;
    }

    public void setRedisKeyHistory(String redisKeyHistory) {
        this.redisKeyHistory = redisKeyHistory;
    }

    public String getApdRecvTime() {
        return apdRecvTime;
    }

    public void setApdRecvTime(String apdRecvTime) {
        this.apdRecvTime = apdRecvTime;
    }

    public double getClosePx() {
        return closePx;
    }

    public void setClosePx(double closePx) {
        this.closePx = closePx;
    }

    public int getIsGoods() {
        return isGoods;
    }

    public void setIsGoods(int isGoods) {
        this.isGoods = isGoods;
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

    public double getAveragePx() {
        return averagePx;
    }

    public void setAveragePx(double averagePx) {
        this.averagePx = averagePx;
    }

    public double getOpenPx() {
        return openPx;
    }

    public void setOpenPx(double openPx) {
        this.openPx = openPx;
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

    public double getBusinessBalance() {
        return businessBalance;
    }

    public void setBusinessBalance(double businessBalance) {
        this.businessBalance = businessBalance;
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

    public Object clone() throws CloneNotSupportedException {
        return SerializationUtils.clone(this);
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

    public String getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(String snapshotId) {
        this.snapshotId = snapshotId;
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

    public String getMinTime() {
        return minTime;
    }

    public void setMinTime(String minTime) {
        this.minTime = minTime;
    }

    public String getOptDate() {
        return optDate;
    }

    public void setOptDate(String optDate) {
        this.optDate = optDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getBeginNumber() {
        return beginNumber;
    }

    public void setBeginNumber(int beginNumber) {
        this.beginNumber = beginNumber;
    }

    public int getEndNumber() {
        return endNumber;
    }

    public void setEndNumber(int endNumber) {
        this.endNumber = endNumber;
    }

    public double getLastAmount() {
        return lastAmount;
    }

    public void setLastAmount(double lastAmount) {
        this.lastAmount = lastAmount;
    }

    public String getCycleTimeDate() {
        return cycleTimeDate;
    }

    public void setCycleTimeDate(String cycleTimeDate) {
        this.cycleTimeDate = cycleTimeDate;
    }

    public String getRedisKey() {
        return redisKey;
    }

    public void setRedisKey(String redisKey) {
        this.redisKey = redisKey;
    }

    public CandleCycle getCycle() {
        return cycle;
    }

    public void setCycle(CandleCycle cycle) {
        this.cycle = cycle;
    }

    public double getLastPx() {
        return lastPx;
    }

    public void setLastPx(double lastPx) {
        this.lastPx = lastPx;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
