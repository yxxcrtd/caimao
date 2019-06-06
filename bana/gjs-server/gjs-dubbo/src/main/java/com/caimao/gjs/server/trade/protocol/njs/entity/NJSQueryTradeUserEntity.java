package com.caimao.gjs.server.trade.protocol.njs.entity;

import java.io.Serializable;

/**
 * 查询交易用户
 */
public class NJSQueryTradeUserEntity implements Serializable {
    private String TRADERID;
    private String TELNO;
    private String FIRMCARDTYPE;
    private String FIRMCARDNO;
    private String SIGNBANKNAME;
    private String BANKNO;

    public String getTRADERID() {
        return TRADERID;
    }

    public void setTRADERID(String TRADERID) {
        this.TRADERID = TRADERID;
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
}