package com.caimao.bana.api.entity.req;

import com.caimao.bana.api.entity.QueryBase;
import com.caimao.bana.api.entity.res.FP2PQueryLoanPageInvestRes;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class FP2PQueryLoanPageInvestReq extends QueryBase<FP2PQueryLoanPageInvestRes> implements Serializable {
    @NotNull(message = "标的Id不能为空")
    private Long targetId;

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }
}