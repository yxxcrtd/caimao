package com.caimao.bana.api.entity.req.ybk;

import com.caimao.bana.api.entity.QueryBase;
import com.caimao.bana.api.entity.res.ybk.FYbkAccountSimpleRes;

import java.io.Serializable;

/**
 * 前端API获取用户开户列表
 * Created by Administrator on 2015/9/16.
 */
public class FYbkApiQueryAccountListReq extends QueryBase<FYbkAccountSimpleRes> implements Serializable {
    private Long userId;
    private Integer exchangeId;
    private Integer status;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(Integer exchangeId) {
        this.exchangeId = exchangeId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
