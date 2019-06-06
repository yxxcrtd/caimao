package com.caimao.bana.api.entity;

import java.util.Date;

/**
 * 充值记录表
 * Created by WangXu on 2015/4/22.
 */
public class TpzChargeOrderEntity extends QueryBase<TpzChargeOrderEntity> {
    private static final long serialVersionUID = -3120551417327327761L;

    private Long orderNo;
    private Long userId;
    private Long pzAccountId;
    private Long channelId;
    private String orderName;
    private String orderStatus;
    private String bankNo;
    private Date createDatetimeBegin;
    private Date createDatetimeEnd;
    private String bankCode;
    private String bankCardName;
    private String bankCardNo;
    private String currencyType;
    private Long orderAmount;
    private String orderAbstract;
    private Date bankSuccessDatetime;
    private Date paySubmitDatetime;
    private Long channelServiceCharge;
    private String bankResultCode;
    private String bankResultCodeNote;
    private String payId;
    private String payResultNote;
    private Date payResultDatetime;
    private String checkStatus;
    private String workDate;
    private Date createDatetime;
    private Date updateDatetime;
    private String remark;
    private String payType;
    private Date verifyDatetime;
    private String verifyUser;

    public Long getOrderNo()
    {
        return this.orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public Date getCreateDatetimeBegin() {
        return this.createDatetimeBegin;
    }

    public void setCreateDatetimeBegin(Date createDatetimeBegin) {
        this.createDatetimeBegin = createDatetimeBegin;
    }

    public void setCreateDatetimeEnd(Date createDatetimeEnd) {
        this.createDatetimeEnd = createDatetimeEnd;
    }

    public Date getCreateDatetimeEnd() {
        return this.createDatetimeEnd;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPzAccountId() {
        return this.pzAccountId;
    }

    public void setPzAccountId(Long pzAccountId) {
        this.pzAccountId = pzAccountId;
    }

    public String getOrderName() {
        return this.orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getOrderStatus() {
        return this.orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getBankNo() {
        return this.bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    public String getBankCode() {
        return this.bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankCardName() {
        return this.bankCardName;
    }

    public void setBankCardName(String bankCardName) {
        this.bankCardName = bankCardName;
    }

    public String getBankCardNo() {
        return this.bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public String getCurrencyType() {
        return this.currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
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

    public Date getBankSuccessDatetime() {
        return this.bankSuccessDatetime;
    }

    public void setBankSuccessDatetime(Date bankSuccessDatetime) {
        this.bankSuccessDatetime = bankSuccessDatetime;
    }

    public Date getPaySubmitDatetime() {
        return this.paySubmitDatetime;
    }

    public void setPaySubmitDatetime(Date paySubmitDatetime) {
        this.paySubmitDatetime = paySubmitDatetime;
    }

    public Long getChannelServiceCharge() {
        return this.channelServiceCharge;
    }

    public void setChannelServiceCharge(Long channelServiceCharge) {
        this.channelServiceCharge = channelServiceCharge;
    }

    public String getBankResultCode() {
        return this.bankResultCode;
    }

    public void setBankResultCode(String bankResultCode) {
        this.bankResultCode = bankResultCode;
    }

    public String getBankResultCodeNote() {
        return this.bankResultCodeNote;
    }

    public void setBankResultCodeNote(String bankResultCodeNote) {
        this.bankResultCodeNote = bankResultCodeNote;
    }

    public String getPayId() {
        return this.payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    public String getPayResultNote() {
        return this.payResultNote;
    }

    public void setPayResultNote(String payResultNote) {
        this.payResultNote = payResultNote;
    }

    public Date getPayResultDatetime() {
        return this.payResultDatetime;
    }

    public void setPayResultDatetime(Date payResultDatetime) {
        this.payResultDatetime = payResultDatetime;
    }

    public String getCheckStatus() {
        return this.checkStatus;
    }

    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getWorkDate() {
        return this.workDate;
    }

    public void setWorkDate(String workDate) {
        this.workDate = workDate;
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

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getChannelId() {
        return this.channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public String getPayType() {
        return this.payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public Date getVerifyDatetime() {
        return this.verifyDatetime;
    }

    public void setVerifyDatetime(Date verifyDatetime) {
        this.verifyDatetime = verifyDatetime;
    }

    public String getVerifyUser() {
        return this.verifyUser;
    }

    public void setVerifyUser(String verifyUser) {
        this.verifyUser = verifyUser;
    }
}
