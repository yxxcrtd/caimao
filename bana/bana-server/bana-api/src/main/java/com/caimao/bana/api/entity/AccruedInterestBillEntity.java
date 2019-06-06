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
public class AccruedInterestBillEntity implements Serializable {

    private static final long serialVersionUID = -4789772236402363961L;

    private Long orderNo;
    private Long userId;
    private Long contractNo;
    private Long orderAmount;
    private String settleInterestBeginDate;
    private String settleInterestEndDate;
    private String billAbstract;
    private String workDate;
    private Date createDatetime;

    public Long getOrderNo() {
        return this.orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getContractNo() {
        return this.contractNo;
    }

    public void setContractNo(Long contractNo) {
        this.contractNo = contractNo;
    }

    public Long getOrderAmount() {
        return this.orderAmount;
    }

    public void setOrderAmount(Long orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getSettleInterestBeginDate() {
        return this.settleInterestBeginDate;
    }

    public void setSettleInterestBeginDate(String settleInterestBeginDate) {
        this.settleInterestBeginDate = settleInterestBeginDate;
    }

    public String getSettleInterestEndDate() {
        return this.settleInterestEndDate;
    }

    public void setSettleInterestEndDate(String settleInterestEndDate) {
        this.settleInterestEndDate = settleInterestEndDate;
    }

    public String getBillAbstract() {
        return this.billAbstract;
    }

    public void setBillAbstract(String billAbstract) {
        this.billAbstract = billAbstract;
    }

    public String getWorkDate() {
        return this.workDate;
    }

    public void setWorkDate(String workDate) {
        this.workDate = workDate;
    }

    public Date getCreateDatetime() {
        return this.createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }
}
