package com.caimao.bana.api.entity.req.guji;

import com.caimao.bana.api.entity.QueryBase;
import com.caimao.bana.api.entity.guji.GujiShareRecordEntity;

/**
 * 股计后台查询推荐的股票列表
 * Created by Administrator on 2016/1/14.
 */
public class FQueryAdminShareListReq extends QueryBase<GujiShareRecordEntity> {

    private String stockType;
    private Integer operType;
    private String stockCode;
    private String nickName;

    public String getStockType() {
        return stockType;
    }

    public void setStockType(String stockType) {
        this.stockType = stockType;
    }

    public Integer getOperType() {
        return operType;
    }

    public void setOperType(Integer operType) {
        this.operType = operType;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
