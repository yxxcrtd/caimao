package com.caimao.bana.api.entity.res.ybk;

import java.io.Serializable;
import java.util.Date;

/**
 * KDJ指标数据
 */
public class FYBKKDJRes implements Serializable {
    private Date date;
    private Date updateTime;
    private Long k;
    private Long d;
    private Long j;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getK() {
        return k;
    }

    public void setK(Long k) {
        this.k = k;
    }

    public Long getD() {
        return d;
    }

    public void setD(Long d) {
        this.d = d;
    }

    public Long getJ() {
        return j;
    }

    public void setJ(Long j) {
        this.j = j;
    }
}