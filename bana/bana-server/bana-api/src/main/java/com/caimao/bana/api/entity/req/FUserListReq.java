package com.caimao.bana.api.entity.req;

import com.caimao.bana.api.entity.QueryBase;
import com.caimao.bana.api.entity.res.FUserListRes;

import java.io.Serializable;

/**
 * 用户列表
 * Created by WangXu on 2015/5/27.
 */
public class FUserListReq extends QueryBase<FUserListRes> implements Serializable {
    private Long userId;
    private String userRealName;
    private String mobile;
    private String withdrawStatus;

    // 以下为贵金属统计用
    private String startDate;
    private String endDate;
    private String exchange;
    private String status1;
    private String refUserId;
    private String status2;
    private String bankId;
    private String e; // 用户统计的交易所标识条件
    private String changeType; // 交易变动类型：A入金；B出金；C质押；D解除质押；E银行入金；F银行出金

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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getWithdrawStatus() {
        return withdrawStatus;
    }

    public void setWithdrawStatus(String withdrawStatus) {
        this.withdrawStatus = withdrawStatus;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getStatus1() {
        return status1;
    }

    public void setStatus1(String status1) {
        this.status1 = status1;
    }

    public String getRefUserId() {
        return refUserId;
    }

    public void setRefUserId(String refUserId) {
        this.refUserId = refUserId;
    }

    public String getStatus2() {
        return status2;
    }

    public void setStatus2(String status2) {
        this.status2 = status2;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }
}
