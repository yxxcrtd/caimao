/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. All Rights Reserved
 */
package com.caimao.bana.api.entity.req.product;

import java.io.Serializable;

import com.caimao.bana.api.entity.QueryBase;
import com.caimao.bana.api.entity.res.F830910Res;


/**
 * @author zxd $Id$
 * 
 */
public class FQueryProductReq extends QueryBase<F830910Res> implements Serializable{
    private static final long serialVersionUID = -4500271354785260584L;
    private Long prodId;
    private String prodStatus;
    private String prodTypeId;

    public Long getProdId() {
        return this.prodId;
    }

    public void setProdId(Long prodId) {
        this.prodId = prodId;
    }

    public String getProdStatus() {
        return this.prodStatus;
    }

    public void setProdStatus(String prodStatus) {
        this.prodStatus = prodStatus;
    }

    public String getProdTypeId() {
        return this.prodTypeId;
    }

    public void setProdTypeId(String prodTypeId) {
        this.prodTypeId = prodTypeId;
    }
}
