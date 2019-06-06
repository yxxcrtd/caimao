package com.caimao.zeus.admin.entity.req;

import com.caimao.bana.api.entity.QueryBase;
import com.caimao.zeus.admin.entity.res.ChangeBalanceJourRes;

import java.io.Serializable;

public class ChangeBalanceJourReq extends QueryBase<ChangeBalanceJourRes> implements Serializable {
    private Long sysAccountId;
    private Long transType;
    private String startDate;
    private String endDate;

    public Long getSysAccountId() {
        return sysAccountId;
    }

    public void setSysAccountId(Long sysAccountId) {
        this.sysAccountId = sysAccountId;
    }

    public Long getTransType() {
        return transType;
    }

    public void setTransType(Long transType) {
        this.transType = transType;
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