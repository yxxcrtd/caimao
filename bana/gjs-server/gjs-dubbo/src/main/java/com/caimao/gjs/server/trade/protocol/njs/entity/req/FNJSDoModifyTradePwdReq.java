package com.caimao.gjs.server.trade.protocol.njs.entity.req;

import java.io.Serializable;

public class FNJSDoModifyTradePwdReq implements Serializable{
    private String TOKEN; //用户索引
    private String TRADERID; //交易商编号
    private String OLDPWD; //原密码
    private String NEWPWD; //新密码
    private String IPADDRESS; //IP地址

    public String getTOKEN() {
        return TOKEN;
    }

    public void setTOKEN(String TOKEN) {
        this.TOKEN = TOKEN;
    }

    public String getTRADERID() {
        return TRADERID;
    }

    public void setTRADERID(String TRADERID) {
        this.TRADERID = TRADERID;
    }

    public String getOLDPWD() {
        return OLDPWD;
    }

    public void setOLDPWD(String OLDPWD) {
        this.OLDPWD = OLDPWD;
    }

    public String getNEWPWD() {
        return NEWPWD;
    }

    public void setNEWPWD(String NEWPWD) {
        this.NEWPWD = NEWPWD;
    }

    public String getIPADDRESS() {
        return IPADDRESS;
    }

    public void setIPADDRESS(String IPADDRESS) {
        this.IPADDRESS = IPADDRESS;
    }
}
