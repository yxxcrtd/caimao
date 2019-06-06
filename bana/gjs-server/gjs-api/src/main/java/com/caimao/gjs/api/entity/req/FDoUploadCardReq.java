package com.caimao.gjs.api.entity.req;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

public class FDoUploadCardReq implements Serializable{
    /**用户编号*/
    @NotNull(message = "用户编号不能为空")
    private Long userId;
    /**交易所代码*/
    @NotEmpty(message = "交易所代码不能为空")
    private String exchange;
    /**身份证正面*/
    @NotEmpty(message = "身份证正面不能为空")
    private String cardPositive;
    /**身份证反面*/
    @NotEmpty(message = "身份证反面不能为空")
    private String cardObverse;

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
}
