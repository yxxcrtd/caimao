package com.caimao.bana.api.entity.req.loan;

import com.caimao.bana.api.entity.QueryBase;
import com.caimao.bana.api.entity.res.loan.FLoanP2PApplyRes;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 获取借贷
 * Created by Administrator on 2015/7/24.
 */
public class FLoanP2PApplyReq extends QueryBase<FLoanP2PApplyRes> implements Serializable {
    @NotNull(message = "用户编号不能为空")
    private Long userId;
    private Long prodId;
    private String loanApplyAction;
    private String verifyStatus;
    private String applyDatetimeBegin;
    private String applyDatetimeEnd;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProdId() {
        return prodId;
    }

    public void setProdId(Long prodId) {
        this.prodId = prodId;
    }

    public String getLoanApplyAction() {
        return loanApplyAction;
    }

    public void setLoanApplyAction(String loanApplyAction) {
        this.loanApplyAction = loanApplyAction;
    }

    public String getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(String verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    public String getApplyDatetimeBegin() {
        return applyDatetimeBegin;
    }

    public void setApplyDatetimeBegin(String applyDatetimeBegin) {
        this.applyDatetimeBegin = applyDatetimeBegin;
    }

    public String getApplyDatetimeEnd() {
        return applyDatetimeEnd;
    }

    public void setApplyDatetimeEnd(String applyDatetimeEnd) {
        this.applyDatetimeEnd = applyDatetimeEnd;
    }
}
