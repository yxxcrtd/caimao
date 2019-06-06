package com.caimao.gjs.server.trade.protocol.njs.entity;

import java.io.Serializable;

/**
 * 查询融货费用
 */
public class NJSQueryFinanGoodsFeeEntity implements Serializable {
    private String TRADEDATE; //交易日期
    private String WAREID; //品种代码
    private String WARENAME; //品种名称
    private String FINANGOODSNUM; //融货数量
    private String FINANGOODSCOMM; //融货手续费

    public String getTRADEDATE() {
        return TRADEDATE;
    }

    public void setTRADEDATE(String TRADEDATE) {
        this.TRADEDATE = TRADEDATE;
    }

    public String getWAREID() {
        return WAREID;
    }

    public void setWAREID(String WAREID) {
        this.WAREID = WAREID;
    }

    public String getWARENAME() {
        return WARENAME;
    }

    public void setWARENAME(String WARENAME) {
        this.WARENAME = WARENAME;
    }

    public String getFINANGOODSNUM() {
        return FINANGOODSNUM;
    }

    public void setFINANGOODSNUM(String FINANGOODSNUM) {
        this.FINANGOODSNUM = FINANGOODSNUM;
    }

    public String getFINANGOODSCOMM() {
        return FINANGOODSCOMM;
    }

    public void setFINANGOODSCOMM(String FINANGOODSCOMM) {
        this.FINANGOODSCOMM = FINANGOODSCOMM;
    }
}