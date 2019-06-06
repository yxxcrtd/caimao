package com.caimao.bana.api.entity;

import java.io.Serializable;

/**
 * 银行卡类型
 * Created by WangXu on 2015/5/14.
 */
public class TpzBankTypeEntity implements Serializable {
    private static final long serialVersionUID = 839665130143995023L;
    private Long id;
    private String bankNo;
    private String bankName;
    private String isEnable;
    private String bankAddrNo;
    private String isQuickPay;
    private double rates;
    private Long channelId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }

    public String getBankAddrNo() {
        return bankAddrNo;
    }

    public void setBankAddrNo(String bankAddrNo) {
        this.bankAddrNo = bankAddrNo;
    }

    public String getIsQuickPay() {
        return isQuickPay;
    }

    public void setIsQuickPay(String isQuickPay) {
        this.isQuickPay = isQuickPay;
    }

    public double getRates() {
        return rates;
    }

    public void setRates(double rates) {
        this.rates = rates;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }
}
