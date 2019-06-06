package com.caimao.gjs.server.trade.protocol.njs.entity;

import java.io.Serializable;

/**
 * 套餐购买记录查询
 */
public class NJSQueryPackageHistoryEntity implements Serializable {
    private String FDATE; //申请日期
    private String WATERID; //申请流水
    private String FIRMID; //交易商编号
    private String REQTIME; //申请时间
    private String REQIPADDRESS; //申请ip
    private String TAOCANID; //套餐编号
    private String TAOCANNAME; //套餐名称
    private String CONTMONEY; //总值

    public String getFDATE() {
        return FDATE;
    }

    public void setFDATE(String FDATE) {
        this.FDATE = FDATE;
    }

    public String getWATERID() {
        return WATERID;
    }

    public void setWATERID(String WATERID) {
        this.WATERID = WATERID;
    }

    public String getFIRMID() {
        return FIRMID;
    }

    public void setFIRMID(String FIRMID) {
        this.FIRMID = FIRMID;
    }

    public String getREQTIME() {
        return REQTIME;
    }

    public void setREQTIME(String REQTIME) {
        this.REQTIME = REQTIME;
    }

    public String getREQIPADDRESS() {
        return REQIPADDRESS;
    }

    public void setREQIPADDRESS(String REQIPADDRESS) {
        this.REQIPADDRESS = REQIPADDRESS;
    }

    public String getTAOCANID() {
        return TAOCANID;
    }

    public void setTAOCANID(String TAOCANID) {
        this.TAOCANID = TAOCANID;
    }

    public String getTAOCANNAME() {
        return TAOCANNAME;
    }

    public void setTAOCANNAME(String TAOCANNAME) {
        this.TAOCANNAME = TAOCANNAME;
    }

    public String getCONTMONEY() {
        return CONTMONEY;
    }

    public void setCONTMONEY(String CONTMONEY) {
        this.CONTMONEY = CONTMONEY;
    }
}