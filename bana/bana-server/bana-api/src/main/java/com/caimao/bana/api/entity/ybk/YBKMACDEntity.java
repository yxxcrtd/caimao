package com.caimao.bana.api.entity.ybk;

import java.io.Serializable;
import java.util.Date;

/**
 * 邮币卡MACD
 */
public class YBKMACDEntity implements Serializable {
    private String exchangeName;
    private String code;
    private Date date;
    private Date updateTime;
    private Long dif;
    private Long dea;
    private Long macd;

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
