package com.caimao.gjs.server.trade.protocol.njs.entity;

import java.io.Serializable;

/**
 * 查询止盈止损单
 */
public class NJSQueryFullStopEntity implements Serializable {
    private String FDATE; //申请日期
    private String BILLNO; //单号
    private String WAREID; //商品代码
    private String BUYORSAL; //买/卖 B买 S卖
    private String UPPRICE; //止盈价
    private String DOWNPRICE; //止损价
    private String NUM; //数量
    private String STATE; //状态 A.待发 B.废单 C.已发 D.已撤
    private String SETTIME; //设置时间 格式 HHMMSS
    private String CANCELTIME; //撤单时间 格式 HHMMSS
    private String EXECTIME; //触发时间 格式 HHMMSS
    private String SERIALNO; //委托单号
    private String SERIALNUM; //委托数量
    private String REMARK; //备注
    private String VALIDDATE; //有效日期

    public String getFDATE() {
        return FDATE;
    }

    public void setFDATE(String FDATE) {
        this.FDATE = FDATE;
    }

    public String getBILLNO() {
        return BILLNO;
    }

    public void setBILLNO(String BILLNO) {
        this.BILLNO = BILLNO;
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

    public String getSTATE() {
        return STATE;
    }

    public void setSTATE(String STATE) {
        this.STATE = STATE;
    }

    public String getSETTIME() {
        return SETTIME;
    }

    public void setSETTIME(String SETTIME) {
        this.SETTIME = SETTIME;
    }

    public String getCANCELTIME() {
        return CANCELTIME;
    }

    public void setCANCELTIME(String CANCELTIME) {
        this.CANCELTIME = CANCELTIME;
    }

    public String getEXECTIME() {
        return EXECTIME;
    }

    public void setEXECTIME(String EXECTIME) {
        this.EXECTIME = EXECTIME;
    }

    public String getSERIALNO() {
        return SERIALNO;
    }

    public void setSERIALNO(String SERIALNO) {
        this.SERIALNO = SERIALNO;
    }

    public String getSERIALNUM() {
        return SERIALNUM;
    }

    public void setSERIALNUM(String SERIALNUM) {
        this.SERIALNUM = SERIALNUM;
    }

    public String getREMARK() {
        return REMARK;
    }

    public void setREMARK(String REMARK) {
        this.REMARK = REMARK;
    }

    public String getVALIDDATE() {
        return VALIDDATE;
    }

    public void setVALIDDATE(String VALIDDATE) {
        this.VALIDDATE = VALIDDATE;
    }
}