package com.caimao.gjs.server.trade.protocol.njs.entity.req;

public class FNJSDoCreateTransferNoReq {
    private String CUSTTRADEID; //客户交易账号
    private String CHANGEMONEY; //转账资金
    private String TRADETYPE; //交易类型
    private String SIGN; //url地址类型
    private String MEMO; //业务备注
    private String TRADEDATE; //交易日期
    private String TRADETIME; //交易时间
    private String OS; //来源
    private String TOKEN; //令牌
    private String BANKID; //银行编号

    public String getCUSTTRADEID() {
        return CUSTTRADEID;
    }

    public void setCUSTTRADEID(String CUSTTRADEID) {
        this.CUSTTRADEID = CUSTTRADEID;
    }

    public String getCHANGEMONEY() {
        return CHANGEMONEY;
    }

    public void setCHANGEMONEY(String CHANGEMONEY) {
        this.CHANGEMONEY = CHANGEMONEY;
    }

    public String getTRADETYPE() {
        return TRADETYPE;
    }

    public void setTRADETYPE(String TRADETYPE) {
        this.TRADETYPE = TRADETYPE;
    }

    public String getSIGN() {
        return SIGN;
    }

    public void setSIGN(String SIGN) {
        this.SIGN = SIGN;
    }

    public String getMEMO() {
        return MEMO;
    }

    public void setMEMO(String MEMO) {
        this.MEMO = MEMO;
    }

    public String getTRADEDATE() {
        return TRADEDATE;
    }

    public void setTRADEDATE(String TRADEDATE) {
        this.TRADEDATE = TRADEDATE;
    }

    public String getTRADETIME() {
        return TRADETIME;
    }

    public void setTRADETIME(String TRADETIME) {
        this.TRADETIME = TRADETIME;
    }

    public String getOS() {
        return OS;
    }

    public void setOS(String OS) {
        this.OS = OS;
    }

    public String getTOKEN() {
        return TOKEN;
    }

    public void setTOKEN(String TOKEN) {
        this.TOKEN = TOKEN;
    }

    public String getBANKID() {
        return BANKID;
    }

    public void setBANKID(String BANKID) {
        this.BANKID = BANKID;
    }
}