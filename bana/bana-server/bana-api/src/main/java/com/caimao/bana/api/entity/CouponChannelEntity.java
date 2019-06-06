package com.caimao.bana.api.entity;

import com.caimao.bana.api.enums.CouponChannelType;

import java.io.Serializable;
import java.math.BigDecimal;

public class CouponChannelEntity implements Serializable {
    private static final long serialVersionUID = 5205307374789516093L;
    private Long id;
    private CouponChannelType channelType;
    private BigDecimal money;
    private BigDecimal scale;
    private Integer amount;
    private Byte status;
    private Integer created;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CouponChannelType getChannelType() {
        return channelType;
    }

    public void setChannelType(Integer value) {
        this.channelType = CouponChannelType.findByValue(value);
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public BigDecimal getScale() {
        return scale;
    }

    public void setScale(BigDecimal scale) {
        this.scale = scale;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Integer getCreated() {
        return created;
    }

    public void setCreated(Integer created) {
        this.created = created;
    }
}