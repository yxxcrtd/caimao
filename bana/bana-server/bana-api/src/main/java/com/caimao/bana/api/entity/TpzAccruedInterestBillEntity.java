/*
*TpzAccruedInterestBill.java
*Created on 2015/5/23 11:52
*Copyright (c) 2015 北京火币天下网络技术有限公司. All Rights Reserved
*/
package com.caimao.bana.api.entity;

import java.io.Serializable;

/**
 * @author Administrator
 * @version 1.0.1
 */
public class TpzAccruedInterestBillEntity implements Serializable {

    private static final long serialVersionUID = 5295504302466080546L;

    private Long orderNo;

    private Long userId;

    private Long contractNo;

    private Long orderAmount;

    private String settleInterestBeginDate;

    private String settleInterestEndDate;

    private String billAbstract;

    private String workDate;

    private String createDatetime;

    private Integer addScoreTimes = 1;

    public Long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getContractNo() {
        return contractNo;
    }

    public void setContractNo(Long contractNo) {
        this.contractNo = contractNo;
    }

    public Long getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Long orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getSettleInterestBeginDate() {
        return settleInterestBeginDate;
    }

    public void setSettleInterestBeginDate(String settleInterestBeginDate) {
        this.settleInterestBeginDate = settleInterestBeginDate;
    }

    public String getSettleInterestEndDate() {
        return settleInterestEndDate;
    }

    public void setSettleInterestEndDate(String settleInterestEndDate) {
        this.settleInterestEndDate = settleInterestEndDate;
    }

    public String getBillAbstract() {
        return billAbstract;
    }

    public void setBillAbstract(String billAbstract) {
        this.billAbstract = billAbstract;
    }

    public String getWorkDate() {
        return workDate;
    }

    public void setWorkDate(String workDate) {
        this.workDate = workDate;
    }

    public String getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(String createDatetime) {
        this.createDatetime = createDatetime;
    }

    public Integer getAddScoreTimes() {
        return addScoreTimes;
    }

    public void setAddScoreTimes(Integer addScoreTimes) {
        this.addScoreTimes = addScoreTimes;
    }
}
