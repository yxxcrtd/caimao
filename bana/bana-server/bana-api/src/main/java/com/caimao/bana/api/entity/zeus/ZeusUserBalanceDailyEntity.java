package com.caimao.bana.api.entity.zeus;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户资产汇总日报
 */
public class ZeusUserBalanceDailyEntity implements Serializable {
    private Long availableAmount;
    private Long freezeAmount;
    private Long userCount;
    private Long loanInterestTotal;
    private Long loanTotal;
    private Long loanTotalRepay;
    private Long p2pInterestTotal;
    private Long p2pInterestTotalPay;
    private Long p2pInvestTotal;
    private Long p2pInvestTotalFail;
    private Long p2pInvestTotalSuccess;
    private Long p2pInvestTotalRepay;
    private Long depositTotal;
    private Long withdrawTotal;
    private Long loanBalance;
    private Date created;
    private Date createDatetime;

    public Long getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(Long availableAmount) {
        this.availableAmount = availableAmount;
    }

    public Long getFreezeAmount() {
        return freezeAmount;
    }

    public void setFreezeAmount(Long freezeAmount) {
        this.freezeAmount = freezeAmount;
    }

    public Long getUserCount() {
        return userCount;
    }

    public void setUserCount(Long userCount) {
        this.userCount = userCount;
    }

    public Long getLoanInterestTotal() {
        return loanInterestTotal;
    }

    public void setLoanInterestTotal(Long loanInterestTotal) {
        this.loanInterestTotal = loanInterestTotal;
    }

    public Long getLoanTotal() {
        return loanTotal;
    }

    public void setLoanTotal(Long loanTotal) {
        this.loanTotal = loanTotal;
    }

    public Long getLoanTotalRepay() {
        return loanTotalRepay;
    }

    public void setLoanTotalRepay(Long loanTotalRepay) {
        this.loanTotalRepay = loanTotalRepay;
    }

    public Long getP2pInterestTotal() {
        return p2pInterestTotal;
    }

    public void setP2pInterestTotal(Long p2pInterestTotal) {
        this.p2pInterestTotal = p2pInterestTotal;
    }

    public Long getP2pInterestTotalPay() {
        return p2pInterestTotalPay;
    }

    public void setP2pInterestTotalPay(Long p2pInterestTotalPay) {
        this.p2pInterestTotalPay = p2pInterestTotalPay;
    }

    public Long getP2pInvestTotal() {
        return p2pInvestTotal;
    }

    public void setP2pInvestTotal(Long p2pInvestTotal) {
        this.p2pInvestTotal = p2pInvestTotal;
    }

    public Long getP2pInvestTotalFail() {
        return p2pInvestTotalFail;
    }

    public void setP2pInvestTotalFail(Long p2pInvestTotalFail) {
        this.p2pInvestTotalFail = p2pInvestTotalFail;
    }

    public Long getP2pInvestTotalSuccess() {
        return p2pInvestTotalSuccess;
    }

    public void setP2pInvestTotalSuccess(Long p2pInvestTotalSuccess) {
        this.p2pInvestTotalSuccess = p2pInvestTotalSuccess;
    }

    public Long getP2pInvestTotalRepay() {
        return p2pInvestTotalRepay;
    }

    public void setP2pInvestTotalRepay(Long p2pInvestTotalRepay) {
        this.p2pInvestTotalRepay = p2pInvestTotalRepay;
    }

    public Long getDepositTotal() {
        return depositTotal;
    }

    public void setDepositTotal(Long depositTotal) {
        this.depositTotal = depositTotal;
    }

    public Long getWithdrawTotal() {
        return withdrawTotal;
    }

    public void setWithdrawTotal(Long withdrawTotal) {
        this.withdrawTotal = withdrawTotal;
    }

    public Long getLoanBalance() {
        return loanBalance;
    }

    public void setLoanBalance(Long loanBalance) {
        this.loanBalance = loanBalance;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }
}