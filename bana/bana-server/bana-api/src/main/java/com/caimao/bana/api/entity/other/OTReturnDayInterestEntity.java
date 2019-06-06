package com.caimao.bana.api.entity.other;

import java.util.Date;

/**
 * 返回多收的日融资利息
 * Created by xavier on 15/6/25.
 */
public class OTReturnDayInterestEntity {
    private Long userId;
    private Long orderAmount;
    private Long contractNo;
    private Date createDatetime;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Long orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Long getContractNo() {
        return contractNo;
    }

    public void setContractNo(Long contractNo) {
        this.contractNo = contractNo;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }
}
