package com.caimao.gjs.server.base.framework;

/**
 * Created by WangXu on 2015/5/29.
 */
public abstract interface IPageParameter {
    public abstract int getStart();

    public abstract int getLimit();

    public abstract boolean isRequireTotal();

    public abstract void setTotal(int paramInt);
}