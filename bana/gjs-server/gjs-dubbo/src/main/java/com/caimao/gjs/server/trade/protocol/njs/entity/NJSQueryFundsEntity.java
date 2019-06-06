package com.caimao.gjs.server.trade.protocol.njs.entity;

import java.io.Serializable;

/**
 * 查询资金
 */
public class NJSQueryFundsEntity implements Serializable{
    private String FDATE; //日期
    private String FIRMID; //交易商编号
    private String INMONEY; //当日入金
    private String OUTMONEY; //当日出金
    private String FINANCING; //融资金额
    private String FREEZEMONEY; //冻结资金
    private String BALANCEMONEY; //资金余额
    private String BAILMONEY; //当日保证金
    private String ENABLEMONEY; //可用资金
    private String ENABLEOUTMONEY; //可出资金
    private String ASSETTOTALVALUE; //资产总值
    private String ASSETNETVALUE; //资产净值
    private String PICKGOODVALUE; //可提货物市值
    private String FINANMARKETVAL; //融货市值
    private String SAFERATE; //安全率

    public String getFDATE() {
        return FDATE;
    }

    public void setFDATE(String FDATE) {
        this.FDATE = FDATE;
    }

    public String getFIRMID() {
        return FIRMID;
    }

    public void setFIRMID(String FIRMID) {
        this.FIRMID = FIRMID;
    }

    public String getINMONEY() {
        return INMONEY;
    }

    public void setINMONEY(String INMONEY) {
        this.INMONEY = INMONEY;
    }

    public String getOUTMONEY() {
        return OUTMONEY;
    }

    public void setOUTMONEY(String OUTMONEY) {
        this.OUTMONEY = OUTMONEY;
    }

    public String getFINANCING() {
        return FINANCING;
    }

    public void setFINANCING(String FINANCING) {
        this.FINANCING = FINANCING;
    }

    public String getFREEZEMONEY() {
        return FREEZEMONEY;
    }

    public void setFREEZEMONEY(String FREEZEMONEY) {
        this.FREEZEMONEY = FREEZEMONEY;
    }

    public String getBALANCEMONEY() {
        return BALANCEMONEY;
    }

    public void setBALANCEMONEY(String BALANCEMONEY) {
        this.BALANCEMONEY = BALANCEMONEY;
    }

    public String getBAILMONEY() {
        return BAILMONEY;
    }

    public void setBAILMONEY(String BAILMONEY) {
        this.BAILMONEY = BAILMONEY;
    }

    public String getENABLEMONEY() {
        return ENABLEMONEY;
    }

    public void setENABLEMONEY(String ENABLEMONEY) {
        this.ENABLEMONEY = ENABLEMONEY;
    }

    public String getENABLEOUTMONEY() {
        return ENABLEOUTMONEY;
    }

    public void setENABLEOUTMONEY(String ENABLEOUTMONEY) {
        this.ENABLEOUTMONEY = ENABLEOUTMONEY;
    }

    public String getASSETTOTALVALUE() {
        return ASSETTOTALVALUE;
    }

    public void setASSETTOTALVALUE(String ASSETTOTALVALUE) {
        this.ASSETTOTALVALUE = ASSETTOTALVALUE;
    }

    public String getASSETNETVALUE() {
        return ASSETNETVALUE;
    }

    public void setASSETNETVALUE(String ASSETNETVALUE) {
        this.ASSETNETVALUE = ASSETNETVALUE;
    }

    public String getPICKGOODVALUE() {
        return PICKGOODVALUE;
    }

    public void setPICKGOODVALUE(String PICKGOODVALUE) {
        this.PICKGOODVALUE = PICKGOODVALUE;
    }

    public String getFINANMARKETVAL() {
        return FINANMARKETVAL;
    }

    public void setFINANMARKETVAL(String FINANMARKETVAL) {
        this.FINANMARKETVAL = FINANMARKETVAL;
    }

    public String getSAFERATE() {
        return SAFERATE;
    }

    public void setSAFERATE(String SAFERATE) {
        this.SAFERATE = SAFERATE;
    }
}