package com.caimao.bana.api.entity.req.ybk;

import java.io.Serializable;

/**
 * 请求邮币卡交易所列表的查询请求
 * Created by Administrator on 2015/9/8.
 */
public class FYbkExchangeQueryListReq implements Serializable {
    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
