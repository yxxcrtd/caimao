package com.caimao.bana.api.entity.req.ybk;

import java.io.Serializable;
import java.util.Date;

/**
 * 邮币卡行情查询的通用请求对象
 * Created by Administrator on 2015/9/14.
 */
public class FYbkMarketReq implements Serializable {

    /**
     * 交易所简称
     */
    private String exchangeShortName;

    /**
     * 商品代码
     */
    private String code;

    /**
     * 显示条数
     */
    private Integer limit;

    /**
     * 时间
     */
    private Date datetime;

    public String getExchangeShortName() {
        return exchangeShortName;
    }

    public void setExchangeShortName(String exchangeShortName) {
        this.exchangeShortName = exchangeShortName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }
}
