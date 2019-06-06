package com.caimao.gjs.api.entity;

import java.io.Serializable;

/**
* GjsHolidayEntity 实例对象
*
* Created by yangxinxin@huobi.com on 2015-11-06 18:07:52 星期五
*/
public class GjsHolidayEntity implements Serializable {

    private Long id;

    private String exchange;

    private String holiday;

    private String tradeTime;

    private String optDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getHoliday() {
        return holiday;
    }

    public void setHoliday(String holiday) {
        this.holiday = holiday;
    }

    public String getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(String tradeTime) {
        this.tradeTime = tradeTime;
    }

    public String getOptDate() {
        return optDate;
    }

    public void setOptDate(String optDate) {
        this.optDate = optDate;
    }

}