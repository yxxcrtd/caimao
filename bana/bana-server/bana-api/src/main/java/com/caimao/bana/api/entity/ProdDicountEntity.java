package com.caimao.bana.api.entity;

import java.io.Serializable;
import java.util.Date;

public class ProdDicountEntity implements Serializable {
    private static final long serialVersionUID = 8682322277459712735L;
    private Long id;
    private Long prodId;
    private String dicountName;
    private Date beginDatetime;
    private Long dicountTotalAmount;
    private Long dicountUseAmount;
    private String prodStatus;
    private Integer dicountUserNum;
    private Double interestRate;
    private Long prodAmountMin;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProdId() {
        return this.prodId;
    }

    public void setProdId(Long prodId) {
        this.prodId = prodId;
    }

    public String getDicountName() {
        return this.dicountName;
    }

    public void setDicountName(String dicountName) {
        this.dicountName = dicountName;
    }

    public Date getBeginDatetime() {
        return this.beginDatetime;
    }

    public void setBeginDatetime(Date beginDatetime) {
        this.beginDatetime = beginDatetime;
    }

    public Long getDicountTotalAmount() {
        return this.dicountTotalAmount;
    }

    public void setDicountTotalAmount(Long dicountTotalAmount) {
        this.dicountTotalAmount = dicountTotalAmount;
    }

    public Long getDicountUseAmount() {
        return this.dicountUseAmount;
    }

    public void setDicountUseAmount(Long dicountUseAmount) {
        this.dicountUseAmount = dicountUseAmount;
    }

    public String getProdStatus() {
        return this.prodStatus;
    }

    public void setProdStatus(String prodStatus) {
        this.prodStatus = prodStatus;
    }

    public Integer getDicountUserNum() {
        return this.dicountUserNum;
    }

    public void setDicountUserNum(Integer dicountUserNum) {
        this.dicountUserNum = dicountUserNum;
    }

    public Double getInterestRate() {
        return this.interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    public Long getProdAmountMin() {
        return this.prodAmountMin;
    }

    public void setProdAmountMin(Long prodAmountMin) {
        this.prodAmountMin = prodAmountMin;
    }
}