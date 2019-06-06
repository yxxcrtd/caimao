package com.caimao.bana.api.entity.p2p;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class P2PLoanRecordEntity  implements Serializable {
    private Long targetId;
    private Long targetUserId;
    private String targetName;
    private Long targetProdId;
    private Byte targetProdLever;
    private BigDecimal targetProdRate;
    private int liftTime;
    private Long targetAmount;
    private Long caimaoValue;
    private BigDecimal yearRate;
    private BigDecimal manageRate;
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
    private Date lastUpdate;
    private String mobile;
    private String LoanUserName;
    private String isTrust;
    private Long targetOver;
    private String targetRate;
    private Integer interestSettleDays;
    private Byte isExt;
    private Long extTargetId;
    private Byte isRepayment;
    private Long contractNo;
    private Long ownerTargetId;

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

    public Integer getInterestSettleDays() {
        return interestSettleDays;
    }

    public void setInterestSettleDays(Integer interestSettleDays) {
        this.interestSettleDays = interestSettleDays;
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

    public int getLiftTime() {
        return liftTime;
    }

    public void setLiftTime(int liftTime) {
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

    public Long getTargetLiftTime() {
        return targetLiftTime;
    }

    public void setTargetLiftTime(Long targetLiftTime) {
        this.targetLiftTime = targetLiftTime;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLoanUserName() {
        return LoanUserName;
    }

    public void setLoanUserName(String loanUserName) {
        LoanUserName = loanUserName;
    }

    public String getIsTrust() {
        return isTrust;
    }

    public void setIsTrust(String isTrust) {
        this.isTrust = isTrust;
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

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Byte getIsExt() {
        return isExt;
    }

    public void setIsExt(Byte isExt) {
        this.isExt = isExt;
    }

    public Long getExtTargetId() {
        return extTargetId;
    }

    public void setExtTargetId(Long extTargetId) {
        this.extTargetId = extTargetId;
    }

    public Byte getIsRepayment() {
        return isRepayment;
    }

    public void setIsRepayment(Byte isRepayment) {
        this.isRepayment = isRepayment;
    }

    public Long getContractNo() {
        return contractNo;
    }

    public void setContractNo(Long contractNo) {
        this.contractNo = contractNo;
    }

    public Long getOwnerTargetId() {
        return ownerTargetId;
    }

    public void setOwnerTargetId(Long ownerTargetId) {
        this.ownerTargetId = ownerTargetId;
    }
}