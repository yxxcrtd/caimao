/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. All Rights Reserved
 */
package com.caimao.bana.api.entity.req;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author zxd $Id$
 * 
 */
public class FP2PAddinvestReq implements Serializable {
    private static final long serialVersionUID = 7416234210503000522L;
    @NotNull(message = "用户ID不能为空")
    private Long userId;    // 用户ID
    @NotNull(message = "标的ID不能为空")
    private Long targetId;    // 投资ID
    private Long investValue;    // 购买金额
    @NotEmpty(message = "安全密码不能为空")
    private String tradePwd;    // 安全密码
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Long getTargetId() {
        return targetId;
    }
    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }
    public Long getInvestValue() {
        return investValue;
    }
    public void setInvestValue(Long investValue) {
        this.investValue = investValue;
    }
    public String getTradePwd() {
        return tradePwd;
    }
    public void setTradePwd(String tradePwd) {
        this.tradePwd = tradePwd;
    }
    
    
}
