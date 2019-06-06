/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. 
 *All Rights Reserved
 */
package com.caimao.bana.api.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class TpzHomsAccountEntity implements Serializable {
    private Long id;
    private String brokerId;
    private String fundAccount;
    private String homsFundAccount;
    private String homsManageId;
    private String fundAccountName;
    private String expireDate;
    private BigDecimal fundPriority;
    private String accountStatus;
    private Long fundUserId;
    private Date createDatetime;
    private Date updateDatetime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrokerId() {
        return brokerId;
    }

    public void setBrokerId(String brokerId) {
        this.brokerId = brokerId;
    }

    public String getFundAccount() {
        return fundAccount;
    }

    public void setFundAccount(String fundAccount) {
        this.fundAccount = fundAccount;
    }

    public String getHomsFundAccount() {
        return homsFundAccount;
    }

    public void setHomsFundAccount(String homsFundAccount) {
        this.homsFundAccount = homsFundAccount;
    }

    public String getHomsManageId() {
        return homsManageId;
    }

    public void setHomsManageId(String homsManageId) {
        this.homsManageId = homsManageId;
    }

    public String getFundAccountName() {
        return fundAccountName;
    }

    public void setFundAccountName(String fundAccountName) {
        this.fundAccountName = fundAccountName;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public BigDecimal getFundPriority() {
        return fundPriority;
    }

    public void setFundPriority(BigDecimal fundPriority) {
        this.fundPriority = fundPriority;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public Long getFundUserId() {
        return fundUserId;
    }

    public void setFundUserId(Long fundUserId) {
        this.fundUserId = fundUserId;
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
}