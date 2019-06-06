package com.caimao.gjs.server.trade.protocol.njs.entity;

import java.io.Serializable;

/**
 * 开户
 */
public class NJSDoOpenAccountEntity implements Serializable {
    private String FULLNAME; //姓名
    private String TELNO; //移动电话
    private String FIRMCARDTYPE; //证件类型
    private String FIRMCARDNO; //证件号码
    private String SIGNBANKNAME; //结算银行
    private String BANKNO; //银行卡号
    private String TRADERID; //交易员号
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

    public String getOPENBANKNAME() {
        return OPENBANKNAME;
    }

    public void setOPENBANKNAME(String OPENBANKNAME) {
        this.OPENBANKNAME = OPENBANKNAME;
    }
}