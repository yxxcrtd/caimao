package com.caimao.bana.api.entity.res.ybk;

import java.io.Serializable;
import java.util.Date;

/**
 * 前端API调用用户开户信息返回数据
 * Created by Administrator on 2015/9/16.
 */
public class FYbkAccountSimpleRes implements Serializable {
    private Long applyId;
    private String userName;
    private Long phoneNo;
    private Integer cardType;
    private String cardPath;
    private String cardOppositePath;
    private String cardNumber;
    private String bankName;
    private String bankCode;
    private String bankNum;
    private String bankNumAll;
    private String bankPath;
    private Integer exchangeIdApply;
    private String exchangeName;
    private String exchangeShortName;
    private String status;
    private Date createDate;
    private String exchangeAccount;
    private String reason;

    public Long getApplyId() {
        return applyId;
    }

    public void setApplyId(Long applyId) {
        this.applyId = applyId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getCardType() {
        return cardType;
    }

    public void setCardType(Integer cardType) {
        this.cardType = cardType;
    }

    public String getCardPath() {
        return cardPath;
    }

    public void setCardPath(String cardPath) {
        this.cardPath = cardPath;
    }

    public String getCardOppositePath() {
        return cardOppositePath;
    }

    public void setCardOppositePath(String cardOppositePath) {
        this.cardOppositePath = cardOppositePath;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getBankNumAll() {
        return bankNumAll;
    }

    public void setBankNumAll(String bankNumAll) {
        this.bankNumAll = bankNumAll;
    }

    public String getBankPath() {
        return bankPath;
    }

    public void setBankPath(String bankPath) {
        this.bankPath = bankPath;
    }

    public String getExchangeShortName() {
        return exchangeShortName;
    }

    public void setExchangeShortName(String exchangeShortName) {
        this.exchangeShortName = exchangeShortName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(Long phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankNum() {
        return bankNum;
    }

    public void setBankNum(String bankNum) {
        this.bankNum = bankNum;
    }

    public Integer getExchangeIdApply() {
        return exchangeIdApply;
    }

    public void setExchangeIdApply(Integer exchangeIdApply) {
        this.exchangeIdApply = exchangeIdApply;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public String getExchangeShortNo() {
        return exchangeShortName;
    }

    public void setExchangeShortNo(String exchangeShortNo) {
        this.exchangeShortName = exchangeShortNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getExchangeAccount() {
        return exchangeAccount;
    }

    public void setExchangeAccount(String exchangeAccount) {
        this.exchangeAccount = exchangeAccount;
    }
}
