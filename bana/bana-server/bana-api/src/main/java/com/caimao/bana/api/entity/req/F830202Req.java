/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. 
 *All Rights Reserved
 */
package com.caimao.bana.api.entity.req;

import javax.validation.constraints.NotNull;

import com.caimao.bana.api.entity.QueryBase;
import com.caimao.bana.api.entity.res.F830202Res;

/**
 * @author yanjg 2015年6月5日
 */
public class F830202Req extends QueryBase<F830202Res> {

    @NotNull(message = "合约编号不能为空")
    private Long contractNo;
    private Long orderNo;
    private String settleDatetimeBegin;
    private String settleDatetimeEnd;

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

    public String getSettleDatetimeBegin() {
        return this.settleDatetimeBegin;
    }

    public void setSettleDatetimeBegin(String settleDatetimeBegin) {
        this.settleDatetimeBegin = settleDatetimeBegin;
    }

    public String getSettleDatetimeEnd() {
        return this.settleDatetimeEnd;
    }

    public void setSettleDatetimeEnd(String settleDatetimeEnd) {
        this.settleDatetimeEnd = settleDatetimeEnd;
    }

    public String getDefaultOrderColumn() {
        return "order_no";
    }
}
