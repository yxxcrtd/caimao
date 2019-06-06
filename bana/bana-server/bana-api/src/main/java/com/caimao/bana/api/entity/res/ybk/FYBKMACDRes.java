package com.caimao.bana.api.entity.res.ybk;

import java.io.Serializable;
import java.util.Date;

/**
 * MACD指标数据
 */
public class FYBKMACDRes implements Serializable {
    private Date date;
    private Date updateTime;
    private Long dif;
    private Long dea;
    private Long macd;

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

    public Long getDif() {
        return dif;
    }

    public void setDif(Long dif) {
        this.dif = dif;
    }

    public Long getDea() {
        return dea;
    }

    public void setDea(Long dea) {
        this.dea = dea;
    }

    public Long getMacd() {
        return macd;
    }

    public void setMacd(Long macd) {
        this.macd = macd;
    }
}