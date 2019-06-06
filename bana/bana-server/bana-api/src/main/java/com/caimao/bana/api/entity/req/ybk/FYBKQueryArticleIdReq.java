package com.caimao.bana.api.entity.req.ybk;

import com.caimao.bana.api.entity.QueryBase;
import com.caimao.bana.api.entity.res.ybk.FYBKArticleSimpleRes;

import java.io.Serializable;
import java.util.Date;

public class FYBKQueryArticleIdReq extends QueryBase<Long> implements Serializable{
    private Integer categoryId;
    private Integer exchangeId;
    private Date created;
    private Integer isShow = 0;
    private int than;// 1大于，-1小于
    private int order;// 1升序、-1降序

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(Integer exchangeId) {
        this.exchangeId = exchangeId;
    }

    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public int getThan() {
        return than;
    }

    public void setThan(int than) {
        this.than = than;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}