package com.caimao.bana.api.entity;


import java.io.Serializable;
import java.util.Date;

/**
 * 用户资产表
 * Created by WangXu on 2015/4/23.
 */
public class TpzAccountEntity implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1633825692226715919L;
    private long pzAccountId;
    private long userId;
    private String userRealName;
    private String currencyType;
    private Long avalaibleAmount;
    private Long freezeAmount;
    private String accountStatus;
    private String md5Code;
    private String remark;
    private Date createDatetime;
    private Date updateDatetime;
    private Long version;
    private String hash;
    private String accountStatusReason;

    public long getPzAccountId() {
        return pzAccountId;
    }

    public void setPzAccountId(long pzAccountId) {
        this.pzAccountId = pzAccountId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserRealName() {
        return userRealName;
    }

    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public Long getAvalaibleAmount() {
        return avalaibleAmount;
    }

    public void setAvalaibleAmount(Long avalaibleAmount) {
        this.avalaibleAmount = avalaibleAmount;
    }

    public Long getFreezeAmount() {
        return freezeAmount;
    }

    public void setFreezeAmount(Long freezeAmount) {
        this.freezeAmount = freezeAmount;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getMd5Code() {
        return md5Code;
    }

    public void setMd5Code(String md5Code) {
        this.md5Code = md5Code;
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

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getAccountStatusReason() {
        return accountStatusReason;
    }

    public void setAccountStatusReason(String accountStatusReason) {
        this.accountStatusReason = accountStatusReason;
    }


}
