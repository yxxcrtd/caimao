package com.caimao.gjs.api.entity.res;

import com.caimao.gjs.api.entity.GjsHolidayEntity;
import com.caimao.bana.common.api.entity.QueryBase;

import java.io.Serializable;

/**
* GjsHolidayEntity 查询响应对象
*
* Created by yangxinxin@huobi.com on 2015-11-06 18:07:52 星期五
*/
public class FQueryGjsHolidayRes extends QueryBase<GjsHolidayEntity> implements Serializable {
    private String exchange;
    private String holiday;

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
}