package com.caimao.gjs.server.trade.protocol.njs.entity.req;

import java.io.Serializable;

public class FNJSDoOpenAccountReq implements Serializable{
    private String FULLNAME; //姓名
    private String TELNO; //移动电话
    private String FIRMCARDTYPE; //证件类型
    private String FIRMCARDNO; //证件号码
    private String SIGNBANKNAME; //结算银行
    private String BANKNO; //银行卡号
    private String TRADERID; //交易员编号
    private String FIRMID; //会员编号
    private String BANKCODE; //银行编码
    private String TRADEPWD; //交易密码
    private String BANKPWD; //资金密码
    private String OPENBANKNAME; //开户行名称

    public String getFULLNAME() {
        return FULLNAME;
    }

    public void setFULLNAME(String FULLNAME) {
        this.FULLNAME = FULLNAME;
    }

    public String getTELNO() {
        return TELNO;
    }

    public void setTELNO(String TELNO) {
        this.TELNO = TELNO;
    }

    public String getFIRMCARDTYPE() {
        return FIRMCARDTYPE;
    }

    public void setFIRMCARDTYPE(String FIRMCARDTYPE) {
        this.FIRMCARDTYPE = FIRMCARDTYPE;
    }

    public String getFIRMCARDNO() {
        return FIRMCARDNO;
    }

    public void setFIRMCARDNO(String FIRMCARDNO) {
        this.FIRMCARDNO = FIRMCARDNO;
    }

    public String getSIGNBANKNAME() {
        return SIGNBANKNAME;
    }

    public void setSIGNBANKNAME(String SIGNBANKNAME) {
        this.SIGNBANKNAME = SIGNBANKNAME;
    }

    public String getBANKNO() {
        return BANKNO;
    }

    public void setBANKNO(String BANKNO) {
        this.BANKNO = BANKNO;
    }

    public String getTRADERID() {
        return TRADERID;
    }

    public void setTRADERID(String TRADERID) {
        this.TRADERID = TRADERID;
    }

    public String getFIRMID() {
        return FIRMID;
    }

    public void setFIRMID(String FIRMID) {
        this.FIRMID = FIRMID;
    }

    public String getBANKCODE() {
        return BANKCODE;
    }

    public void setBANKCODE(String BANKCODE) {
        this.BANKCODE = BANKCODE;
    }

    public String getTRADEPWD() {
        return TRADEPWD;
    }

    public void setTRADEPWD(String TRADEPWD) {
        this.TRADEPWD = TRADEPWD;
    }

    public String getBANKPWD() {
        return BANKPWD;
    }

    public void setBANKPWD(String BANKPWD) {
        this.BANKPWD = BANKPWD;
    }

    public String getOPENBANKNAME() {
        return OPENBANKNAME;
    }

    public void setOPENBANKNAME(String OPENBANKNAME) {
        this.OPENBANKNAME = OPENBANKNAME;
    }
}
