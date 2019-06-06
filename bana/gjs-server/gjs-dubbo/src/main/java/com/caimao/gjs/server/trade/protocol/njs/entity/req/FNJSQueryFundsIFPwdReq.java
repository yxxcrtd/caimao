package com.caimao.gjs.server.trade.protocol.njs.entity.req;

import java.io.Serializable;

public class FNJSQueryFundsIFPwdReq implements Serializable{
    private String IOTYPE; //出入金类型 0出金 1入金 2银行余额
    private String BANKID; //银行编号
    private String TRADERID; //交易员编号

    public String getIOTYPE() {
        return IOTYPE;
    }

    public void setIOTYPE(String IOTYPE) {
        this.IOTYPE = IOTYPE;
    }

    public String getBANKID() {
        return BANKID;
    }

    public void setBANKID(String BANKID) {
        this.BANKID = BANKID;
    }

    public String getTRADERID() {
        return TRADERID;
    }

    public void setTRADERID(String TRADERID) {
        this.TRADERID = TRADERID;
    }
}