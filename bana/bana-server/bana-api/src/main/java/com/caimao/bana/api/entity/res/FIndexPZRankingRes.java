package com.caimao.bana.api.entity.res;

import java.io.Serializable;

/**
 * 首页融资排行返回对象
 * Created by WangXu on 2015/5/28.
 */
public class FIndexPZRankingRes implements Serializable {
    private String mobile;
    private Long cashAmount;
    private int loanRatio;
    private Long loanAmount;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(Long cashAmount) {
        this.cashAmount = cashAmount;
    }

    public int getLoanRatio() {
        return loanRatio;
    }

    public void setLoanRatio(int loanRatio) {
        this.loanRatio = loanRatio;
    }

    public Long getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(Long loanAmount) {
        this.loanAmount = loanAmount;
    }
}
