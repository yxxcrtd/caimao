/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. 
 *All Rights Reserved
 */
package com.caimao.bana.api.entity.req;

import com.caimao.bana.api.entity.QueryBase;
import com.caimao.bana.api.entity.res.F831209Res;

/**
 * @author yanjg 2015年6月5日
 */
public class F831209Req extends QueryBase<F831209Res> {
    private Long prodId;
    private Long refUserId;
    private String settleDatetimeBegin;
    private String settleDatetimeEnd;

    public Long getProdId() {
        return this.prodId;
    }

    public void setProdId(Long prodId) {
        this.prodId = prodId;
    }

    public Long getRefUserId() {
        return this.refUserId;
    }

    public void setRefUserId(Long refUserId) {
        this.refUserId = refUserId;
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
}
