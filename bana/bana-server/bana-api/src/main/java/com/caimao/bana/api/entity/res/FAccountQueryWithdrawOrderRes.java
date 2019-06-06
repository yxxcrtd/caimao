package com.caimao.bana.api.entity.res;

import java.io.Serializable;

/**
 * 提现订单记录
 * Created by WangXu on 2015/6/1.
 */
public class FAccountQueryWithdrawOrderRes implements Serializable {
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
    private String createDatetime;
    private String verifyDatetime;

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

    public String getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(String createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getVerifyDatetime() {
        return verifyDatetime;
    }

    public void setVerifyDatetime(String verifyDatetime) {
        this.verifyDatetime = verifyDatetime;
    }
}
