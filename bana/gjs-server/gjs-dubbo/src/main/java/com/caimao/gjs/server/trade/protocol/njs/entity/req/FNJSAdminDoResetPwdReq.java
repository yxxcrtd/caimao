package com.caimao.gjs.server.trade.protocol.njs.entity.req;

import java.io.Serializable;

public class FNJSAdminDoResetPwdReq implements Serializable{
    private String TOKEN; //用户索引
    private String USERID; //用户编号
    private String TRADERID; //交易商编号
    private String TRADEPWD; //交易密码
    private String BANKPWD; //资金密码
    private String IPADDRESS; //ip地址

    public String getTOKEN() {
        return TOKEN;
    }

    public void setTOKEN(String TOKEN) {
        this.TOKEN = TOKEN;
    }

    public String getUSERID() {
        return USERID;
    }

    public void setUSERID(String USERID) {
        this.USERID = USERID;
    }

    public String getTRADERID() {
        return TRADERID;
    }

    public void setTRADERID(String TRADERID) {
        this.TRADERID = TRADERID;
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

    public String getIPADDRESS() {
        return IPADDRESS;
    }

    public void setIPADDRESS(String IPADDRESS) {
        this.IPADDRESS = IPADDRESS;
    }
}