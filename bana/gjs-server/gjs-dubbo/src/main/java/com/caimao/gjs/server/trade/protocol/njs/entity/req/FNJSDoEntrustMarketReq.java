package com.caimao.gjs.server.trade.protocol.njs.entity.req;

import java.io.Serializable;

public class FNJSDoEntrustMarketReq implements Serializable{
    private String TOKEN; //用户索引
    private String TRADERID; //交易商编号
    private String BUYORSAL; //买卖标记
    private String WAREID; //品种代码
    private String PRICE; //价格
    private String NUM; //数量
    private String IPADDRESS; //IP地址
    private String SUBNO; //子客户号

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

    public String getBUYORSAL() {
        return BUYORSAL;
    }

    public void setBUYORSAL(String BUYORSAL) {
        this.BUYORSAL = BUYORSAL;
    }

    public String getWAREID() {
        return WAREID;
    }

    public void setWAREID(String WAREID) {
        this.WAREID = WAREID;
    }

    public String getPRICE() {
        return PRICE;
    }

    public void setPRICE(String PRICE) {
        this.PRICE = PRICE;
    }

    public String getNUM() {
        return NUM;
    }

    public void setNUM(String NUM) {
        this.NUM = NUM;
    }

    public String getIPADDRESS() {
        return IPADDRESS;
    }

    public void setIPADDRESS(String IPADDRESS) {
        this.IPADDRESS = IPADDRESS;
    }

    public String getSUBNO() {
        return SUBNO;
    }

    public void setSUBNO(String SUBNO) {
        this.SUBNO = SUBNO;
    }
}
