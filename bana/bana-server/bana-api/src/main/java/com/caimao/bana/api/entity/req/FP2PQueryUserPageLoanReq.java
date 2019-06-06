package com.caimao.bana.api.entity.req;

import com.caimao.bana.api.entity.QueryBase;
import com.caimao.bana.api.entity.res.FP2PQueryUserPageLoanRes;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class FP2PQueryUserPageLoanReq extends QueryBase<FP2PQueryUserPageLoanRes> implements Serializable {
    @NotNull(message = "用户Id不能为空")
    private Long userId;
    private Byte targetStatus;
    private String startDate;
    private String endDate;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Byte getTargetStatus() {
        return targetStatus;
    }

    public void setTargetStatus(Byte targetStatus) {
        this.targetStatus = targetStatus;
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
