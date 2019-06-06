package com.caimao.gjs.server.trade.protocol.njs.entity.req;

import java.io.Serializable;

public class FNJSDoPackageBuyReq implements Serializable{
    private String TOKEN; //用户索引
    private String TRADERID; //交易商编号
    private String PACKAGEID; //套餐编号
    private String IPADDRESS; //申请ip

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

    public String getPACKAGEID() {
        return PACKAGEID;
    }

    public void setPACKAGEID(String PACKAGEID) {
        this.PACKAGEID = PACKAGEID;
    }

    public String getIPADDRESS() {
        return IPADDRESS;
    }

    public void setIPADDRESS(String IPADDRESS) {
        this.IPADDRESS = IPADDRESS;
    }
}