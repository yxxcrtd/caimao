package com.caimao.bana.api.entity.res;

import java.util.Date;

public class F831106Res
{
  private Long pzAccountId;
  private Long userId;
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

  public String getAccountStatusReason()
  {
    return this.accountStatusReason;
  }

  public void setAccountStatusReason(String accountStatusReason) {
    this.accountStatusReason = accountStatusReason;
  }

  public Long getVersion() {
    return this.version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  public String getHash() {
    return this.hash;
  }

  public void setHash(String hash) {
    this.hash = hash;
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

  public String getCurrencyType() {
    return this.currencyType;
  }

  public void setCurrencyType(String currencyType) {
    this.currencyType = currencyType;
  }

  public Long getAvalaibleAmount() {
    return this.avalaibleAmount;
  }

  public void setAvalaibleAmount(Long avalaibleAmount) {
    this.avalaibleAmount = avalaibleAmount;
  }

  public Long getFreezeAmount() {
    return this.freezeAmount;
  }

  public void setFreezeAmount(Long freezeAmount) {
    this.freezeAmount = freezeAmount;
  }

  public String getAccountStatus() {
    return this.accountStatus;
  }

  public void setAccountStatus(String accountStatus) {
    this.accountStatus = accountStatus;
  }

  public String getMd5Code() {
    return this.md5Code;
  }

  public void setMd5Code(String md5Code) {
    this.md5Code = md5Code;
  }

  public String getRemark() {
    return this.remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
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
}