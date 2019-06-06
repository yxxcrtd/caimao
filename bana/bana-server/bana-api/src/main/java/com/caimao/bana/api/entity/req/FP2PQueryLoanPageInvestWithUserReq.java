/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. All Rights Reserved
 */
package com.caimao.bana.api.entity.req;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.caimao.bana.api.entity.QueryBase;
import com.caimao.bana.api.entity.res.FP2PQueryLoanPageInvestWithUserRes;

/**
 * @author Administrator $Id$
 * 
 */
public class FP2PQueryLoanPageInvestWithUserReq extends QueryBase<FP2PQueryLoanPageInvestWithUserRes> implements Serializable {
    private static final long serialVersionUID = -6870149481070412171L;
    @NotNull(message = "标的Id不能为空")
    private Long targetId;

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }
}
