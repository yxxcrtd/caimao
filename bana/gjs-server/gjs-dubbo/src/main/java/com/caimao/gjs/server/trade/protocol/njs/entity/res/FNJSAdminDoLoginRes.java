package com.caimao.gjs.server.trade.protocol.njs.entity.res;

import com.caimao.gjs.server.trade.protocol.njs.entity.NJSAdminDoLoginEntity;

public class FNJSAdminDoLoginRes extends FNJSBaseRes<NJSAdminDoLoginEntity> {
    private String TOKEN; //登录令牌

    public String getTOKEN() {
        return TOKEN;
    }

    public void setTOKEN(String TOKEN) {
        this.TOKEN = TOKEN;
    }
}