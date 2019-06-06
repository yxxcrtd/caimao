/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. All Rights Reserved
 */
package com.caimao.bana.api.entity.res;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zxd $Id$
 * 
 */
public class FP2PQueryLoanPageInvestWithUserRes implements Serializable {
    private static final long serialVersionUID = -7823500147648373801L;

    private Long investId;

    private Long investUserId;

    private Long targetId;
    
    private String userName;
    
    private String mobile;

    private Long targetUserId;

    private String targetName;

    private Long targetProdId;

    private Integer liftTime;

    private Long investValue;

    private BigDecimal yearRate;

    private Long yearValue;

    private Integer interestPeriod;

    private Long payInterest;

    private Date investCreated;

    private Date fullDatetime;

    private Date expirationDatetime;

    private Date interestDatetime;

    private Byte investStatus;

    private Boolean isAdmin = true;

    public Long getInvestId() {
        return investId;
    }

    public void setInvestId(Long investId) {
        this.investId = investId;
    }

    public Long getInvestUserId() {
        return investUserId;
    }

    public void setInvestUserId(Long investUserId) {
        this.investUserId = investUserId;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public Long getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(Long targetUserId) {
        this.targetUserId = targetUserId;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public Long getTargetProdId() {
        return targetProdId;
    }

    public void setTargetProdId(Long targetProdId) {
        this.targetProdId = targetProdId;
    }

    public Integer getLiftTime() {
        return liftTime;
    }

    public void setLiftTime(Integer liftTime) {
        this.liftTime = liftTime;
    }

    public Long getInvestValue() {
        return investValue;
    }

    public void setInvestValue(Long investValue) {
        this.investValue = investValue;
    }

    public BigDecimal getYearRate() {
        return yearRate;
    }

    public void setYearRate(BigDecimal yearRate) {
        this.yearRate = yearRate;
    }

    public Long getYearValue() {
        return yearValue;
    }

    public void setYearValue(Long yearValue) {
        this.yearValue = yearValue;
    }

    public Integer getInterestPeriod() {
        return interestPeriod;
    }

    public void setInterestPeriod(Integer interestPeriod) {
        this.interestPeriod = interestPeriod;
    }

    public Long getPayInterest() {
        return payInterest;
    }

    public void setPayInterest(Long payInterest) {
        this.payInterest = payInterest;
    }

    public Date getInvestCreated() {
        return investCreated;
    }

    public void setInvestCreated(Date investCreated) {
        this.investCreated = investCreated;
    }

    public Date getFullDatetime() {
        return fullDatetime;
    }

    public void setFullDatetime(Date fullDatetime) {
        this.fullDatetime = fullDatetime;
    }

    public Date getExpirationDatetime() {
        return expirationDatetime;
    }

    public void setExpirationDatetime(Date expirationDatetime) {
        this.expirationDatetime = expirationDatetime;
    }

    public Date getInterestDatetime() {
        return interestDatetime;
    }

    public void setInterestDatetime(Date interestDatetime) {
        this.interestDatetime = interestDatetime;
    }

    public Byte getInvestStatus() {
        return investStatus;
    }

    public void setInvestStatus(Byte investStatus) {
        this.investStatus = investStatus;
    }
    
    public String getUserName() {
        if(!this.isAdmin){
            return "";
        }else{
            return userName;
        }
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        if(!this.isAdmin){
            return "";
        }else{
            return mobile;
        }
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

	public String getMobileHide() {
        if (StringUtils.isNotBlank(this.mobile) && this.mobile.length() >= 6) {
            int length = this.mobile.length();
            String pre = this.mobile.substring(0, 3);
            String last = this.mobile.substring(length - 3, length);
            StringBuffer sb = new StringBuffer(pre);
            for (int i = 0; i < length - 6; ++i) {
                sb.append("*");
            }
            sb.append(last);
            return sb.toString();
        } else {
            return null;
        }
    }

    public Boolean isAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}
