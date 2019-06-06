package com.caimao.bana.api.entity.req;

import com.caimao.bana.api.entity.QueryBase;
import com.caimao.bana.api.entity.res.FP2PQueryUserPageInterestRes;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class FP2PQueryUserPageInterestReq extends QueryBase<FP2PQueryUserPageInterestRes> implements Serializable{
    @NotNull(message = "用户Id不能为空")
    private Long userId;

    @NotNull(message = "投标Id不能为空")
    private Long investId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getInvestId() {
        return investId;
    }

    public void setInvestId(Long investId) {
        this.investId = investId;
    }
}
