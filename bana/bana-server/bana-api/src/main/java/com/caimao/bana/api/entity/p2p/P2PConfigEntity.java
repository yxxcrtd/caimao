package com.caimao.bana.api.entity.p2p;

import java.io.Serializable;
import java.math.BigDecimal;

public class P2PConfigEntity implements Serializable {
    private static final long serialVersionUID = 7466644596140523392L;

    private Long prodId;

    private Integer prodLever;
    
    private Boolean isAvailable;

    private Long chuziMin;

    private Long chuziMax;

    private BigDecimal chuziRate;

    private Long manageFee;
    
    private BigDecimal caimaoRate;
    
    private BigDecimal manageRate;
    
    public BigDecimal getManageRate() {
        return manageRate;
    }

    public void setManageRate(BigDecimal manageRate) {
        this.manageRate = manageRate;
    }

    public BigDecimal getCaimaoRate() {
        return caimaoRate;
    }

    public void setCaimaoRate(BigDecimal caimaoRate) {
        this.caimaoRate = caimaoRate;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public Long getChuziMin() {
        return chuziMin;
    }

    public void setChuziMin(Long chuziMin) {
        this.chuziMin = chuziMin;
    }

    public Long getChuziMax() {
        return chuziMax;
    }

    public void setChuziMax(Long chuziMax) {
        this.chuziMax = chuziMax;
    }

    public BigDecimal getChuziRate() {
        return chuziRate;
    }

    public void setChuziRate(BigDecimal chuziRate) {
        this.chuziRate = chuziRate;
    }

    public Long getManageFee() {
        return manageFee;
    }

    public void setManageFee(Long manageFee) {
        this.manageFee = manageFee;
    }

    public Long getProdId() {
        return prodId;
    }

    public void setProdId(Long prodId) {
        this.prodId = prodId;
    }

    public Integer getProdLever() {
        return prodLever;
    }

    public void setProdLever(Integer prodLever) {
        this.prodLever = prodLever;
    }
}
