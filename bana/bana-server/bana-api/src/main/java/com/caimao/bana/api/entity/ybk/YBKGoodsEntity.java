package com.caimao.bana.api.entity.ybk;

import java.io.Serializable;

/**
 * 藏品列表
 */
public class YBKGoodsEntity implements Serializable {
    private Integer exchangeId;
    private String shortName;
    private String exchangeName;
    private String goodCode;
    private String goodName;
    private String goodPinyin;

    public Integer getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(Integer exchangeId) {
        this.exchangeId = exchangeId;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public String getGoodCode() {
        return goodCode;
    }

    public void setGoodCode(String goodCode) {
        this.goodCode = goodCode;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public String getGoodPinyin() {
        return goodPinyin;
    }

    public void setGoodPinyin(String goodPinyin) {
        this.goodPinyin = goodPinyin;
    }
}