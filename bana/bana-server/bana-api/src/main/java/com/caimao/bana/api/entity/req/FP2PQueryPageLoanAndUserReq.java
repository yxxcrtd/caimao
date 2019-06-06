package com.caimao.bana.api.entity.req;

import java.io.Serializable;

import com.caimao.bana.api.entity.QueryBase;
import com.caimao.bana.api.entity.res.FP2PQueryPageLoanAndUserRes;

public class FP2PQueryPageLoanAndUserReq extends QueryBase<FP2PQueryPageLoanAndUserRes> implements Serializable {
    
    private static final long serialVersionUID = 8892934139095215940L;

    private Byte targetStatus;
    
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

    public Byte getTargetStatus() {
        return targetStatus;
    }

    public void setTargetStatus(Byte targetStatus) {
        this.targetStatus = targetStatus;
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