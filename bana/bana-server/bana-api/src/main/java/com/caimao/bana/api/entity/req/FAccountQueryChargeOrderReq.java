package com.caimao.bana.api.entity.req;

import com.caimao.bana.api.entity.QueryBase;
import com.caimao.bana.api.entity.res.FAccountQueryChargeOrderRes;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 查询用户充值订单
 * Created by WangXu on 2015/5/27.
 */
public class FAccountQueryChargeOrderReq extends QueryBase<FAccountQueryChargeOrderRes> implements Serializable {
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    private Long pzAccountId;
    private Long channelId;
    private String orderStatus;

    @NotEmpty(message = "请设置开始时间")
    private String startDate;
    @NotEmpty(message = "请设置结束时间")
    private String endDate;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPzAccountId() {
        return pzAccountId;
    }

    public void setPzAccountId(Long pzAccountId) {
        this.pzAccountId = pzAccountId;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
