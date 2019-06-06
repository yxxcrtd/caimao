package com.caimao.gjs.api.entity.req;

import com.caimao.bana.common.api.entity.QueryBase;
import com.caimao.gjs.api.entity.res.FQueryGjsHolidayRes;

import java.io.Serializable;

/**
* GjsHolidayEntity 查询请求对象
*
* Created by yangxinxin@huobi.com on 2015-11-06 18:07:52 星期五
*/
public class FQueryGjsHolidayReq extends QueryBase<FQueryGjsHolidayRes> implements Serializable {
    // TODO 这里的字段需要根据业务需求删除，当前提供的是全字段
    private Long id;
    private String exchange;
    private String holiday;
    private String tradeTime;
    private String optDate;
    private String beginHoliday;

    public String getBeginHoliday() {
        return beginHoliday;
    }

    public void setBeginHoliday(String beginHoliday) {
        this.beginHoliday = beginHoliday;
    }

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