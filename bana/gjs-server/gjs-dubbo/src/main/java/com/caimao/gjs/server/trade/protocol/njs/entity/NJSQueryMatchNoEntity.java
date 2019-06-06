package com.caimao.gjs.server.trade.protocol.njs.entity;

import java.io.Serializable;

/**
 * 查询未成交
 */
public class NJSQueryMatchNoEntity implements Serializable {
    private String DATE; //成交日期
    private String FIRMID; //交易商编号
    private String SERIALNO; //委托编号
    private String WAREID; //品种代码
    private String BUYORSAL; //买/卖
    private String PRICE; //价格
    private String NUM; //委托数量
    private String CONTNUM; //成交数量
    private String TIME; //委托时间
    private String BAILMONEY; //保证金
    private String TMPMONEY; //手续费
    private String CSTATUS; //交易单状态
    private String SOIPADDRESS; //委托ip地址
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

    public String getCONTNUM() {
        return CONTNUM;
    }

    public void setCONTNUM(String CONTNUM) {
        this.CONTNUM = CONTNUM;
    }

    public String getTIME() {
        return TIME;
    }

    public void setTIME(String TIME) {
        this.TIME = TIME;
    }

    public String getBAILMONEY() {
        return BAILMONEY;
    }

    public void setBAILMONEY(String BAILMONEY) {
        this.BAILMONEY = BAILMONEY;
    }

    public String getTMPMONEY() {
        return TMPMONEY;
    }

    public void setTMPMONEY(String TMPMONEY) {
        this.TMPMONEY = TMPMONEY;
    }

    public String getCSTATUS() {
        return CSTATUS;
    }

    public void setCSTATUS(String CSTATUS) {
        this.CSTATUS = CSTATUS;
    }

    public String getSOIPADDRESS() {
        return SOIPADDRESS;
    }

    public void setSOIPADDRESS(String SOIPADDRESS) {
        this.SOIPADDRESS = SOIPADDRESS;
    }

    public String getCGENERATEFLAG() {
        return CGENERATEFLAG;
    }

    public void setCGENERATEFLAG(String CGENERATEFLAG) {
        this.CGENERATEFLAG = CGENERATEFLAG;
    }
}