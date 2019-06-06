package com.caimao.bana.api.entity.activity;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户活动记录
 * Created by Administrator on 2015/7/31.
 */
public class BanaActivityRecordEntity implements Serializable {
    private Long id;
    private Integer actId;
    private Long userId;
    private Long amount;
    private Date datetime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getActId() {
        return actId;
    }

    public void setActId(Integer actId) {
        this.actId = actId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }
}
