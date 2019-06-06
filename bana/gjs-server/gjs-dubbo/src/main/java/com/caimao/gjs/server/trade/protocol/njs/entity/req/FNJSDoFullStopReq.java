package com.caimao.gjs.server.trade.protocol.njs.entity.req;

import java.io.Serializable;

public class FNJSDoFullStopReq implements Serializable{
    private String TOKEN; //用户索引
    private String TRADERID; //交易商编号
    private String WAREID; //品种代码
    private String UPPRICE; //止盈触发价
    private String DOWNPRICE; //止损触发价
    private String NUM; //最大执行数量
    private String SUBNO; //子客户号
    private String IPADDRESS; //IP地址
    private String VALIDDATE; //有效日期

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

    public String getWAREID() {
        return WAREID;
    }

    public void setWAREID(String WAREID) {
        this.WAREID = WAREID;
    }

    public String getUPPRICE() {
        return UPPRICE;
    }

    public void setUPPRICE(String UPPRICE) {
        this.UPPRICE = UPPRICE;
    }

    public String getDOWNPRICE() {
        return DOWNPRICE;
    }

    public void setDOWNPRICE(String DOWNPRICE) {
        this.DOWNPRICE = DOWNPRICE;
    }

    public String getNUM() {
        return NUM;
    }

    public void setNUM(String NUM) {
        this.NUM = NUM;
    }

    public String getSUBNO() {
        return SUBNO;
    }

    public void setSUBNO(String SUBNO) {
        this.SUBNO = SUBNO;
    }

    public String getIPADDRESS() {
        return IPADDRESS;
    }

    public void setIPADDRESS(String IPADDRESS) {
        this.IPADDRESS = IPADDRESS;
    }

    public String getVALIDDATE() {
        return VALIDDATE;
    }

    public void setVALIDDATE(String VALIDDATE) {
        this.VALIDDATE = VALIDDATE;
    }
}
