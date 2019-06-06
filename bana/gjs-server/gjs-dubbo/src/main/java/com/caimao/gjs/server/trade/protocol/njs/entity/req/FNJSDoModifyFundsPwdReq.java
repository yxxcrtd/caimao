package com.caimao.gjs.server.trade.protocol.njs.entity.req;

import java.io.Serializable;

public class FNJSDoModifyFundsPwdReq implements Serializable{
    private String TOKEN; //用户索引
    private String CUSTTRADEID; //客户交易账号
    private String CUSTMONEYPWD; //客户资金密码
    private String NCUSTMONEYPWD; //新客户资金密码
    private String BANKID; //银行编号
    private String TRADERID; //交易商编号

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

    public String getNCUSTMONEYPWD() {
        return NCUSTMONEYPWD;
    }

    public void setNCUSTMONEYPWD(String NCUSTMONEYPWD) {
        this.NCUSTMONEYPWD = NCUSTMONEYPWD;
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
