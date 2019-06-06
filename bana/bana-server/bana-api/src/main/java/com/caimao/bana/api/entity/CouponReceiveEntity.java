package com.caimao.bana.api.entity;

import com.caimao.bana.api.enums.CouponReceiveStatus;

import java.io.Serializable;
import java.math.BigDecimal;

public class CouponReceiveEntity implements Serializable {
    private static final long serialVersionUID = -2087301208364882705L;
    private Long id;
    private Long couponId;
    private Long userId;
    private BigDecimal money;
    private Byte receiveType;
    private Long phone;
    private Long extId;
    private CouponReceiveStatus status;
    private Integer created;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Byte getReceiveType() {
        return receiveType;
    }

    public void setReceiveType(Byte receiveType) {
        this.receiveType = receiveType;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public Long getExtId() {
        return extId;
    }

    public void setExtId(Long extId) {
        this.extId = extId;
    }

    public CouponReceiveStatus getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = CouponReceiveStatus.findByValue(status);
    }

    public Integer getCreated() {
        return created;
    }

    public void setCreated(Integer created) {
        this.created = created;
    }
}