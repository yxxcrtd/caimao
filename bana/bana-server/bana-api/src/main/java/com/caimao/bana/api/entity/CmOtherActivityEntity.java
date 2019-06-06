package com.caimao.bana.api.entity;

import java.io.Serializable;

/**
 * 活动，前三天免费的东东
 * Created by WangXu on 2015/5/13.
 */
public class CmOtherActivityEntity implements Serializable {
    private static final long serialVersionUID = -8377574655307080998L;
    private Long contractNo;
    private Long interestSettleDays;
    private Long userId;
    private String userRealName;
    private Long loanAmount;
    private Double interestRate;
    private Long fee;

    public Long getContractNo() {
        return contractNo;
    }

    public void setContractNo(Long contractNo) {
        this.contractNo = contractNo;
    }

    public Long getInterestSettleDays() {
        return interestSettleDays;
    }

    public void setInterestSettleDays(Long interestSettleDays) {
        this.interestSettleDays = interestSettleDays;
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

    public Long getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(Long loanAmount) {
        this.loanAmount = loanAmount;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    public Long getFee() {
        return fee;
    }

    public void setFee(Long fee) {
        this.fee = fee;
    }
}
