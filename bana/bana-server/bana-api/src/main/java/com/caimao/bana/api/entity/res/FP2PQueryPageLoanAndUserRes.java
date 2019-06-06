package com.caimao.bana.api.entity.res;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class FP2PQueryPageLoanAndUserRes implements Serializable {
    private static final long serialVersionUID = -5954427600244595164L;

    private Long targetId;
    private Long targetUserId;
    private String userName;
    private String mobile;
    private String targetName;
    private Long targetProdId;
    private Byte targetProdLever;
    private BigDecimal targetProdRate;
    private Integer liftTime;
    private Long targetAmount;
    private Long caimaoValue;
    private BigDecimal yearRate;
    private BigDecimal manageRate;
    private Long actualValue;
    private Integer investUserNum;
    private Long payMargin;
    private Long payTargetInterest;
    private Long payManageFee;
    private Long sentInterest;
    private Integer created;
    private Date targetFullDatetime;
    private Byte targetStatus;
    private Long contractId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public Byte getTargetProdLever() {
        return targetProdLever;
    }

    public void setTargetProdLever(Byte targetProdLever) {
        this.targetProdLever = targetProdLever;
    }

    public BigDecimal getTargetProdRate() {
        return targetProdRate;
    }

    public void setTargetProdRate(BigDecimal targetProdRate) {
        this.targetProdRate = targetProdRate;
    }

    public Integer getLiftTime() {
        return liftTime;
    }

    public void setLiftTime(Integer liftTime) {
        this.liftTime = liftTime;
    }

    public Long getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(Long targetAmount) {
        this.targetAmount = targetAmount;
    }

    public Long getCaimaoValue() {
        return caimaoValue;
    }

    public void setCaimaoValue(Long caimaoValue) {
        this.caimaoValue = caimaoValue;
    }

    public BigDecimal getYearRate() {
        return yearRate;
    }

    public void setYearRate(BigDecimal yearRate) {
        this.yearRate = yearRate;
    }

    public BigDecimal getManageRate() {
        return manageRate;
    }

    public void setManageRate(BigDecimal manageRate) {
        this.manageRate = manageRate;
    }

    public Long getActualValue() {
        return actualValue;
    }

    public void setActualValue(Long actualValue) {
        this.actualValue = actualValue;
    }

    public Integer getInvestUserNum() {
        return investUserNum;
    }

    public void setInvestUserNum(Integer investUserNum) {
        this.investUserNum = investUserNum;
    }

    public Long getPayMargin() {
        return payMargin;
    }

    public void setPayMargin(Long payMargin) {
        this.payMargin = payMargin;
    }

    public Long getPayTargetInterest() {
        return payTargetInterest;
    }

    public void setPayTargetInterest(Long payTargetInterest) {
        this.payTargetInterest = payTargetInterest;
    }

    public Long getSentInterest() {
        return sentInterest;
    }

    public void setSentInterest(Long sentInterest) {
        this.sentInterest = sentInterest;
    }

    public Integer getCreated() {
        return created;
    }

    public void setCreated(Integer created) {
        this.created = created;
    }

    public Date getTargetFullDatetime() {
        return targetFullDatetime;
    }

    public void setTargetFullDatetime(Date targetFullDatetime) {
        this.targetFullDatetime = targetFullDatetime;
    }

    public Byte getTargetStatus() {
        return targetStatus;
    }

    public void setTargetStatus(Byte targetStatus) {
        this.targetStatus = targetStatus;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public BigDecimal getTargetRate() {
        return new BigDecimal(this.actualValue / (this.targetAmount - this.caimaoValue) * 100).setScale(2, BigDecimal.ROUND_DOWN);
    }

    public Long getTargetOver() {
        return this.targetAmount - this.caimaoValue - this.actualValue;
    }
    
    public Long getPayManageFee() {
        return payManageFee;
    }

    public void setPayManageFee(Long payManageFee) {
        this.payManageFee = payManageFee;
    }
}

