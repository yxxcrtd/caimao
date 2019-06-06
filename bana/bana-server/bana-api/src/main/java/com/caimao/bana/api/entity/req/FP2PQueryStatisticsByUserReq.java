/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. All Rights Reserved
 */
package com.caimao.bana.api.entity.req;

import java.io.Serializable;

import com.caimao.bana.api.entity.QueryBase;
import com.caimao.bana.api.entity.res.FP2PQueryStatisticsByUserRes;

/**
 * @author Administrator $Id$
 * 
 */
public class FP2PQueryStatisticsByUserReq extends QueryBase<FP2PQueryStatisticsByUserRes> implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 5729053377456194785L;

    private Byte investStatus;

    private String userName;

    private String mobile;

    public Byte getInvestStatus() {
        return investStatus;
    }

    public void setInvestStatus(Byte investStatus) {
        this.investStatus = investStatus;
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
