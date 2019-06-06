package com.caimao.gjs.server.trade.protocol.njs.entity.req;

import java.io.Serializable;

public class FNJSDoTradeLoginReq implements Serializable{
    private String TRADERID; //交易商编号
    private String TRADEPASS; //交易密码
    private String TYPE; //登录类型 0交易商编号 1手机号
    private String IPADDRESS; //登录IP地址

    public String getTRADERID() {
        return TRADERID;
    }

    public void setTRADERID(String TRADERID) {
        this.TRADERID = TRADERID;
    }

    public String getTRADEPASS() {
        return TRADEPASS;
    }

    public void setTRADEPASS(String TRADEPASS) {
        this.TRADEPASS = TRADEPASS;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public String getIPADDRESS() {
        return IPADDRESS;
    }

    public void setIPADDRESS(String IPADDRESS) {
        this.IPADDRESS = IPADDRESS;
    }
}
