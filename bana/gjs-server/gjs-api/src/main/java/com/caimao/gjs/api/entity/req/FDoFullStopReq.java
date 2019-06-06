package com.caimao.gjs.api.entity.req;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

public class FDoFullStopReq implements Serializable{
    /**用户编号*/
    @NotNull(message = "用户编号不能为空")
    private Long userId;
    /**交易所代码*/
    @NotEmpty(message = "交易所代码不能为空")
    private String exchange;
    /**商品代码*/
    @NotEmpty(message = "商品代码不能为空")
    private String prodCode;
    /**止盈价*/
    @NotNull(message = "止盈价不能为空")
    @DecimalMin(value = "0", message = "价格不能小于0")
    private BigDecimal upPrice;
    /**止损价*/
    @NotNull(message = "止损价不能为空")
    @DecimalMin(value = "0", message = "价格不能小于0")
    private BigDecimal downPrice;
    /**数量*/
    @NotNull(message = "数量不能为空")
    @Min(value = 1, message = "数量不能小于1")
    private Integer amount;

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

    public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public BigDecimal getUpPrice() {
        return upPrice;
    }

    public void setUpPrice(BigDecimal upPrice) {
        this.upPrice = upPrice;
    }

    public BigDecimal getDownPrice() {
        return downPrice;
    }

    public void setDownPrice(BigDecimal downPrice) {
        this.downPrice = downPrice;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
