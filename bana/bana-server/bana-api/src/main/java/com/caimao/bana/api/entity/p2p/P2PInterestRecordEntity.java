package com.caimao.bana.api.entity.p2p;

import java.io.Serializable;
import java.util.Date;

public class P2PInterestRecordEntity implements Serializable {
    private Long id;

    private Long investId;

    private Long investUserId;

    private Long targetId;

    private Long targetUserId;
    //第几次结息
    private int interestTimes;

    private Date interestDate;

    private Long interestValue;

    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getInvestId() {
        return investId;
    }

    public void setInvestId(Long investId) {
        this.investId = investId;
    }

    public Long getInvestUserId() {
        return investUserId;
    }

    public void setInvestUserId(Long investUserId) {
        this.investUserId = investUserId;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public Long getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(Long targetUserId) {
        this.targetUserId = targetUserId;
    }

    public Long getInterestValue() {
        return interestValue;
    }

    public void setInterestValue(Long interestValue) {
        this.interestValue = interestValue;
    }

    public Date getInterestDate() {
        return interestDate;
    }

    public void setInterestDate(Date interestDate) {
        this.interestDate = interestDate;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getInterestTimes() {
        return interestTimes;
    }

    public void setInterestTimes(int interestTime) {
        this.interestTimes = interestTime;
    }
    
}