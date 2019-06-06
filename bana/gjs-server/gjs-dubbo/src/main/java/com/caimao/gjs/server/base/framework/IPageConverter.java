package com.caimao.gjs.server.base.framework;

/**
 * Created by WangXu on 2015/5/29.
 */
public abstract interface IPageConverter<T>
{
    public abstract IPageParameter toPage(T paramT);

    public abstract void returnTotal(T paramT, int paramInt);
}