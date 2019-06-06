package com.caimao.gjs.server.trade.protocol.njs.entity.res;

public class FNJSDoOpenAccountRes{
    private String ADAPTER; //协议号
    private String STATE; //状态 0为成功， 错误对应相应的错误代码
    private String MSG; //错误信息
    private String FULLNAME; //姓名
    private String TELNO; //移动电话
    private String FIRMCARDTYPE; //证件类型
    private String FIRMCARDNO; //证件号码
    private String SIGNBANKNAME; //结算银行
    private String BANKNO; //银行卡号
    private String TRADERID; //交易员号

    public String getADAPTER() {
        return ADAPTER;
    }

    public void setADAPTER(String ADAPTER) {
        this.ADAPTER = ADAPTER;
    }

    public String getSTATE() {
        return STATE;
    }

    public void setSTATE(String STATE) {
        this.STATE = STATE;
    }

    public String getMSG() {
        return MSG;
    }

    public void setMSG(String MSG) {
        this.MSG = MSG;
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
}