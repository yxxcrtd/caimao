package com.caimao.gjs.api.entity.req;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

public class FDoTransferInReq implements Serializable{
    /**用户编号*/
    @NotNull(message = "用户编号不能为空")
    private Long userId;
    /**交易所代码*/
    @NotEmpty(message = "交易所代码不能为空")
    private String exchange;
    /**资金密码*/
    @NotEmpty(message = "资金密码不能为空")
    private String fundsPwd;
    /**银行卡密码*/
    private String bankPwd;
    /**金额*/
    @NotNull(message = "金额不能为空")
    @DecimalMin(value = "1", message = "金额不能小于1")
    private BigDecimal money;
    /**来源*/
    private String os;

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

    public String getFundsPwd() {
        return fundsPwd;
    }

    public void setFundsPwd(String fundsPwd) {
        this.fundsPwd = fundsPwd;
    }

    public String getBankPwd() {
        return bankPwd;
    }

    public void setBankPwd(String bankPwd) {
        this.bankPwd = bankPwd;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }
}
