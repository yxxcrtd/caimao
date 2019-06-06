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
public class RepayOrderEntity implements Serializable {
    private static final long serialVersionUID = -9031764101132878795L;
    private Long orderNo;
    private Long contractNo;
    private Long pzAccountId;
    private Long userId;
    private Long orderAmount;
    private Long accruedInterest;
    private String orderAbstract;
    private String remark;
    private Date repayDatetime;

    public Long getOrderNo() {
        return this.orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public Long getContractNo() {
        return this.contractNo;
    }

    public void setContractNo(Long contractNo) {
        this.contractNo = contractNo;
    }

    public Long getPzAccountId() {
        return this.pzAccountId;
    }

    public void setPzAccountId(Long pzAccountId) {
        this.pzAccountId = pzAccountId;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOrderAmount() {
        return this.orderAmount;
    }

    public void setOrderAmount(Long orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Long getAccruedInterest() {
        return this.accruedInterest;
    }

    public void setAccruedInterest(Long accruedInterest) {
        this.accruedInterest = accruedInterest;
    }

    public String getOrderAbstract() {
        return this.orderAbstract;
    }

    public void setOrderAbstract(String orderAbstract) {
        this.orderAbstract = orderAbstract;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getRepayDatetime() {
        return this.repayDatetime;
    }

    public void setRepayDatetime(Date repayDatetime) {
        this.repayDatetime = repayDatetime;
    }
}
