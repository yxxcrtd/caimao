package com.caimao.gjs.server.trade.protocol.njs.entity;

import java.io.Serializable;

/**
 * 管理员登录
 */
public class NJSAdminDoLoginEntity implements Serializable {
    private String TOKEN; //登录令牌

    public String getTOKEN() {
        return TOKEN;
    }

    public void setTOKEN(String TOKEN) {
        this.TOKEN = TOKEN;
    }
}