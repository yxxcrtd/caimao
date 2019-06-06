package com.caimao.bana.api.entity.ybk;

import java.io.Serializable;
import java.util.Date;

/**
 * 邮币卡KDJ
 */
public class YBKKDJEntity implements Serializable {
    private String exchangeName;
    private String code;
    private Date date;
    private Date updateTime;
    private Long rsv;
    private Long k;
    private Long d;
    private Long j;

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

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

    public Long getRsv() {
        return rsv;
    }

    public void setRsv(Long rsv) {
        this.rsv = rsv;
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
