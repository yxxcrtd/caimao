package com.caimao.bana.api.entity.res;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class FP2PQueryUserPageLoanRes implements Serializable {
    private Long targetId;

    private Long targetUserId;

    private String targetName;

    private Long targetProdId;

    private Byte targetProdLever;

    private BigDecimal targetProdRate;

    private Integer liftTime;

    private Long targetAmount;

    private Long caimaoValue;

    private BigDecimal yearRate;

    private Long actualValue;

    private Integer investUserNum;

    private Long payMargin;

    private Long payPzInterest;

    private Long payTargetInterest;

    private Long payManageFee;

    private Long sentInterest;

    private Date created;

    private Date targetFullDatetime;

    private Byte targetStatus;

    private Long contractId;

    private Long targetLiftTime;

    private Long targetOver;

    private String targetRate;

    private String customTitle;
    private String customContent;
    private String customMobile;
    private String customUsername;
    private Integer p2pType;


    public String getCustomTitle() {
        return customTitle;
    }

    public void setCustomTitle(String customTitle) {
        this.customTitle = customTitle;
    }

    public String getCustomContent() {
        return customContent;
    }

    public void setCustomContent(String customContent) {
        this.customContent = customContent;
    }

    public String getCustomMobile() {
        return customMobile;
    }

    public void setCustomMobile(String customMobile) {
        this.customMobile = customMobile;
    }

    public String getCustomUsername() {
        return customUsername;
    }

    public void setCustomUsername(String customUsername) {
        this.customUsername = customUsername;
    }

    public Integer getP2pType() {
        return p2pType;
    }

    public void setP2pType(Integer p2pType) {
        this.p2pType = p2pType;
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

    public Long getPayPzInterest() {
        return payPzInterest;
    }

    public void setPayPzInterest(Long payPzInterest) {
        this.payPzInterest = payPzInterest;
    }

    public Long getPayTargetInterest() {
        return payTargetInterest;
    }

    public void setPayTargetInterest(Long payTargetInterest) {
        this.payTargetInterest = payTargetInterest;
    }

    public Long getPayManageFee() {
        return payManageFee;
    }

    public void setPayManageFee(Long payManageFee) {
        this.payManageFee = payManageFee;
    }

    public Long getSentInterest() {
        return sentInterest;
    }

    public void setSentInterest(Long sentInterest) {
        this.sentInterest = sentInterest;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
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

    public void setTargetLiftTime(Long targetLiftTime) {
        this.targetLiftTime = targetLiftTime;
    }

    public Long getTargetLiftTime() {
        return targetLiftTime;
    }

    public Long getTargetOver() {
        return targetOver;
    }

    public void setTargetOver(Long targetOver) {
        this.targetOver = targetOver;
    }

    public String getTargetRate() {
        return targetRate;
    }

    public void setTargetRate(String targetRate) {
        this.targetRate = targetRate;
    }
}
