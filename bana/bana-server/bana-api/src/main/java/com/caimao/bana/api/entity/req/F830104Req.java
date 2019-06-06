package com.caimao.bana.api.entity.req;



public class F830104Req
{
  private Long userId;
  private Long pzAccountId;
  private String homsFundAccount;
  private String homsCombineId;
  private String accountBizType;
  private Long transAmount;
  private String seqFlag;
  private String remark;

  public Long getUserId()
  {
    return this.userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public long getPzAccountId() {
    return this.pzAccountId.longValue();
  }

  public void setPzAccountId(long pzAccountId) {
    this.pzAccountId = Long.valueOf(pzAccountId);
  }

  public String getHomsFundAccount() {
    return this.homsFundAccount;
  }

  public void setHomsFundAccount(String homsFundAccount) {
    this.homsFundAccount = homsFundAccount;
  }

  public String getHomsCombineId() {
    return this.homsCombineId;
  }

  public void setHomsCombineId(String homsCombineId) {
    this.homsCombineId = homsCombineId;
  }

  public String getAccountBizType() {
    return this.accountBizType;
  }

  public void setAccountBizType(String accountBizType) {
    this.accountBizType = accountBizType;
  }

  public Long getTransAmount() {
    return this.transAmount;
  }

  public void setTransAmount(Long transAmount) {
    this.transAmount = transAmount;
  }

  public String getSeqFlag() {
    return this.seqFlag;
  }

  public void setSeqFlag(String seqFlag) {
    this.seqFlag = seqFlag;
  }

  public String getRemark() {
    return this.remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }
}