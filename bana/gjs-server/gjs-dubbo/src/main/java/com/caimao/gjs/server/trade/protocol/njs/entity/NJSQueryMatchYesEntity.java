package com.caimao.gjs.server.trade.protocol.njs.entity;

import java.io.Serializable;

/**
 * 查询已成交
 */
public class NJSQueryMatchYesEntity implements Serializable {
    private String DATE; //成交日期
    private String FIRMID; //交易商编号
    private String SERIALNO; //委托编号
    private String CONTNO; //成交编号
    private String WAREID; //品种代码
    private String BUYORSAL; //买/卖
    private String CONPRICE; //价格
    private String CONTNUM; //成交数量
    private String FTIME; //委托时间
    private String TMPMONEY; //手续费
    private String ORDERSTY; //委托标志
    private String CONTQTY; //成交额
    private String CGENERATEFLAG; //下单类型

    public String getDATE() {
        return DATE;
    }

    public void setDATE(String DATE) {
        this.DATE = DATE;
    }

    public String getFIRMID() {
        return FIRMID;
    }

    public void setFIRMID(String FIRMID) {
        this.FIRMID = FIRMID;
    }

    public String getSERIALNO() {
        return SERIALNO;
    }

    public void setSERIALNO(String SERIALNO) {
        this.SERIALNO = SERIALNO;
    }

    public String getCONTNO() {
        return CONTNO;
    }

    public void setCONTNO(String CONTNO) {
        this.CONTNO = CONTNO;
    }

    public String getWAREID() {
        return WAREID;
    }

    public void setWAREID(String WAREID) {
        this.WAREID = WAREID;
    }

    public String getBUYORSAL() {
        return BUYORSAL;
    }

    public void setBUYORSAL(String BUYORSAL) {
        this.BUYORSAL = BUYORSAL;
    }

    public String getCONPRICE() {
        return CONPRICE;
    }

    public void setCONPRICE(String CONPRICE) {
        this.CONPRICE = CONPRICE;
    }

    public String getCONTNUM() {
        return CONTNUM;
    }

    public void setCONTNUM(String CONTNUM) {
        this.CONTNUM = CONTNUM;
    }

    public String getFTIME() {
        return FTIME;
    }

    public void setFTIME(String FTIME) {
        this.FTIME = FTIME;
    }

    public String getTMPMONEY() {
        return TMPMONEY;
    }

    public void setTMPMONEY(String TMPMONEY) {
        this.TMPMONEY = TMPMONEY;
    }

    public String getORDERSTY() {
        return ORDERSTY;
    }

    public void setORDERSTY(String ORDERSTY) {
        this.ORDERSTY = ORDERSTY;
    }

    public String getCONTQTY() {
        return CONTQTY;
    }

    public void setCONTQTY(String CONTQTY) {
        this.CONTQTY = CONTQTY;
    }

    public String getCGENERATEFLAG() {
        return CGENERATEFLAG;
    }

    public void setCGENERATEFLAG(String CGENERATEFLAG) {
        this.CGENERATEFLAG = CGENERATEFLAG;
    }
}