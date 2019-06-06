package com.caimao.bana.api.entity.req;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 用户预充值请求
 * Created by WangXu on 2015/5/27.
 */
public class FAccountPreChargeReq implements Serializable {
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @NotNull(message = "请选择充值方式")
    private Long payCompanyNo;

    private String bankNo;

    @Min(value = 100, message = "最小充值数量1元")
    private Long chargeAmount;

    private Long terminalType;

    @NotEmpty(message = "订单名称不能为空")
    private String orderName;
    @NotEmpty(message = "订单摘要不能为空")
    private String orderAbstract;
    private int payType;
    private String userIp;
    private String backCardName;

    public String getOrderAbstract() {
        return orderAbstract;
    }

    public void setOrderAbstract(String orderAbstract) {
        this.orderAbstract = orderAbstract;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPayCompanyNo() {
        return payCompanyNo;
    }

    public void setPayCompanyNo(Long payCompanyNo) {
        this.payCompanyNo = payCompanyNo;
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    public Long getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(Long chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    public Long getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(Long terminalType) {
        this.terminalType = terminalType;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public String getBackCardName() {
        return backCardName;
    }

    public void setBackCardName(String backCardName) {
        this.backCardName = backCardName;
    }
}
