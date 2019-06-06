package com.caimao.bana.api.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by WangXu on 2015/5/14.
 */
public class TpzChannelBankEntity implements Serializable {
    private static final long serialVersionUID = 3576345600958993007L;
    private Long id;
    private String bankNo;
    private String bankCode;
    private String isQuickPay;
    private Long channelId;
    private BigDecimal rates;
    private Short level;

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

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getIsQuickPay() {
        return isQuickPay;
    }

    public void setIsQuickPay(String isQuickPay) {
        this.isQuickPay = isQuickPay;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public BigDecimal getRates() {
        return rates;
    }

    public void setRates(BigDecimal rates) {
        this.rates = rates;
    }

    public Short getLevel() {
        return level;
    }

    public void setLevel(Short level) {
        this.level = level;
    }
}
