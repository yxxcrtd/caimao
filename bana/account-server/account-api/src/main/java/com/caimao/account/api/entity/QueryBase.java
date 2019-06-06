/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. 
 *All Rights Reserved
 */
package com.caimao.account.api.entity;

import com.huobi.commons.utils.StringUtil;

import java.io.Serializable;
import java.util.List;

/**
 * 分页基础类
 * @param <T>
 */
public class QueryBase<T> implements Serializable {
    private static final long serialVersionUID = 8990985499718290368L;
    private Integer start = Integer.valueOf(0);
    private Integer limit = Integer.valueOf(10);
    private Integer totalCount;
    private Integer pages;
    private List<T> items;
    private Integer currentPage = 1;
    private String orderColumn;
    private String orderDir;

    public QueryBase() {
    }

    public Integer getStart() {
        return this.start;
    }

    public void setStart(Integer start) {
        currentPage = start / limit + 1;
        this.start = start;
    }

    public Integer getLimit() {
        return this.limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getTotalCount() {
        return this.totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
        this.pages = Integer.valueOf((totalCount.intValue() + this.limit.intValue() - 1) / this.limit.intValue());
        this.start = (currentPage - 1) * limit;
    }

    public Integer getPages() {
        return this.pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public List<T> getItems() {
        return this.items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public String getOrderColumn() {
        return this.orderColumn;
    }

    public void setOrderColumn(String orderColumn) {
        if (StringUtil.isEmpty(orderColumn)) {
            orderColumn = this.getDefaultOrderColumn();
        }
        this.orderColumn = orderColumn;
    }

    public String getOrderDir() {
        return this.orderDir;
    }

    public void setOrderDir(String orderDir) {
        if (StringUtil.isEmpty(orderDir)) {
            orderDir = "DESC";
        }

        this.orderDir = orderDir;
    }

    public String getDefaultOrderColumn() {
        return this.orderColumn;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
        this.start = (currentPage - 1) * limit;
    }
}
