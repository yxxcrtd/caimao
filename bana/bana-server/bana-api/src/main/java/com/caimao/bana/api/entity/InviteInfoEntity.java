package com.caimao.bana.api.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 用户返佣信息表
 */
public class InviteInfoEntity implements Serializable{
    private static final long serialVersionUID = -1054498999966436080L;
    private Long userId;
    private Integer regCnt;
    private Integer pzCnt;
    private Long pzAmount;
    private Long interestAmount;
    private Long commissionLevel;
    private BigDecimal commissionRate;
    private Long commissionTotal;
    private Long inviteTotal;
    private Long created;
    private Long updated;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getRegCnt() {
        return regCnt;
    }

    public void setRegCnt(Integer regCnt) {
        this.regCnt = regCnt;
    }

    public Integer getPzCnt() {
        return pzCnt;
    }

    public void setPzCnt(Integer pzCnt) {
        this.pzCnt = pzCnt;
    }

    public Long getPzAmount() {
        return pzAmount;
    }

    public void setPzAmount(Long pzAmount) {
        this.pzAmount = pzAmount;
    }

    public Long getInterestAmount() {
        return interestAmount;
    }

    public void setInterestAmount(Long interestAmount) {
        this.interestAmount = interestAmount;
    }

    public Long getCommissionLevel() {
        return commissionLevel;
    }

    public void setCommissionLevel(Long commissionLevel) {
        this.commissionLevel = commissionLevel;
    }

    public BigDecimal getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(BigDecimal commissionRate) {
        this.commissionRate = commissionRate;
    }

    public Long getCommissionTotal() {
        return commissionTotal;
    }

    public void setCommissionTotal(Long commissionTotal) {
        this.commissionTotal = commissionTotal;
    }

    public Long getInviteTotal() {
        return inviteTotal;
    }

    public void setInviteTotal(Long inviteTotal) {
        this.inviteTotal = inviteTotal;
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