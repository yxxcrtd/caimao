package com.caimao.bana.api.entity.guji;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
* GujiUserStockEntity 实例对象
*
* Created by wangxu@huobi.com on 2016-01-07 17:44:32 星期四
*/
public class GujiUserStockEntity implements Serializable {

    /**  */
    private Long id;

    /** 微信用户ID */
    private Long wxId;

    /** 微信唯一标示 */
    private String openId;

    /** 持仓类型 */
    private String stockType;

    /** 股票代码 */
    private String stockCode;

    /** 股票名称 */
    private String stockName;

    /** 股票价格 */
    private String stockPrice;

    /** 目标价位 */
    private String targetPrice;

    /** 更新时间 */
    private Date updateTime;

    /** 创建时间 */
    private Date createTime;

    private String updateStr;

    /** 当前价 */
    private float currentPrice;

    /** 涨幅 */
    private float increase;

    public String getUpdateStr() {
        return updateStr;
    }

    public void setUpdateStr(String updateStr) {
        this.updateStr = updateStr;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWxId() {
        return wxId;
    }

    public void setWxId(Long wxId) {
        this.wxId = wxId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getStockType() {
        return stockType;
    }

    public void setStockType(String stockType) {
        this.stockType = stockType;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getStockPrice() {
        return stockPrice;
    }

    public void setStockPrice(String stockPrice) {
        this.stockPrice = stockPrice;
    }

    public String getTargetPrice() {
        return targetPrice;
    }

    public void setTargetPrice(String targetPrice) {
        this.targetPrice = targetPrice;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public float getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(float currentPrice) {
        this.currentPrice = currentPrice;
    }

    public float getIncrease() {
        return increase;
    }

    public void setIncrease(float increase) {
        this.increase = increase;
    }
}