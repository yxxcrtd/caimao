/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. 
 *All Rights Reserved
 */
package com.caimao.bana.api.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yanjg
 *         2015年5月13日
 */
public class LoanDeferedFreezeEntity implements Serializable {
    private static final long serialVersionUID = 1711324118739755325L;
    private Long id;
    private Long contractNo;
    private Long freezeAmount;
    private String freezeDate;
    private String freezeFlag;
    private Date createDatetime;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getContractNo() {
        return this.contractNo;
    }

    public void setContractNo(Long contractNo) {
        this.contractNo = contractNo;
    }

    public Long getFreezeAmount() {
        return this.freezeAmount;
    }

    public void setFreezeAmount(Long freezeAmount) {
        this.freezeAmount = freezeAmount;
    }

    public String getFreezeDate() {
        return this.freezeDate;
    }

    public void setFreezeDate(String freezeDate) {
        this.freezeDate = freezeDate;
    }

    public String getFreezeFlag() {
        return this.freezeFlag;
    }

    public void setFreezeFlag(String freezeFlag) {
        this.freezeFlag = freezeFlag;
    }

    public Date getCreateDatetime() {
        return this.createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }
}
