package com.caimao.bana.api.entity.res;

import java.io.Serializable;

/**
 * Created by WangXu on 2015/5/15.
 */
public class F830216Res implements Serializable {
    private Long totalLoanAmount;
    private Long totalCashAmount;
    private Long totalRepayAmount;
    private Long totalSettledInterest;
    private Long totalAccruedInterest;

    public Long getTotalLoanAmount() {
        return totalLoanAmount;
    }

    public void setTotalLoanAmount(Long totalLoanAmount) {
        this.totalLoanAmount = totalLoanAmount;
    }

    public Long getTotalCashAmount() {
        return totalCashAmount;
    }

    public void setTotalCashAmount(Long totalCashAmount) {
        this.totalCashAmount = totalCashAmount;
    }

    public Long getTotalRepayAmount() {
        return totalRepayAmount;
    }

    public void setTotalRepayAmount(Long totalRepayAmount) {
        this.totalRepayAmount = totalRepayAmount;
    }

    public Long getTotalSettledInterest() {
        return totalSettledInterest;
    }

    public void setTotalSettledInterest(Long totalSettledInterest) {
        this.totalSettledInterest = totalSettledInterest;
    }

    public Long getTotalAccruedInterest() {
        return totalAccruedInterest;
    }

    public void setTotalAccruedInterest(Long totalAccruedInterest) {
        this.totalAccruedInterest = totalAccruedInterest;
    }
}
