package com.caimao.gjs.server.trade.protocol.njs.entity;

import java.io.Serializable;

/**
 * 套餐列表查询
 */
public class NJSQueryPackageListEntity implements Serializable {
    private String TAOCANID; //套餐编号
    private String TAOCANNAME; //套餐名称
    private String CONTMONEY; //成交额
    private String COMM; //套餐费用
    private String TIMELIMIT; //有效期限
    private String MEMO; //详细说明

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

    public String getCOMM() {
        return COMM;
    }

    public void setCOMM(String COMM) {
        this.COMM = COMM;
    }

    public String getTIMELIMIT() {
        return TIMELIMIT;
    }

    public void setTIMELIMIT(String TIMELIMIT) {
        this.TIMELIMIT = TIMELIMIT;
    }

    public String getMEMO() {
        return MEMO;
    }

    public void setMEMO(String MEMO) {
        this.MEMO = MEMO;
    }
}