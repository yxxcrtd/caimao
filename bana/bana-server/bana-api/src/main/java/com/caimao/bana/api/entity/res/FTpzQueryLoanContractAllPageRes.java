package com.caimao.bana.api.entity.res;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 融资合约表
 * Created by WangXu on 2015/6/23.
 */
public class FTpzQueryLoanContractAllPageRes implements Serializable{
    private Long contractNo;
    private Long pzAccountId;
    private String contractType;
    private Long userId;
    private String userRealName;
    private String contractBeginDate;
    private String contractEndDate;
    private String beginInterestDate;
    private Long cashAmount;
    private Integer loanRatio;
    private BigDecimal interestRate;
    private String interestSettleMode;
    private String interestAccrualMode;
    private Long loanAmount;
    private Long repayAmount;
    private Date contractSignDatetime;
    private Date contractStopDatetime;
    private String contractStatus;
    private Long settledInterest;
    private Long accruedInterest;
    private String lastSettleInterestDate;
    private String nextSettleInterestDate;
    private String counterpartIdcard;
    private String counterpartName;
    private String counterpartFundAccount;
    private Long counterpartUserId;
    private Long relContractNo;
    private Long prodId;
    private String idcard;
    private String remark;
    private Date createDatetime;
    private Date updateDatetime;
    private Long fee;
    private String prodBillType;
    private Integer interestSettleDays;
    private Integer prodTerm;
    private String homsFundAccount;
    private String homsCombineId;
    private String prodTypeId;
    private Date repayDatetime;
    private Long interestShould;
    private Long interestAlready;

    public Long getContractNo() {
        return contractNo;
    }

    public void setContractNo(Long contractNo) {
        this.contractNo = contractNo;
    }

    public Long getPzAccountId() {
        return pzAccountId;
    }

    public void setPzAccountId(Long pzAccountId) {
        this.pzAccountId = pzAccountId;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserRealName() {
        return userRealName;
    }

    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }

    public String getContractBeginDate() {
        return contractBeginDate;
    }

    public void setContractBeginDate(String contractBeginDate) {
        this.contractBeginDate = contractBeginDate;
    }

    public String getContractEndDate() {
        return contractEndDate;
    }

    public void setContractEndDate(String contractEndDate) {
        this.contractEndDate = contractEndDate;
    }

    public String getBeginInterestDate() {
        return beginInterestDate;
    }

    public void setBeginInterestDate(String beginInterestDate) {
        this.beginInterestDate = beginInterestDate;
    }

    public Long getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(Long cashAmount) {
        this.cashAmount = cashAmount;
    }

    public Integer getLoanRatio() {
        return loanRatio;
    }

    public void setLoanRatio(Integer loanRatio) {
        this.loanRatio = loanRatio;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public String getInterestSettleMode() {
        return interestSettleMode;
    }

    public void setInterestSettleMode(String interestSettleMode) {
        this.interestSettleMode = interestSettleMode;
    }

    public String getInterestAccrualMode() {
        return interestAccrualMode;
    }

    public void setInterestAccrualMode(String interestAccrualMode) {
        this.interestAccrualMode = interestAccrualMode;
    }

    public Long getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(Long loanAmount) {
        this.loanAmount = loanAmount;
    }

    public Long getRepayAmount() {
        return repayAmount;
    }

    public void setRepayAmount(Long repayAmount) {
        this.repayAmount = repayAmount;
    }

    public Date getContractSignDatetime() {
        return contractSignDatetime;
    }

    public void setContractSignDatetime(Date contractSignDatetime) {
        this.contractSignDatetime = contractSignDatetime;
    }

    public Date getContractStopDatetime() {
        return contractStopDatetime;
    }

    public void setContractStopDatetime(Date contractStopDatetime) {
        this.contractStopDatetime = contractStopDatetime;
    }

    public String getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(String contractStatus) {
        this.contractStatus = contractStatus;
    }

    public Long getSettledInterest() {
        return settledInterest;
    }

    public void setSettledInterest(Long settledInterest) {
        this.settledInterest = settledInterest;
    }

    public Long getAccruedInterest() {
        return accruedInterest;
    }

    public void setAccruedInterest(Long accruedInterest) {
        this.accruedInterest = accruedInterest;
    }

    public String getLastSettleInterestDate() {
        return lastSettleInterestDate;
    }

    public void setLastSettleInterestDate(String lastSettleInterestDate) {
        this.lastSettleInterestDate = lastSettleInterestDate;
    }

    public String getNextSettleInterestDate() {
        return nextSettleInterestDate;
    }

    public void setNextSettleInterestDate(String nextSettleInterestDate) {
        this.nextSettleInterestDate = nextSettleInterestDate;
    }

    public String getCounterpartIdcard() {
        return counterpartIdcard;
    }

    public void setCounterpartIdcard(String counterpartIdcard) {
        this.counterpartIdcard = counterpartIdcard;
    }

    public String getCounterpartName() {
        return counterpartName;
    }

    public void setCounterpartName(String counterpartName) {
        this.counterpartName = counterpartName;
    }

    public String getCounterpartFundAccount() {
        return counterpartFundAccount;
    }

    public void setCounterpartFundAccount(String counterpartFundAccount) {
        this.counterpartFundAccount = counterpartFundAccount;
    }

    public Long getCounterpartUserId() {
        return counterpartUserId;
    }

    public void setCounterpartUserId(Long counterpartUserId) {
        this.counterpartUserId = counterpartUserId;
    }

    public Long getRelContractNo() {
        return relContractNo;
    }

    public void setRelContractNo(Long relContractNo) {
        this.relContractNo = relContractNo;
    }

    public Long getProdId() {
        return prodId;
    }

    public void setProdId(Long prodId) {
        this.prodId = prodId;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public Date getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    public Long getFee() {
        return fee;
    }

    public void setFee(Long fee) {
        this.fee = fee;
    }

    public String getProdBillType() {
        return prodBillType;
    }

    public void setProdBillType(String prodBillType) {
        this.prodBillType = prodBillType;
    }

    public Integer getInterestSettleDays() {
        return interestSettleDays;
    }

    public void setInterestSettleDays(Integer interestSettleDays) {
        this.interestSettleDays = interestSettleDays;
    }

    public Integer getProdTerm() {
        return prodTerm;
    }

    public void setProdTerm(Integer prodTerm) {
        this.prodTerm = prodTerm;
    }

    public String getHomsFundAccount() {
        return homsFundAccount;
    }

    public void setHomsFundAccount(String homsFundAccount) {
        this.homsFundAccount = homsFundAccount;
    }

    public String getHomsCombineId() {
        return homsCombineId;
    }

    public void setHomsCombineId(String homsCombineId) {
        this.homsCombineId = homsCombineId;
    }

    public String getProdTypeId() {
        return prodTypeId;
    }

    public void setProdTypeId(String prodTypeId) {
        this.prodTypeId = prodTypeId;
    }

    public Date getRepayDatetime() {
        return repayDatetime;
    }

    public void setRepayDatetime(Date repayDatetime) {
        this.repayDatetime = repayDatetime;
    }

    public Long getInterestShould() {
        return interestShould;
    }

    public void setInterestShould(Long interestShould) {
        this.interestShould = interestShould;
    }

    public Long getInterestAlready() {
        return interestAlready;
    }

    public void setInterestAlready(Long interestAlready) {
        this.interestAlready = interestAlready;
    }
}
