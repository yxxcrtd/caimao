package com.caimao.bana.api.entity.req;

import com.caimao.bana.api.entity.QueryBase;
import com.caimao.bana.api.entity.res.FP2PQueryLoanPageInvestWithUserRes;

import java.io.Serializable;

/**
 * 后台 投资人投资列表
 */
public class FP2PQueryPageInvestListReq extends QueryBase<FP2PQueryLoanPageInvestWithUserRes> implements Serializable {
    
    private static final long serialVersionUID = 8892934139095215940L;

    private Byte investStatus;
    
    private Long targetId;

    private String userName;
    
    private String mobile;

    // 加一个开始时间、结束时间
    private String beginDateTime;
    private String endDateTime;

    public String getBeginDateTime() {
        return beginDateTime;
    }

    public void setBeginDateTime(String beginDateTime) {
        this.beginDateTime = beginDateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    public Byte getInvestStatus() {
        return investStatus;
    }

    public void setInvestStatus(Byte investStatus) {
        this.investStatus = investStatus;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}