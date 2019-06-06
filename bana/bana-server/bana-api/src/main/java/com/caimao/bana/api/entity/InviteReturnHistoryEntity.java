/*
*InviteReturnHistory.java
*Created on 2015/5/25 9:07
*Copyright (c) 2015 北京火币天下网络技术有限公司. All Rights Reserved
*/
package com.caimao.bana.api.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 用户返积分记录表
 */
public class InviteReturnHistoryEntity implements Serializable {

    private static final long serialVersionUID = 2885902142564702894L;

    private Long id;

    private Long userId;

    private Long inviteUserId;

    private Integer returnType;

    private Long orderNo;

    private Long orderAmount;

    private BigDecimal returnRate;

    private BigDecimal returnAmount;

    private Long created;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getInviteUserId() {
        return inviteUserId;
    }

    public void setInviteUserId(Long inviteUserId) {
        this.inviteUserId = inviteUserId;
    }

    public Integer getReturnType() {
        return returnType;
    }

    public void setReturnType(Integer returnType) {
        this.returnType = returnType;
    }

    public Long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public Long getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Long orderAmount) {
        this.orderAmount = orderAmount;
    }

    public BigDecimal getReturnRate() {
        return returnRate;
    }

    public void setReturnRate(BigDecimal returnRate) {
        this.returnRate = returnRate;
    }

    public BigDecimal getReturnAmount() {
        return returnAmount;
    }

    public void setReturnAmount(BigDecimal returnAmount) {
        this.returnAmount = returnAmount;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }
}
