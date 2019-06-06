package com.caimao.bana.api.entity.res;

import java.io.Serializable;

/**
 * 前台查询用户充值记录的返回值对象
 * Created by WangXu on 2015/5/29.
 */
public class FAccountQueryChargeOrderRes implements Serializable {
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
    private Long channelId;
    private String createDatetime;
    private String paySubmitDatetime;
    private Long channelServiceCharge;

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

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public String getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(String createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getPaySubmitDatetime() {
        return paySubmitDatetime;
    }

    public void setPaySubmitDatetime(String paySubmitDatetime) {
        this.paySubmitDatetime = paySubmitDatetime;
    }

    public Long getChannelServiceCharge() {
        return channelServiceCharge;
    }

    public void setChannelServiceCharge(Long channelServiceCharge) {
        this.channelServiceCharge = channelServiceCharge;
    }
}
