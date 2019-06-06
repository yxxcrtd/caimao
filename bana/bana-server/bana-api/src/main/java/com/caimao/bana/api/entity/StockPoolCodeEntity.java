package com.caimao.bana.api.entity;

import java.io.Serializable;

public class StockPoolCodeEntity implements Serializable {
    private static final long serialVersionUID = -1663093286377251876L;
    private Long id;
    private Long stockPoolNo;
    private String exchangeType;
    private String stockCode;
    private String stockName;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStockPoolNo() {
        return this.stockPoolNo;
    }

    public void setStockPoolNo(Long stockPoolNo) {
        this.stockPoolNo = stockPoolNo;
    }

    public String getExchangeType() {
        return this.exchangeType;
    }

    public void setExchangeType(String exchangeType) {
        this.exchangeType = exchangeType;
    }

    public String getStockCode() {
        return this.stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getStockName() {
        return this.stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }
}