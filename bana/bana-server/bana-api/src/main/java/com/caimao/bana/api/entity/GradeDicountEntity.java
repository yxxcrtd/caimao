package com.caimao.bana.api.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class GradeDicountEntity implements Serializable {
    private static final long serialVersionUID = 6698473939335516401L;
    private Short userGrade;
    private BigDecimal discountRate;

    public Short getUserGrade() {
        return this.userGrade;
    }

    public void setUserGrade(Short userGrade) {
        this.userGrade = userGrade;
    }

    public BigDecimal getDiscountRate() {
        return this.discountRate;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }
}