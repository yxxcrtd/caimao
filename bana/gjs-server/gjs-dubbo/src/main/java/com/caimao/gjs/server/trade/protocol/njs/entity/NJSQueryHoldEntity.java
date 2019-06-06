package com.caimao.gjs.server.trade.protocol.njs.entity;

import java.io.Serializable;

/**
 * 查询持仓
 */
public class NJSQueryHoldEntity implements Serializable {
    private String FIRMID; //交易商编号
    private String WAREID; //商品代码
    private String SWARENAME; //品种名称
    private String GOODSNUM; //买入量
    private String GOODSVALUE; //货物市值
    private String RZMONEY; //融资余额
    private String RHNUMBER; //卖出量
    private String RHVALUE; //融货市值
    private String BAVGPRICE; //买入均价
    private String SAVGPRICE; //卖出均价
    private String CONSULTFLAT; //参考盈亏
    private String FLATSCALE; //盈亏比例
    private String CONSULTMARKVAL; //参考市值
    private String CONSULTCOST; //参考成本价
    private String NEWPRICE; //参考价

    public String getFIRMID() {
        return FIRMID;
    }

    public void setFIRMID(String FIRMID) {
        this.FIRMID = FIRMID;
    }

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

    public String getGOODSNUM() {
        return GOODSNUM;
    }

    public void setGOODSNUM(String GOODSNUM) {
        this.GOODSNUM = GOODSNUM;
    }

    public String getGOODSVALUE() {
        return GOODSVALUE;
    }

    public void setGOODSVALUE(String GOODSVALUE) {
        this.GOODSVALUE = GOODSVALUE;
    }

    public String getRZMONEY() {
        return RZMONEY;
    }

    public void setRZMONEY(String RZMONEY) {
        this.RZMONEY = RZMONEY;
    }

    public String getRHNUMBER() {
        return RHNUMBER;
    }

    public void setRHNUMBER(String RHNUMBER) {
        this.RHNUMBER = RHNUMBER;
    }

    public String getRHVALUE() {
        return RHVALUE;
    }

    public void setRHVALUE(String RHVALUE) {
        this.RHVALUE = RHVALUE;
    }

    public String getBAVGPRICE() {
        return BAVGPRICE;
    }

    public void setBAVGPRICE(String BAVGPRICE) {
        this.BAVGPRICE = BAVGPRICE;
    }

    public String getSAVGPRICE() {
        return SAVGPRICE;
    }

    public void setSAVGPRICE(String SAVGPRICE) {
        this.SAVGPRICE = SAVGPRICE;
    }

    public String getCONSULTFLAT() {
        return CONSULTFLAT;
    }

    public void setCONSULTFLAT(String CONSULTFLAT) {
        this.CONSULTFLAT = CONSULTFLAT;
    }

    public String getFLATSCALE() {
        return FLATSCALE;
    }

    public void setFLATSCALE(String FLATSCALE) {
        this.FLATSCALE = FLATSCALE;
    }

    public String getCONSULTMARKVAL() {
        return CONSULTMARKVAL;
    }

    public void setCONSULTMARKVAL(String CONSULTMARKVAL) {
        this.CONSULTMARKVAL = CONSULTMARKVAL;
    }

    public String getCONSULTCOST() {
        return CONSULTCOST;
    }

    public void setCONSULTCOST(String CONSULTCOST) {
        this.CONSULTCOST = CONSULTCOST;
    }

    public String getNEWPRICE() {
        return NEWPRICE;
    }

    public void setNEWPRICE(String NEWPRICE) {
        this.NEWPRICE = NEWPRICE;
    }
}