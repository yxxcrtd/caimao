package com.caimao.bana.api.entity.req.ybk;

import com.caimao.bana.api.entity.QueryBase;
import com.caimao.bana.api.entity.ybk.YBKActivityEntity;

import java.io.Serializable;

public class FYBKQueryActivityListReq extends QueryBase<YBKActivityEntity> implements Serializable{
    private Integer exchangeId;
    private String dateStart;
    private String dateEnd;
    private Integer isShow;

    // 聚合页提示语，存在redis中的
    private String topic;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Integer getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(Integer exchangeId) {
        this.exchangeId = exchangeId;
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart + " 00:00:00";
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd + " 23:59:59";
    }

    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }
}