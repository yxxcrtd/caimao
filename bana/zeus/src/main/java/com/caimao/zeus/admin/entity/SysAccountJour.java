/*
*Account.java
*Created on 2015/5/8 14:36
*Copyright (c) 2015 北京火币天下网络技术有限公司. All Rights Reserved
*/
package com.caimao.zeus.admin.entity;


import java.io.Serializable;
import java.util.Date;

/**
 * 系统账户资金变动实体类
 */
public class SysAccountJour implements Serializable {
    private Long id;
    private Long sysAccountId;
    private Long extId;
    private Integer transType;
    private Long transAmount;
    private Long preAmount;
    private Long postAmount;
    private String remark;
    private Date created;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSysAccountId() {
        return sysAccountId;
    }

    public void setSysAccountId(Long sysAccountId) {
        this.sysAccountId = sysAccountId;
    }

    public Long getExtId() {
        return extId;
    }

    public void setExtId(Long extId) {
        this.extId = extId;
    }

    public Integer getTransType() {
        return transType;
    }

    public void setTransType(Integer transType) {
        this.transType = transType;
    }

    public Long getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(Long transAmount) {
        this.transAmount = transAmount;
    }

    public Long getPreAmount() {
        return preAmount;
    }

    public void setPreAmount(Long preAmount) {
        this.preAmount = preAmount;
    }

    public Long getPostAmount() {
        return postAmount;
    }

    public void setPostAmount(Long postAmount) {
        this.postAmount = postAmount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
