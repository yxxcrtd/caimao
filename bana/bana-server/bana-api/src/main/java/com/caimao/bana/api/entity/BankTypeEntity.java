package com.caimao.bana.api.entity;

import java.io.Serializable;

public class BankTypeEntity implements Serializable {
    private static final long serialVersionUID = -2829928346242050275L;
    private Long id;
    private String bankNo;
    private String bankName;
    private String isEnable;
    private String bankAddrNo;
    private String isQuickPay;
    private double rates;
    private Long channelId;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBankNo() {
        return this.bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    public String getBankName() {
        return this.bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getIsEnable() {
        return this.isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }

    public String getBankAddrNo() {
        return this.bankAddrNo;
    }

    public void setBankAddrNo(String bankAddrNo) {
        this.bankAddrNo = bankAddrNo;
    }

    public String getIsQuickPay() {
        return this.isQuickPay;
    }

    public void setIsQuickPay(String isQuickPay) {
        this.isQuickPay = isQuickPay;
    }

    public double getRates() {
        return this.rates;
    }

    public void setRates(double rates) {
        this.rates = rates;
    }

    public Long getChannelId() {
        return this.channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }
}