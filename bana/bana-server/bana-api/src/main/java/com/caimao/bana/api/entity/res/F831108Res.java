package com.caimao.bana.api.entity.res;

import java.util.Date;

public class F831108Res {
    private Long orderNo;
    private Long pzAccountId;
    private Long userId;
    private String userRealName;
    private Long orderAmount;
    private String orderAbstract;
    private String operUser;
    private String verifyUser;
    private String remark;
    private Date verifyDatetime;
    private String verifyStatus;
    private Date createDatetime;
    private Date updateDatetime;
    private String seqFlag;
    private String accountBizType;

    public String getAccountBizType() {
        return this.accountBizType;
    }

    public void setAccountBizType(String accountBizType) {
        this.accountBizType = accountBizType;
    }

    public Long getOrderNo() {
        return this.orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public Long getPzAccountId() {
        return this.pzAccountId;
    }

    public void setPzAccountId(Long pzAccountId) {
        this.pzAccountId = pzAccountId;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserRealName() {
        return this.userRealName;
    }

    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }

    public Long getOrderAmount() {
        return this.orderAmount;
    }

    public void setOrderAmount(Long orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getOrderAbstract() {
        return this.orderAbstract;
    }

    public void setOrderAbstract(String orderAbstract) {
        this.orderAbstract = orderAbstract;
    }

    public String getOperUser() {
        return this.operUser;
    }

    public void setOperUser(String operUser) {
        this.operUser = operUser;
    }

    public String getVerifyUser() {
        return this.verifyUser;
    }

    public void setVerifyUser(String verifyUser) {
        this.verifyUser = verifyUser;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getVerifyDatetime() {
        return this.verifyDatetime;
    }

    public void setVerifyDatetime(Date verifyDatetime) {
        this.verifyDatetime = verifyDatetime;
    }

    public String getVerifyStatus() {
        return this.verifyStatus;
    }

    public void setVerifyStatus(String verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    public Date getCreateDatetime() {
        return this.createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public Date getUpdateDatetime() {
        return this.updateDatetime;
    }

    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    public String getSeqFlag() {
        return this.seqFlag;
    }

    public void setSeqFlag(String seqFlag) {
        this.seqFlag = seqFlag;
    }
}
