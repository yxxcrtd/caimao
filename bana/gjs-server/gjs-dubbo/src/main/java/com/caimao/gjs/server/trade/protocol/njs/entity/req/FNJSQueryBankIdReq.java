package com.caimao.gjs.server.trade.protocol.njs.entity.req;

import java.io.Serializable;

public class FNJSQueryBankIdReq implements Serializable{
    private String ADAPTER; //协议号

    public String getADAPTER() {
        return ADAPTER;
    }

    public void setADAPTER(String ADAPTER) {
        this.ADAPTER = ADAPTER;
    }
}
