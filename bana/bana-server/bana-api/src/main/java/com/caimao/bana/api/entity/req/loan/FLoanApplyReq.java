/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. All Rights Reserved
 */
package com.caimao.bana.api.entity.req.loan;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;

import com.caimao.bana.api.entity.QueryBase;
import com.caimao.bana.api.entity.res.loan.FLoanApplyRes;


/**
 * @author zxd $Id$
 * 
 */
public class FLoanApplyReq extends QueryBase<FLoanApplyRes> implements Serializable{

    private static final long serialVersionUID = 1947262002339502840L;
    @NotNull(message = "用户编号不能为空")
    private Long userId;
    private Long orderNo;
    private Long prodId;
    private String loanApplyAction;
    private String verifyStatus;
    private String applyDatetimeBegin;
    private String applyDatetimeEnd;
    private Long relContractNo;
    private String prodTypeId;

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOrderNo() {
        return this.orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public Long getProdId() {
        return this.prodId;
    }

    public void setProdId(Long prodId) {
        this.prodId = prodId;
    }

    public String getLoanApplyAction() {
        return this.loanApplyAction;
    }

    public void setLoanApplyAction(String loanApplyAction) {
        this.loanApplyAction = loanApplyAction;
    }

    public String getVerifyStatus() {
        return this.verifyStatus;
    }

    public void setVerifyStatus(String verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    public String getApplyDatetimeBegin() {
        return this.applyDatetimeBegin;
    }

    public void setApplyDatetimeBegin(String applyDatetimeBegin) {
        if (StringUtils.isNotEmpty(applyDatetimeBegin)) {
            applyDatetimeBegin = applyDatetimeBegin + " 00:00:00";
        }
        this.applyDatetimeBegin = applyDatetimeBegin;
    }

    public String getApplyDatetimeEnd() {
        return this.applyDatetimeEnd;
    }

    public void setApplyDatetimeEnd(String applyDatetimeEnd) {
        if (StringUtils.isNotEmpty(applyDatetimeEnd)) {
            applyDatetimeEnd = applyDatetimeEnd + " 23:59:59";
        }
        this.applyDatetimeEnd = applyDatetimeEnd;
    }

    public String getDefaultOrderColumn() {
        return "a.order_no";
    }

    public Long getRelContractNo() {
        return this.relContractNo;
    }

    public void setRelContractNo(Long relContractNo) {
        this.relContractNo = relContractNo;
    }

    public String getProdTypeId() {
        return this.prodTypeId;
    }

    public void setProdTypeId(String prodTypeId) {
        this.prodTypeId = prodTypeId;
    }
}
