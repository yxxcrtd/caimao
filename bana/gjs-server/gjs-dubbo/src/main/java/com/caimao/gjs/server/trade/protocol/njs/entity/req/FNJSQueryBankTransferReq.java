package com.caimao.gjs.server.trade.protocol.njs.entity.req;

import java.io.Serializable;

public class FNJSQueryBankTransferReq implements Serializable{
    private String CUSTBANKACCTNO; //客户银行账号
    private String CUSTTRADEID; //客户交易账号
    private String CUSTMONEYPWD; //客户资金密码
    private String MONEYTYPE; //币种
    private String BANKID; //银行编号
    private String TRADERID; //交易员编号

    public String getCUSTBANKACCTNO() {
        return CUSTBANKACCTNO;
    }

    public void setCUSTBANKACCTNO(String CUSTBANKACCTNO) {
        this.CUSTBANKACCTNO = CUSTBANKACCTNO;
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

    public String getMONEYTYPE() {
        return MONEYTYPE;
    }

    public void setMONEYTYPE(String MONEYTYPE) {
        this.MONEYTYPE = MONEYTYPE;
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