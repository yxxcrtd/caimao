package com.caimao.bana.api.entity.req;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 取消提现的请求对象
 * Created by WangXu on 2015/5/27.
 */
public class FAccountCancelWithdrawReq implements Serializable {
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @NotNull(message = "订单ID不能为空")
    private Long orderId;

    @NotEmpty(message = "请填写取消提现备注信息")
    private String remark;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
