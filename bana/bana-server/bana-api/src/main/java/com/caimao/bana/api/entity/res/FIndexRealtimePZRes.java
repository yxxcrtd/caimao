package com.caimao.bana.api.entity.res;

import java.io.Serializable;

/**
 * 首页实时动态的融资数据
 * Created by WangXu on 2015/5/28.
 */
public class FIndexRealtimePZRes implements Serializable {

    private String mobile;
    private Long loanAmount;
    private String applyDatetime;

    public String getApplyDatetime() {
        return applyDatetime;
    }

    public void setApplyDatetime(String applyDatetime) {
        this.applyDatetime = applyDatetime;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(Long loanAmount) {
        this.loanAmount = loanAmount;
    }
}
