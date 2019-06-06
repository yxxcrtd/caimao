/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. 
 *All Rights Reserved
 */
package com.caimao.bana.api.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付宝交易记录
 */
public class AlipayRecordEntity implements Serializable {
    public BigDecimal tradeId;
    public Long amount;
    public String realName;
    public Date finishTime;
    public Date createTime;
    public Long relNo;

    public BigDecimal getTradeId() {
        return tradeId;
    }

    public void setTradeId(BigDecimal tradeId) {
        this.tradeId = tradeId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getRelNo() {
        return relNo;
    }

    public void setRelNo(Long relNo) {
        this.relNo = relNo;
    }
}
