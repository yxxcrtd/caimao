package com.caimao.bana.api.entity.req;

import com.caimao.bana.api.entity.QueryBase;
import com.caimao.bana.api.entity.res.FAccountQueryWithdrawOrderRes;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 查询提现订单记录
 * Created by WangXu on 2015/5/27.
 */
public class FAccountQueryWithdrawOrderReq extends QueryBase<FAccountQueryWithdrawOrderRes> implements Serializable {
    private Long orderNo;
    private Long pzAccountId;

    @NotNull(message = "用户ID不能为空")
    private Long userId;

    private String orderStatus;
    private String verifyStatus;

    @NotEmpty(message = "请设置开始时间")
    private String startDate;
    @NotEmpty(message = "请设置结束时间")
    private String endDate;

    public Long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public Long getPzAccountId() {
        return pzAccountId;
    }

    public void setPzAccountId(Long pzAccountId) {
        this.pzAccountId = pzAccountId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(String verifyStatus) {
        this.verifyStatus = verifyStatus;
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
