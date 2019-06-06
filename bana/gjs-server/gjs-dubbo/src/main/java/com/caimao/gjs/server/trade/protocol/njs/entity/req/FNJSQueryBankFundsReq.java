package com.caimao.gjs.server.trade.protocol.njs.entity.req;

import java.io.Serializable;

public class FNJSQueryBankFundsReq implements Serializable{
    private String CUSTBANKACCTNO; //客户银行账号
    private String CUSTTRADEID; //客户交易账号
    private String CUSTBANKPASS; //客户银行密码
    private String CUSTMONEYPWD; //客户资金密码
    private String BUSINTYPE;  //业务类别 0普通转账 1外汇业务
    private String MONEYSTY; //资金类别 0保证金 1贷款 2贷款入金
    private String MONEYTYPE; //币种 0人民币 1美元 2港币 3英镑 4法郎 5日元 6欧元
    private String MEMO; //业务备注
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

    public String getCUSTBANKPASS() {
        return CUSTBANKPASS;
    }

    public void setCUSTBANKPASS(String CUSTBANKPASS) {
        this.CUSTBANKPASS = CUSTBANKPASS;
    }

    public String getCUSTMONEYPWD() {
        return CUSTMONEYPWD;
    }

    public void setCUSTMONEYPWD(String CUSTMONEYPWD) {
        this.CUSTMONEYPWD = CUSTMONEYPWD;
    }

    public String getBUSINTYPE() {
        return BUSINTYPE;
    }

    public void setBUSINTYPE(String BUSINTYPE) {
        this.BUSINTYPE = BUSINTYPE;
    }

    public String getMONEYSTY() {
        return MONEYSTY;
    }

    public void setMONEYSTY(String MONEYSTY) {
        this.MONEYSTY = MONEYSTY;
    }

    public String getMONEYTYPE() {
        return MONEYTYPE;
    }

    public void setMONEYTYPE(String MONEYTYPE) {
        this.MONEYTYPE = MONEYTYPE;
    }

    public String getMEMO() {
        return MEMO;
    }

    public void setMEMO(String MEMO) {
        this.MEMO = MEMO;
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
