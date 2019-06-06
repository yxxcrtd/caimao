package com.caimao.gjs.api.entity.req;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class FDoFullStopCancelReq implements Serializable{
    /**用户编号*/
    @NotNull(message = "用户编号不能为空")
    private Long userId;
    /**交易所代码*/
    @NotEmpty(message = "交易所代码不能为空")
    private String exchange;
    /**申请日期*/
    @NotEmpty(message = "申请日期不能为空")
    private String applyDate;
    /**商品代码*/
    @NotEmpty(message = "商品代码不能为空")
    private String prodCode;
    /**订单编号*/
    @NotEmpty(message = "订单编号不能为空")
    private String orderNo;

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

    public String getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(String applyDate) {
        this.applyDate = applyDate;
    }

    public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
