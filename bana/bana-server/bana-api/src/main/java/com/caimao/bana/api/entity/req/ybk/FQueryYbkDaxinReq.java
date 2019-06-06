package com.caimao.bana.api.entity.req.ybk;

import com.caimao.bana.api.entity.ybk.YbkDaxinEntity;
import com.caimao.bana.api.entity.QueryBase;

import java.io.Serializable;
import java.util.Date;

/**
* YbkDaxinEntity 查询请求对象
*
* Created by yangxinxin@huobi.com on 2015-11-17 17:47:39 星期二
*/
public class FQueryYbkDaxinReq extends QueryBase<YbkDaxinEntity> implements Serializable {
    // TODO 这里的字段需要根据业务需求删除，当前提供的是全字段
    private Integer exchangeId;
    private Date endDatetime;
    private String isShow;

    public Integer getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(Integer exchangeId) {
        this.exchangeId = exchangeId;
    }

    public Date getEndDatetime() {
        return endDatetime;
    }

    public void setEndDatetime(Date endDatetime) {
        this.endDatetime = endDatetime;
    }

    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow;
    }
}