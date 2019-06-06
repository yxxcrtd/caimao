package com.caimao.gjs.server.trade.protocol.njs.entity;

import java.io.Serializable;

/**
 * 查询风险交易商
 */
public class NJSQueryRiskEntity implements Serializable {
    private String FIRMID; //交易员编号
    private String SAFERATE; //风险率

    public String getFIRMID() {
        return FIRMID;
    }

    public void setFIRMID(String FIRMID) {
        this.FIRMID = FIRMID;
    }

    public String getSAFERATE() {
        return SAFERATE;
    }

    public void setSAFERATE(String SAFERATE) {
        this.SAFERATE = SAFERATE;
    }
}