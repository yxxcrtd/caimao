package com.caimao.bana.api.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户自选股
 */
public class OptionalStockEntity implements Serializable{
    private Long id;
    private Long userId;
    private String stockCode;
    private String stockName;
    private Byte marketType;
    private Long sort;
    private Date created;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public Byte getMarketType() {
        return marketType;
    }

    public void setMarketType(Byte marketType) {
        this.marketType = marketType;
    }

    public Long getSort() {
        return sort;
    }

    public void setSort(Long sort) {
        this.sort = sort;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
