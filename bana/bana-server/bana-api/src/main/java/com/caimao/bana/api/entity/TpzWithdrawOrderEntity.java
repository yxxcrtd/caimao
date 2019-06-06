/*
*WithdrawOrder.java
*Created on 2015/5/7 16:55
*Copyright (c) 2015 北京火币天下网络技术有限公司. All Rights Reserved
*/
package com.caimao.bana.api.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Administrator
 * @version 1.0.1
 */
public class TpzWithdrawOrderEntity implements Serializable {
    private static final long serialVersionUID = 2631418291900180567L;
    private Long orderNo;
    private Long userId;
    private Long pzAccountId;
    private String orderName;
    private String orderStatus;
    private String bankNo;
    private String bankCode;
    private String bankCardName;
    private String bankCardNo;
    private String province;
    private String city;
    private String openBank;
    private String currencyType;
    private String bankResultCode;
    private String bankResultCodeNote;
    private String payId;
    private String payResultNote;
    private Date payResultDatetime;
    private String checkStatus;
    private String workDate;
    private Long orderAmount;
    private String orderAbstract;
    private Date bankSuccessDatetime;
    private Date paySubmitDatetime;
    private Long channelServiceCharge;
    private Date createDatetime;
    private Date createDatetimeEnd;
    private Date updateDatetime;
    private String remark;
    private String verifyUser;
    private Date verifyDatetime;
    private String verifyStatus;
    private Long channelId;
    private String bankAddress;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getOpenBank() {
        return openBank;
    }

    public void setOpenBank(String openBank) {
        this.openBank = openBank;
    }

    public Long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPzAccountId() {
        return pzAccountId;
    }

    public void setPzAccountId(Long pzAccountId) {
        this.pzAccountId = pzAccountId;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankCardName() {
        return bankCardName;
    }

    public void setBankCardName(String bankCardName) {
        this.bankCardName = bankCardName;
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public String getBankResultCode() {
        return bankResultCode;
    }

    public void setBankResultCode(String bankResultCode) {
        this.bankResultCode = bankResultCode;
    }

    public String getBankResultCodeNote() {
        return bankResultCodeNote;
    }

    public void setBankResultCodeNote(String bankResultCodeNote) {
        this.bankResultCodeNote = bankResultCodeNote;
    }

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    public String getPayResultNote() {
        return payResultNote;
    }

    public void setPayResultNote(String payResultNote) {
        this.payResultNote = payResultNote;
    }

    public Date getPayResultDatetime() {
        return payResultDatetime;
    }

    public void setPayResultDatetime(Date payResultDatetime) {
        this.payResultDatetime = payResultDatetime;
    }

    public String getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getWorkDate() {
        return workDate;
    }

    public void setWorkDate(String workDate) {
        this.workDate = workDate;
    }

    public Long getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Long orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getOrderAbstract() {
        return orderAbstract;
    }

    public void setOrderAbstract(String orderAbstract) {
        this.orderAbstract = orderAbstract;
    }

    public Date getBankSuccessDatetime() {
        return bankSuccessDatetime;
    }

    public void setBankSuccessDatetime(Date bankSuccessDatetime) {
        this.bankSuccessDatetime = bankSuccessDatetime;
    }

    public Date getPaySubmitDatetime() {
        return paySubmitDatetime;
    }

    public void setPaySubmitDatetime(Date paySubmitDatetime) {
        this.paySubmitDatetime = paySubmitDatetime;
    }

    public Long getChannelServiceCharge() {
        return channelServiceCharge;
    }

    public void setChannelServiceCharge(Long channelServiceCharge) {
        this.channelServiceCharge = channelServiceCharge;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public Date getCreateDatetimeEnd() {
        return createDatetimeEnd;
    }

    public void setCreateDatetimeEnd(Date createDatetimeEnd) {
        this.createDatetimeEnd = createDatetimeEnd;
    }

    public Date getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getVerifyUser() {
        return verifyUser;
    }

    public void setVerifyUser(String verifyUser) {
        this.verifyUser = verifyUser;
    }

    public Date getVerifyDatetime() {
        return verifyDatetime;
    }

    public void setVerifyDatetime(Date verifyDatetime) {
        this.verifyDatetime = verifyDatetime;
    }

    public String getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(String verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public String getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }
}
