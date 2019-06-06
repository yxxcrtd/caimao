package com.caimao.gjs.api.entity;

import java.io.Serializable;
import java.util.Date;

public class GJSAccountEntity implements Serializable {
    /**用户编号*/
    private Long userId;
    /**交易所代码*/
    private String exchange;
    /**加盐Key*/
    private String saltKey;
    /**分组编号*/
    private String firmId;
    /**交易员编号*/
    private String traderId;
    /**真实姓名*/
    private String realName;
    /**身份证号码*/
    private String idCard;
    /**银行编号*/
    private String bankId;
    /**银行卡号码*/
    private String bankCard;
    /**是否签约*/
    private Integer isSign;
    /**0未上传 1已上传 2未提交 3已提交 4未通过 5已通过*/
    private Integer uploadStatus;
    /**正面照片*/
    private String cardPositive;
    /**反面照片*/
    private String cardObverse;
    /**是否签交收协议*/
    private Integer isSignAgreement;
    /**创建时间*/
    private Date createDatetime;
    /**开户银行名称*/
    private String openBankName;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getSaltKey() {
        return saltKey;
    }

    public void setSaltKey(String saltKey) {
        this.saltKey = saltKey;
    }

    public String getFirmId() {
        return firmId;
    }

    public void setFirmId(String firmId) {
        this.firmId = firmId;
    }

    public String getTraderId() {
        return traderId;
    }

    public void setTraderId(String traderId) {
        this.traderId = traderId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public Integer getIsSign() {
        return isSign;
    }

    public void setIsSign(Integer isSign) {
        this.isSign = isSign;
    }

    public Integer getUploadStatus() {
        return uploadStatus;
    }

    public void setUploadStatus(Integer uploadStatus) {
        this.uploadStatus = uploadStatus;
    }

    public String getCardPositive() {
        return cardPositive;
    }

    public void setCardPositive(String cardPositive) {
        this.cardPositive = cardPositive;
    }

    public String getCardObverse() {
        return cardObverse;
    }

    public void setCardObverse(String cardObverse) {
        this.cardObverse = cardObverse;
    }

    public Integer getIsSignAgreement() {
        return isSignAgreement;
    }

    public void setIsSignAgreement(Integer isSignAgreement) {
        this.isSignAgreement = isSignAgreement;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getOpenBankName() {
        return openBankName;
    }

    public void setOpenBankName(String openBankName) {
        this.openBankName = openBankName;
    }
}