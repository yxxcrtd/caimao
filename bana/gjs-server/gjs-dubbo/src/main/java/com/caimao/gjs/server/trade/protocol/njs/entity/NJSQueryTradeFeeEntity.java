package com.caimao.gjs.server.trade.protocol.njs.entity;

import java.io.Serializable;

/**
 * 查询交易手续费
 */
public class NJSQueryTradeFeeEntity implements Serializable {
    private String WAREID; //商品代码
    private String SWARENAME; //品种名称
    private String BCOMM; //买手续费
    private String SCOMM; //卖手续费
    private String TOTALCOMM; //总手续费

    public String getWAREID() {
        return WAREID;
    }

    public void setWAREID(String WAREID) {
        this.WAREID = WAREID;
    }

    public String getSWARENAME() {
        return SWARENAME;
    }

    public void setSWARENAME(String SWARENAME) {
        this.SWARENAME = SWARENAME;
    }

    public String getBCOMM() {
        return BCOMM;
    }

    public void setBCOMM(String BCOMM) {
        this.BCOMM = BCOMM;
    }

    public String getSCOMM() {
        return SCOMM;
    }

    public void setSCOMM(String SCOMM) {
        this.SCOMM = SCOMM;
    }

    public String getTOTALCOMM() {
        return TOTALCOMM;
    }

    public void setTOTALCOMM(String TOTALCOMM) {
        this.TOTALCOMM = TOTALCOMM;
    }
}