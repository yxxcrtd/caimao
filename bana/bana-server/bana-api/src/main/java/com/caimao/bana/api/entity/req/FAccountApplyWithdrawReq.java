package com.caimao.bana.api.entity.req;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 申请提现的请求对象
 * Created by WangXu on 2015/5/27.
 */
public class FAccountApplyWithdrawReq implements Serializable {
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @NotEmpty(message = "资金密码不能为空")
    private String userTradePwd;

    @Min(value = 10000, message = "最小提现100元")
    private Long orderAmount;

    @NotEmpty(message = "订单名称不能为空")
    private String orderName;

    @NotEmpty(message = "订单摘要不能为空")
    private String orderAbstract;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserTradePwd() {
        return userTradePwd;
    }

    public void setUserTradePwd(String userTradePwd) {
        this.userTradePwd = userTradePwd;
    }

    public Long getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Long orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getOrderAbstract() {
        return orderAbstract;
    }

    public void setOrderAbstract(String orderAbstract) {
        this.orderAbstract = orderAbstract;
    }
}
