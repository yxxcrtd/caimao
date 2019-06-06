/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. All Rights Reserved
 */
package com.caimao.bana.api.entity.req;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

/**
 * @author zxd $Id$
 * 
 */
public class F830312Req {
    @NotNull(message = "请输入正确用户id")
    private Long userId;

    @NotBlank(message = "请输入正确HOMS主账户")
    private String homsFundAccount;

    @NotBlank(message = "请输入正确HOMS子账户")
    private String homsCombineId;
    private Long pzAccountId;

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getHomsFundAccount() {
        return this.homsFundAccount;
    }

    public void setHomsFundAccount(String homsFundAccount) {
        this.homsFundAccount = homsFundAccount;
    }

    public String getHomsCombineId() {
        return this.homsCombineId;
    }

    public void setHomsCombineId(String homsCombineId) {
        this.homsCombineId = homsCombineId;
    }

    public Long getPzAccountId() {
        return this.pzAccountId;
    }

    public void setPzAccountId(Long pzAccountId) {
        this.pzAccountId = pzAccountId;
    }
}
