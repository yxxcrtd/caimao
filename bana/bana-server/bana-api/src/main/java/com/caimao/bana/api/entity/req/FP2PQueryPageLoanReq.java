package com.caimao.bana.api.entity.req;

import com.caimao.bana.api.entity.QueryBase;
import com.caimao.bana.api.entity.res.FP2PQueryPageLoanRes;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class FP2PQueryPageLoanReq extends QueryBase<FP2PQueryPageLoanRes> implements Serializable {
    @NotNull(message = "标的状态不能为空")
    private Byte targetStatus;

    public Byte getTargetStatus() {
        return targetStatus;
    }

    public void setTargetStatus(Byte targetStatus) {
        this.targetStatus = targetStatus;
    }
}
