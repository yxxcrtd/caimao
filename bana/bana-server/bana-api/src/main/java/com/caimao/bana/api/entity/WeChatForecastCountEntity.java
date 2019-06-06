package com.caimao.bana.api.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 微信预测信息统计表
 */
public class WeChatForecastCountEntity implements Serializable{
    private static final long serialVersionUID = 4867559940481517694L;
    private Long weChatUserId;
    private Integer correctDays;
    private BigDecimal beatRate;
    private Long created;
    private Long updated;

    public Long getWeChatUserId() {
        return weChatUserId;
    }

    public void setWeChatUserId(Long weChatUserId) {
        this.weChatUserId = weChatUserId;
    }

    public Integer getCorrectDays() {
        return correctDays;
    }

    public void setCorrectDays(Integer correctDays) {
        this.correctDays = correctDays;
    }

    public BigDecimal getBeatRate() {
        return beatRate;
    }

    public void setBeatRate(BigDecimal beatRate) {
        this.beatRate = beatRate;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public Long getUpdated() {
        return updated;
    }

    public void setUpdated(Long updated) {
        this.updated = updated;
    }
}