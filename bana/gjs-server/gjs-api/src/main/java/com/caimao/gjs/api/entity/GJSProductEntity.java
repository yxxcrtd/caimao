package com.caimao.gjs.api.entity;

import java.io.Serializable;

public class GJSProductEntity implements Serializable {
    private Long productId;
    private String exchange;
    private String prodCode;
    private String prodName;
    private Integer tradeType;
    private Integer isGoods;
    private Integer isShow;
    private Integer priceUnit;
    private Integer handUnit;
    private Integer priceChangeUnit;
    private Integer priceLimit;
    private Integer marginRatio;
    private Integer sort;
    private Integer feeRate;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
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

    public Integer getTradeType() {
        return tradeType;
    }

    public void setTradeType(Integer tradeType) {
        this.tradeType = tradeType;
    }

    public Integer getIsGoods() {
        return isGoods;
    }

    public void setIsGoods(Integer isGoods) {
        this.isGoods = isGoods;
    }

    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public Integer getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(Integer priceUnit) {
        this.priceUnit = priceUnit;
    }

    public Integer getHandUnit() {
        return handUnit;
    }

    public void setHandUnit(Integer handUnit) {
        this.handUnit = handUnit;
    }

    public Integer getPriceChangeUnit() {
        return priceChangeUnit;
    }

    public void setPriceChangeUnit(Integer priceChangeUnit) {
        this.priceChangeUnit = priceChangeUnit;
    }

    public Integer getPriceLimit() {
        return priceLimit;
    }

    public void setPriceLimit(Integer priceLimit) {
        this.priceLimit = priceLimit;
    }

    public Integer getMarginRatio() {
        return marginRatio;
    }

    public void setMarginRatio(Integer marginRatio) {
        this.marginRatio = marginRatio;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getFeeRate() {
        return feeRate;
    }

    public void setFeeRate(Integer feeRate) {
        this.feeRate = feeRate;
    }
}