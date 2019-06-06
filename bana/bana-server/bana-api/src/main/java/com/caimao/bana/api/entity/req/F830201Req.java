/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. 
 *All Rights Reserved
 */
package com.caimao.bana.api.entity.req;

import javax.validation.constraints.NotNull;

import com.caimao.bana.api.entity.QueryBase;
import com.caimao.bana.api.entity.res.F830201Res;

/**
 * @author yanjg 2015年6月5日
 */
public class F830201Req extends QueryBase<F830201Res> {

    @NotNull(message = "合约编号不能为空")
    private Long contractNo;
    private Long orderNo;
    private String transDatetimeBegin;
    private String transDatetimeEnd;

    public Long getContractNo() {
        return this.contractNo;
    }

    public void setContractNo(Long contractNo) {
        this.contractNo = contractNo;
    }

    public Long getOrderNo() {
        return this.orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public String getTransDatetimeBegin() {
        return this.transDatetimeBegin;
    }

    public void setTransDatetimeBegin(String transDatetimeBegin) {
        this.transDatetimeBegin = transDatetimeBegin;
    }

    public String getTransDatetimeEnd() {
        return this.transDatetimeEnd;
    }

    public void setTransDatetimeEnd(String transDatetimeEnd) {
        this.transDatetimeEnd = transDatetimeEnd;
    }

    public String getDefaultOrderColumn() {
        return "order_no";
    }
}
