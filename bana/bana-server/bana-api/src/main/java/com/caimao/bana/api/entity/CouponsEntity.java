package com.caimao.bana.api.entity;

import com.caimao.bana.api.enums.CouponStatus;

import java.io.Serializable;
import java.math.BigDecimal;

public class CouponsEntity implements Serializable {
    private static final long serialVersionUID = 8615614823750979662L;
    private Long id;
    private Long userId;
    private Long channelId;
    private BigDecimal money;
    private BigDecimal moneyUsed;
    private Integer amount;
    private Integer amountUsed;
    private CouponStatus status;
    private String packageData;
    private Long extId;
    private Integer endTime;
    private Integer created;

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

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public BigDecimal getMoneyUsed() {
        return moneyUsed;
    }

    public void setMoneyUsed(BigDecimal moneyUsed) {
        this.moneyUsed = moneyUsed;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getAmountUsed() {
        return amountUsed;
    }

    public void setAmountUsed(Integer amountUsed) {
        this.amountUsed = amountUsed;
    }

    public CouponStatus getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = CouponStatus.findByValue(status);
    }

    public String getPackageData() {
        return packageData;
    }

    public void setPackageData(String packageData) {
        this.packageData = packageData;
    }

    public Long getExtId() {
        return extId;
    }

    public void setExtId(Long extId) {
        this.extId = extId;
    }

    public Integer getEndTime() {
        return endTime;
    }

    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }

    public Integer getCreated() {
        return created;
    }

    public void setCreated(Integer created) {
        this.created = created;
    }
}