package com.caimao.bana.api.entity.req;

import com.caimao.bana.api.entity.QueryBase;
import com.caimao.bana.api.entity.TpzLoanContractEntity;
import com.caimao.bana.api.entity.res.FTpzQueryLoanContractAllPageRes;

import java.io.Serializable;

public class FTpzQueryLoanContractAllPageReq extends QueryBase<FTpzQueryLoanContractAllPageRes> implements Serializable {
    private Long userId;
    private String userRealName;
    private Long contractNo;
    private String contractStatus;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserRealName() {
        return userRealName;
    }

    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }

    public Long getContractNo() {
        return contractNo;
    }

    public void setContractNo(Long contractNo) {
        this.contractNo = contractNo;
    }

    public String getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(String contractStatus) {
        this.contractStatus = contractStatus;
    }
}