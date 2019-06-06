package com.caimao.gjs.server.trade.protocol.njs.entity;

import java.io.Serializable;

/**
 * 查询所有品种
 */
public class NJSQueryWareAllEntity implements Serializable {
    private String WAREID; //品种编码
    private String WARENAME; //品种名称
    private String SWAREKIND; //品种大类
    private String SKINDNAME; //打雷名称
    private String WPOINT; //点值

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

    public String getSWAREKIND() {
        return SWAREKIND;
    }

    public void setSWAREKIND(String SWAREKIND) {
        this.SWAREKIND = SWAREKIND;
    }

    public String getSKINDNAME() {
        return SKINDNAME;
    }

    public void setSKINDNAME(String SKINDNAME) {
        this.SKINDNAME = SKINDNAME;
    }

    public String getWPOINT() {
        return WPOINT;
    }

    public void setWPOINT(String WPOINT) {
        this.WPOINT = WPOINT;
    }
}