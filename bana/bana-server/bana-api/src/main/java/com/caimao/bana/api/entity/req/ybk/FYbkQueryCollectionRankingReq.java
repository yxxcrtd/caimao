package com.caimao.bana.api.entity.req.ybk;

import com.caimao.bana.api.entity.QueryBase;
import com.caimao.bana.api.entity.res.ybk.FYBKCollectionRankingRes;

import java.io.Serializable;
import java.util.Date;

/**
 * 查询邮币卡交易所下所有商品行情信息列表
 * Created by Administrator on 2015/9/14.
 */
public class FYbkQueryCollectionRankingReq extends QueryBase<FYBKCollectionRankingRes> implements Serializable {

    /**
     * 交易所简称
     */
    private String exchangeShortName;

    /**
     * 显示的那个日期的
     */
    private Date hqDate;

    public Date getHqDate() {
        return hqDate;
    }

    public void setHqDate(Date hqDate) {
        this.hqDate = hqDate;
    }

    public String getExchangeShortName() {
        return exchangeShortName;
    }

    public void setExchangeShortName(String exchangeShortName) {
        this.exchangeShortName = exchangeShortName;
    }
}
