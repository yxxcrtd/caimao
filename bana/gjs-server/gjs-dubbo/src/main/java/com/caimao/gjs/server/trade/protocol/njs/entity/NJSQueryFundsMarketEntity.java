package com.caimao.gjs.server.trade.protocol.njs.entity;

import java.io.Serializable;

/**
 * 查询市场资金
 */
public class NJSQueryFundsMarketEntity implements Serializable {
    private String CUSTTRADEID; //客户交易账号
    private String COMPENABLEMONEY; //可用资金
    private String COMPOUTMONEY; //可取资金

    public String getCUSTTRADEID() {
        return CUSTTRADEID;
    }

    public void setCUSTTRADEID(String CUSTTRADEID) {
        this.CUSTTRADEID = CUSTTRADEID;
    }

    public String getCOMPENABLEMONEY() {
        return COMPENABLEMONEY;
    }

    public void setCOMPENABLEMONEY(String COMPENABLEMONEY) {
        this.COMPENABLEMONEY = COMPENABLEMONEY;
    }

    public String getCOMPOUTMONEY() {
        return COMPOUTMONEY;
    }

    public void setCOMPOUTMONEY(String COMPOUTMONEY) {
        this.COMPOUTMONEY = COMPOUTMONEY;
    }
}