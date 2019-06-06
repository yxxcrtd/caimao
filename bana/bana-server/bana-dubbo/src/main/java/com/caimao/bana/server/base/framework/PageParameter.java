package com.caimao.bana.server.base.framework;

import java.io.Serializable;

/**
 * Created by WangXu on 2015/5/29.
 */
public class PageParameter implements IPageParameter, Serializable {
    private static final long serialVersionUID = 6245373576773306619L;
    public static final int PAGE_LIMIT_DEFAULT = 20;
    private int start;
    private int limit = 20;
    private boolean requireTotal = true;
    private int total;

    public int getStart()
    {
        return this.start;
    }

    public void setStart(int start)
    {
        this.start = start;
    }

    public int getLimit() {
        return this.limit;
    }

    public void setLimit(int limit)
    {
        this.limit = limit;
    }

    public int getTotal() {
        return this.total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public boolean isRequireTotal() {
        return this.requireTotal;
    }

    public void setRequireTotal(boolean requireTotal)
    {
        this.requireTotal = requireTotal;
    }
}
