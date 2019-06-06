package com.caimao.bana.api.entity.req.ybk;

import com.caimao.bana.api.entity.QueryBase;
import com.caimao.bana.api.entity.ybk.YBKAccountEntity;
import com.caimao.bana.api.entity.ybk.YBKArticleEntity;

import java.io.Serializable;

public class FYBKQueryAccountListReq extends QueryBase<YBKAccountEntity> implements Serializable{
    private Integer exchangeIdApply;

    private Integer status;

    public Integer getExchangeIdApply() {
        return exchangeIdApply;
    }

    public void setExchangeIdApply(Integer exchangeIdApply) {
        this.exchangeIdApply = exchangeIdApply;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}