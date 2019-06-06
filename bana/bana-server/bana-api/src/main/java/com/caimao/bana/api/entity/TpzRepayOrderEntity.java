package com.caimao.bana.api.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 合约还款
 */
public class TpzRepayOrderEntity implements Serializable {
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
        return orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public Long getContractNo() {
        return contractNo;
    }

    public void setContractNo(Long contractNo) {
        this.contractNo = contractNo;
    }

    public Long getPzAccountId() {
        return pzAccountId;
    }

    public void setPzAccountId(Long pzAccountId) {
        this.pzAccountId = pzAccountId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Long orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Long getAccruedInterest() {
        return accruedInterest;
    }

    public void setAccruedInterest(Long accruedInterest) {
        this.accruedInterest = accruedInterest;
    }

    public String getOrderAbstract() {
        return orderAbstract;
    }

    public void setOrderAbstract(String orderAbstract) {
        this.orderAbstract = orderAbstract;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getRepayDatetime() {
        return repayDatetime;
    }

    public void setRepayDatetime(Date repayDatetime) {
        this.repayDatetime = repayDatetime;
    }
}
