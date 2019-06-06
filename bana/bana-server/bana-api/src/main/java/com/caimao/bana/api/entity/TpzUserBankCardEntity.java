package com.caimao.bana.api.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户绑定银行卡信息数据表
 * Created by WangXu on 2015/5/7.
 */
public class TpzUserBankCardEntity implements Serializable {
    private static final long serialVersionUID = 2660135327141045389L;
    private Long id;
    private Long userId;
    private String bankNo;
    private String bankCode;
    private String bankName;
    private String bankCardNo;
    private String bankCardName;
    private String idcardKind;
    private String idcard;
    private String province;
    private String city;
    private String openBank;
    private String isDefault;
    private String bankCardStatus;
    private String transferStatus;
    private Long transAmount;
    private Integer errorCount;
    private Date curDatetime;
    private String remark;
    private String isQuickPay;
    private String bindAgreements;
    private Date createDatetime;
    private Date updateDatetime;
    private String bankAddress;
    private String idCardHide;
    private String idCardNameHide;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public String getBankCardName() {
        return bankCardName;
    }

    public void setBankCardName(String bankCardName) {
        this.bankCardName = bankCardName;
    }

    public String getIdcardKind() {
        return idcardKind;
    }

    public void setIdcardKind(String idcardKind) {
        this.idcardKind = idcardKind;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public String getBankCardStatus() {
        return bankCardStatus;
    }

    public void setBankCardStatus(String bankCardStatus) {
        this.bankCardStatus = bankCardStatus;
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

    public String getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
    }

    public Long getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(Long transAmount) {
        this.transAmount = transAmount;
    }

    public Integer getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(Integer errorCount) {
        this.errorCount = errorCount;
    }

    public Date getCurDatetime() {
        return curDatetime;
    }

    public void setCurDatetime(Date curDatetime) {
        this.curDatetime = curDatetime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIsQuickPay() {
        return isQuickPay;
    }

    public void setIsQuickPay(String isQuickPay) {
        this.isQuickPay = isQuickPay;
    }

    public String getBindAgreements() {
        return bindAgreements;
    }

    public void setBindAgreements(String bindAgreements) {
        this.bindAgreements = bindAgreements;
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

    public String getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }

    public String getIdCardHide() {
        return idCardHide;
    }

    public void setIdCardHide(String idCardHide) {
        this.idCardHide = idCardHide;
    }

    public String getIdCardNameHide() {
        return idCardNameHide;
    }

    public void setIdCardNameHide(String idCardNameHide) {
        this.idCardNameHide = idCardNameHide;
    }
}
