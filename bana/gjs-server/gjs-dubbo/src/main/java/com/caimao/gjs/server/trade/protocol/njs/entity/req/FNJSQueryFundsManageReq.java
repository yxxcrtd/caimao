package com.caimao.gjs.server.trade.protocol.njs.entity.req;

import java.io.Serializable;

public class FNJSQueryFundsManageReq implements Serializable{
    private String TOKEN; //用户令牌
    private String CUSTTRADEID; //用户索引
    private String CUSTMONEYPWD; //交易商编号
    private String TRADERID; //交易员编号

    public String getTOKEN() {
        return TOKEN;
    }

    public void setTOKEN(String TOKEN) {
        this.TOKEN = TOKEN;
    }

    public String getCUSTTRADEID() {
        return CUSTTRADEID;
    }

    public void setCUSTTRADEID(String CUSTTRADEID) {
        this.CUSTTRADEID = CUSTTRADEID;
    }

    public String getCUSTMONEYPWD() {
        return CUSTMONEYPWD;
    }

    public void setCUSTMONEYPWD(String CUSTMONEYPWD) {
        this.CUSTMONEYPWD = CUSTMONEYPWD;
    }

    public String getTRADERID() {
        return TRADERID;
    }

    public void setTRADERID(String TRADERID) {
        this.TRADERID = TRADERID;
    }
}