package com.caimao.gjs.api.entity.req;

import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

/**
 * 查询商品列表
 */
public class FQueryGoodsListReq implements Serializable {
    /**交易所代码*/
    @NotEmpty(message = "交易所代码不能为空")
    private String exchange;
    /**数据类型 0行情 1交易*/
    private Integer dataType;

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }
}
