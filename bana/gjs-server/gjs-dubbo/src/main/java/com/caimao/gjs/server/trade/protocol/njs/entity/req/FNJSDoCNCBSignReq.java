package com.caimao.gjs.server.trade.protocol.njs.entity.req;

import java.io.Serializable;

public class FNJSDoCNCBSignReq implements Serializable{
    private String CUSTTRADEID; //交易商编号（交易账号）
    private String BANKID; //银行编号（212-中信异度支付）
    private String CUSTBANKACCTNO; //客户银行帐号
    private String CARDIDTYPE; //客户证件类型
    private String CARDID; //客户证件号
    private String FIRMNAME; //客户名称（银行账户名称）
    private String ACCBANKNAME; //开户银行名称
    private String CUSTOPENTYPE; //客户类型（0-个人 1-企业）
    private String TOKEN; //令牌

    public String getCUSTTRADEID() {
        return CUSTTRADEID;
    }

    public void setCUSTTRADEID(String CUSTTRADEID) {
        this.CUSTTRADEID = CUSTTRADEID;
    }

    public String getBANKID() {
        return BANKID;
    }

    public void setBANKID(String BANKID) {
        this.BANKID = BANKID;
    }

    public String getCUSTBANKACCTNO() {
        return CUSTBANKACCTNO;
    }

    public void setCUSTBANKACCTNO(String CUSTBANKACCTNO) {
        this.CUSTBANKACCTNO = CUSTBANKACCTNO;
    }

    public String getCARDIDTYPE() {
        return CARDIDTYPE;
    }

    public void setCARDIDTYPE(String CARDIDTYPE) {
        this.CARDIDTYPE = CARDIDTYPE;
    }

    public String getCARDID() {
        return CARDID;
    }

    public void setCARDID(String CARDID) {
        this.CARDID = CARDID;
    }

    public String getFIRMNAME() {
        return FIRMNAME;
    }

    public void setFIRMNAME(String FIRMNAME) {
        this.FIRMNAME = FIRMNAME;
    }

    public String getACCBANKNAME() {
        return ACCBANKNAME;
    }

    public void setACCBANKNAME(String ACCBANKNAME) {
        this.ACCBANKNAME = ACCBANKNAME;
    }

    public String getCUSTOPENTYPE() {
        return CUSTOPENTYPE;
    }

    public void setCUSTOPENTYPE(String CUSTOPENTYPE) {
        this.CUSTOPENTYPE = CUSTOPENTYPE;
    }

    public String getTOKEN() {
        return TOKEN;
    }

    public void setTOKEN(String TOKEN) {
        this.TOKEN = TOKEN;
    }
}