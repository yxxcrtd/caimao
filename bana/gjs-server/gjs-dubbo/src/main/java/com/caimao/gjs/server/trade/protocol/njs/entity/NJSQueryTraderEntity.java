package com.caimao.gjs.server.trade.protocol.njs.entity;

import java.io.Serializable;

/**
 * 查询交易员
 */
public class NJSQueryTraderEntity implements Serializable {
    private String TRADERID; //交易员编号
    private String FULLNAME; //姓名
    private String TELNO; //电话
    private String FIRMCARDTYPE; //证件类型
    private String FlRMCARDNO; //证件号码
    private String STGNBANKNAME; //结算银行
    private String BANKNO; //银行卡号

    public String getTRADERID() {
        return TRADERID;
    }

    public void setTRADERID(String TRADERID) {
        this.TRADERID = TRADERID;
    }

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

    public String getFlRMCARDNO() {
        return FlRMCARDNO;
    }

    public void setFlRMCARDNO(String flRMCARDNO) {
        FlRMCARDNO = flRMCARDNO;
    }

    public String getSTGNBANKNAME() {
        return STGNBANKNAME;
    }

    public void setSTGNBANKNAME(String STGNBANKNAME) {
        this.STGNBANKNAME = STGNBANKNAME;
    }

    public String getBANKNO() {
        return BANKNO;
    }

    public void setBANKNO(String BANKNO) {
        this.BANKNO = BANKNO;
    }
}