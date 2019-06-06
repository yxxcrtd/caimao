package com.caimao.gjs.api.entity.req;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

public class FDoConditionReq implements Serializable{
    /**用户编号*/
    @NotNull(message = "用户编号不能为空")
    private Long userId;
    /**交易所代码*/
    @NotEmpty(message = "交易所代码不能为空")
    private String exchange;
    /**交易类型*/
    @NotEmpty(message = "交易类型不能为空")
    private String tradeType;
    /**商品代码*/
    @NotEmpty(message = "商品代码不能为空")
    private String prodCode;
    /**价格*/
    @NotNull(message = "价格不能为空")
    @DecimalMin(value = "1", message = "价格不能小于1")
    private BigDecimal price;
    /**数量*/
    @NotNull(message = "数量不能为空")
    @Min(value = 1, message = "数量不能小于1")
    private Integer amount;
    /**触发价格*/
    @NotNull(message = "触发价格不能为空")
    @DecimalMin(value = "1", message = "价格不能小于1")
    private BigDecimal touchPrice;
    /**条件*/
    @NotEmpty(message = "条件不能为空")
    private String condition;

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

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getTouchPrice() {
        return touchPrice;
    }

    public void setTouchPrice(BigDecimal touchPrice) {
        this.touchPrice = touchPrice;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
