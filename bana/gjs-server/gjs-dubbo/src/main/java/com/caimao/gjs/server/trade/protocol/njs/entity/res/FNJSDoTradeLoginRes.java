package com.caimao.gjs.server.trade.protocol.njs.entity.res;

import com.caimao.gjs.server.trade.protocol.njs.entity.NJSDoTradeLoginEntity;

public class FNJSDoTradeLoginRes extends FNJSBaseRes<NJSDoTradeLoginEntity> {
    //用户名密码索引
    private String TOKEN;

    public String getTOKEN() {
        return TOKEN;
    }

    public void setTOKEN(String TOKEN) {
        this.TOKEN = TOKEN;
    }
}