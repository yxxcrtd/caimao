package com.caimao.bana.api.entity.ybk;

import java.io.Serializable;
import java.util.Date;

/**
 * 邮币卡RSI
 */
public class YBKRSIEntity implements Serializable {
    private String exchangeName;
    private String code;
    private Date date;
    private Date updateTime;
    private Long rsi1;
    private Long rsi2;
    private Long rsi3;

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

    public Long getRsi1() {
        return rsi1;
    }

    public void setRsi1(Long rsi1) {
        this.rsi1 = rsi1;
    }

    public Long getRsi2() {
        return rsi2;
    }

    public void setRsi2(Long rsi2) {
        this.rsi2 = rsi2;
    }

    public Long getRsi3() {
        return rsi3;
    }

    public void setRsi3(Long rsi3) {
        this.rsi3 = rsi3;
    }
}
