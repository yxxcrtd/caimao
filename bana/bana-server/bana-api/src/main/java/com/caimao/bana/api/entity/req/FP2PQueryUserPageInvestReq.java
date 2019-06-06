package com.caimao.bana.api.entity.req;

import com.caimao.bana.api.entity.QueryBase;
import com.caimao.bana.api.entity.res.FP2PQueryUserPageInvestRes;
import org.apache.commons.lang.StringUtils;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class FP2PQueryUserPageInvestReq extends QueryBase<FP2PQueryUserPageInvestRes> implements Serializable {
    @NotNull(message = "用户Id不能为空")
    private Long userId;
    private Byte investStatus;
    private String startDate;
    private String endDate;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Byte getInvestStatus() {
        return investStatus;
    }

    public void setInvestStatus(Byte investStatus) {
        this.investStatus = investStatus;
    }

    public String getstartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        if (StringUtils.isNotEmpty(startDate)) {
            startDate = startDate + " 00:00:00";
        }
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        if (StringUtils.isNotEmpty(endDate)) {
            endDate = endDate + " 23:59:59";
        }
        this.endDate = endDate;
    }
}
