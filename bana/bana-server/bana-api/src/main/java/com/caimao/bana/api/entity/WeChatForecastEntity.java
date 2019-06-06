package com.caimao.bana.api.entity;

import java.io.Serializable;

/**
 * 微信预测记录表
 */
public class WeChatForecastEntity implements Serializable{
    private static final long serialVersionUID = -642926974644140637L;
    private Long forecastDate;
    private Long weChatUserId;
    private Byte forecastType;
    private Long created;

    public Long getForecastDate() {
        return forecastDate;
    }

    public void setForecastDate(Long forecastDate) {
        this.forecastDate = forecastDate;
    }

    public Long getWeChatUserId() {
        return weChatUserId;
    }

    public void setWeChatUserId(Long weChatUserId) {
        this.weChatUserId = weChatUserId;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public Byte getForecastType() {
        return forecastType;
    }

    public void setForecastType(Byte forecastType) {
        this.forecastType = forecastType;
    }
}