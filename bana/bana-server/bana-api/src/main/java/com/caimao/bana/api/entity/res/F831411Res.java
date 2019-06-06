/*
*F831411Res.java
*Created on 2015/5/16 11:17
*Copyright (c) 2015 北京火币天下网络技术有限公司. All Rights Reserved
*/
package com.caimao.bana.api.entity.res;

import java.io.Serializable;

/**
 * 查询取现列表
 *
 * @author rpw
 * @version 1.0.1
 */
public class F831411Res implements Serializable {

    private static final long serialVersionUID = 2915963397021148699L;
    private Long userId;
    private String payId;
    private String userRealName;
    private Long channelServiceCharge;
    private String payResultDatetime;
    private String payResultNote;
    private String remark;
    private String verifyUser;
    private String verifyDatetime;
    private String verifyStatus;
    private String createDateTime;
    private String bankAddress;
    private String bankName;
    private Long orderNo;
    private Long pzAccountId;
    private String orderStatus;
    private String bankNo;
    private String bankCode;
    private String bankCardName;
    private String bankCardNo;
    private String currencyType;
    private Long orderAmount;
    private String orderAbstract;
    private String paySubmitDatetime;
    private String province;
    private String city;
    private String openBank;
    private Long totalDeposit;
    private Long totalWithdraw;
    private Long totalBad;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    public String getUserRealName() {
        return userRealName;
    }

    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }

    public Long getChannelServiceCharge() {
        return channelServiceCharge;
    }

    public void setChannelServiceCharge(Long channelServiceCharge) {
        this.channelServiceCharge = channelServiceCharge;
    }

    public String getPayResultDatetime() {
        return payResultDatetime;
    }

    public void setPayResultDatetime(String payResultDatetime) {
        this.payResultDatetime = payResultDatetime;
    }

    public String getPayResultNote() {
        return payResultNote;
    }

    public void setPayResultNote(String payResultNote) {
        this.payResultNote = payResultNote;
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

    public String getVerifyDatetime() {
        return verifyDatetime;
    }

    public void setVerifyDatetime(String verifyDatetime) {
        this.verifyDatetime = verifyDatetime;
    }

    public String getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(String verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    public String getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(String createDateTime) {
        this.createDateTime = createDateTime;
    }

    public String getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public Long getPzAccountId() {
        return pzAccountId;
    }

    public void setPzAccountId(Long pzAccountId) {
        this.pzAccountId = pzAccountId;
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

    public String getPaySubmitDatetime() {
        return paySubmitDatetime;
    }

    public void setPaySubmitDatetime(String paySubmitDatetime) {
        this.paySubmitDatetime = paySubmitDatetime;
    }

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

    public Long getTotalDeposit() {
        return totalDeposit;
    }

    public void setTotalDeposit(Long totalDeposit) {
        this.totalDeposit = totalDeposit;
    }

    public Long getTotalWithdraw() {
        return totalWithdraw;
    }

    public void setTotalWithdraw(Long totalWithdraw) {
        this.totalWithdraw = totalWithdraw;
    }

    public Long getTotalBad() {
        return totalBad;
    }

    public void setTotalBad(Long totalBad) {
        this.totalBad = totalBad;
    }
}
