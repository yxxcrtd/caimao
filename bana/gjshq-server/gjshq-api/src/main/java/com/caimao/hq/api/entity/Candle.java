package com.caimao.hq.api.entity;

import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

public  class Candle extends   BaseBean implements Serializable,Cloneable{
	public String exchange;//交易所识别码
	public String prodCode;//产品代码
	public String prodName;//产品代码
	public String minTime;//格式为YYYYmmddHHMM对于其它周日的K线，返回格式为YYYYmmdd
	public double openPx;//开盘价
	public double highPx;//最高价
	public double lowPx;//最低价
	public double closePx;//收盘价
	public double lastPx;//最新价
	private double pxChange;//价格涨跌
	private double pxChangeRate;//涨跌幅
	public CandleCycle cycle;//K线周期
	public double businessAmount;//成交数量
	public double businessBalance;//成交金额
	public String redisKey="";
	public int number;//查询条数
	private String beginDate ;//按日期查询
	private String endDate ;
	public double preClosePx;//昨收盘价
	private   double lastSettle;//昨结算
	private   double settle;//结算
	private   int isGoods;//1现货，0其他
	public double averagePx;//均价

	public double getAveragePx() {
		return averagePx;
	}

	public void setAveragePx(double averagePx) {
		this.averagePx = averagePx;
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

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getRedisKey() {
		return redisKey;
	}

	public void setRedisKey(String redisKey) {
		this.redisKey = redisKey;
	}



	public Object clone() throws CloneNotSupportedException {
		return SerializationUtils.clone(this);
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

	public String getMinTime() {
		return minTime;
	}

	public void setMinTime(String minTime) {
		this.minTime = minTime;
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

	public double getClosePx() {
		return closePx;
	}

	public void setClosePx(double closePx) {
		this.closePx = closePx;
	}

	public double getLastPx() {
		return lastPx;
	}

	public void setLastPx(double lastPx) {
		this.lastPx = lastPx;
	}

	public CandleCycle getCycle() {
		return cycle;
	}

	public void setCycle(CandleCycle cycle) {
		this.cycle = cycle;
	}

	public double getBusinessAmount() {
		return businessAmount;
	}

	public void setBusinessAmount(double businessAmount) {
		this.businessAmount = businessAmount;
	}

	public double getBusinessBalance() {
		return businessBalance;
	}

	public void setBusinessBalance(double businessBalance) {
		this.businessBalance = businessBalance;
	}

	public double getPreClosePx() {
		return preClosePx;
	}

	public void setPreClosePx(double preClosePx) {
		this.preClosePx = preClosePx;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
