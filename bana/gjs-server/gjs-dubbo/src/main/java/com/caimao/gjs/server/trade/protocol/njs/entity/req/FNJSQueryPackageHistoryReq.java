package com.caimao.gjs.server.trade.protocol.njs.entity.req;

import java.io.Serializable;

public class FNJSQueryPackageHistoryReq implements Serializable{
    private String TOKEN; //用户索引
    private String TRADERID; //交易商编号
    private String BEGINDATE; //开始日期
    private String ENDDATE; //结束日期

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

    public String getBEGINDATE() {
        return BEGINDATE;
    }

    public void setBEGINDATE(String BEGINDATE) {
        this.BEGINDATE = BEGINDATE;
    }

    public String getENDDATE() {
        return ENDDATE;
    }

    public void setENDDATE(String ENDDATE) {
        this.ENDDATE = ENDDATE;
    }
}