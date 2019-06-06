package com.caimao.bana.api.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class ChannelBankEntity implements Serializable {
    private static final long serialVersionUID = 5754247419839063642L;
    private Long id;
    private String bankNo;
    private String bankCode;
    private String isQuickPay;
    private Long channelId;
    private BigDecimal rates;
    private Short level;

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

    public String getBankCode() {
        return this.bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getIsQuickPay() {
        return this.isQuickPay;
    }

    public void setIsQuickPay(String isQuickPay) {
        this.isQuickPay = isQuickPay;
    }

    public Long getChannelId() {
        return this.channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public BigDecimal getRates() {
        return this.rates;
    }

    public void setRates(BigDecimal rates) {
        this.rates = rates;
    }

    public Short getLevel() {
        return this.level;
    }

    public void setLevel(Short level) {
        this.level = level;
    }
}