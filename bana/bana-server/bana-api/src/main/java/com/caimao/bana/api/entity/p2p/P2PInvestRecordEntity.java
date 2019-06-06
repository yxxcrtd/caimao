package com.caimao.bana.api.entity.p2p;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class P2PInvestRecordEntity implements Serializable{
    private Long investId;

    private Long investUserId;

    private Long targetId;

    private Long targetUserId;

    private String targetName;

    private Long targetProdId;

    private Integer liftTime;

    private Long investValue;

    private BigDecimal yearRate;

    private Long yearValue;

    private Integer interestPeriod;
  //第几次结息
    private int interestTimes;

    private Long payInterest;

    private Date investCreated;

    private Date fullDatetime;

    private Date expirationDatetime;

    private Date interestDatetime;

    private Byte investStatus;

    private Date lastUpdate;

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

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public Long getTargetProdId() {
        return targetProdId;
    }

    public void setTargetProdId(Long targetProdId) {
        this.targetProdId = targetProdId;
    }

    public Integer getLiftTime() {
        return liftTime;
    }

    public void setLiftTime(Integer liftTime) {
        this.liftTime = liftTime;
    }

    public Long getInvestValue() {
        return investValue;
    }

    public void setInvestValue(Long investValue) {
        this.investValue = investValue;
    }

    public BigDecimal getYearRate() {
        return yearRate;
    }

    public void setYearRate(BigDecimal yearRate) {
        this.yearRate = yearRate;
    }

    public Long getYearValue() {
        return yearValue;
    }

    public void setYearValue(Long yearValue) {
        this.yearValue = yearValue;
    }

    public Integer getInterestPeriod() {
        return interestPeriod;
    }

    public void setInterestPeriod(Integer interestPeriod) {
        this.interestPeriod = interestPeriod;
    }

    public Long getPayInterest() {
        return payInterest;
    }

    public void setPayInterest(Long payInterest) {
        this.payInterest = payInterest;
    }

    public Date getInvestCreated() {
        return investCreated;
    }

    public void setInvestCreated(Date investCreated) {
        this.investCreated = investCreated;
    }

    public Date getFullDatetime() {
        return fullDatetime;
    }

    public void setFullDatetime(Date fullDatetime) {
        this.fullDatetime = fullDatetime;
    }

    public Date getExpirationDatetime() {
        return expirationDatetime;
    }

    public void setExpirationDatetime(Date expirationDatetime) {
        this.expirationDatetime = expirationDatetime;
    }

    public Date getInterestDatetime() {
        return interestDatetime;
    }

    public void setInterestDatetime(Date interestDatetime) {
        this.interestDatetime = interestDatetime;
    }

    public Byte getInvestStatus() {
        return investStatus;
    }

    public void setInvestStatus(Byte investStatus) {
        this.investStatus = investStatus;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public int getInterestTimes() {
        return interestTimes;
    }

    public void setInterestTimes(int interestTime) {
        this.interestTimes = interestTime;
    }
}