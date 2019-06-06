/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. 
 *All Rights Reserved
 */
package com.caimao.bana.api.entity.res;

import com.huobi.commons.utils.HsDateUtil;

/**
 * @author yanjg 2015年6月5日
 */
public class F830202Res {
    private Long orderNo;
    private Long orderAmount;
    private String settleInterestBeginDate;
    private String settleInterestEndDate;
    private String billAbstract;

    public Long getOrderNo() {
        return this.orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
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
        this.settleInterestBeginDate = HsDateUtil.convertDateNumToStr(settleInterestBeginDate);
    }

    public String getSettleInterestEndDate() {
        return this.settleInterestEndDate;
    }

    public void setSettleInterestEndDate(String settleInterestEndDate) {
        this.settleInterestEndDate = HsDateUtil.convertDateNumToStr(settleInterestEndDate);
    }

    public String getBillAbstract() {
        return this.billAbstract;
    }

    public void setBillAbstract(String billAbstract) {
        this.billAbstract = billAbstract;
    }
}
