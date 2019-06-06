package com.caimao.hq.api.entity.req;

import java.io.Serializable;

/**
* GjsPriceAlertEntity 查询请求对象
*
* Created by wangxu@huobi.com on 2015-11-23 11:10:10 星期一
*/
public class FQueryGjsPriceAlertReq implements Serializable {
    private Long userId;
    private String exchange;
    private String goodCode;
    private String condition;
    private String on;
    private String price;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getGoodCode() {
        return goodCode;
    }

    public void setGoodCode(String goodCode) {
        this.goodCode = goodCode;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getOn() {
        return on;
    }

    public void setOn(String on) {
        this.on = on;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}