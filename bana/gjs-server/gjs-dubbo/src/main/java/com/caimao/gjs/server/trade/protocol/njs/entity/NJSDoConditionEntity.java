package com.caimao.gjs.server.trade.protocol.njs.entity;

import java.io.Serializable;

/**
 * 下条件单
 */
public class NJSDoConditionEntity implements Serializable {
    private String FDATE; //申请日期
    private String SERIALNO; //委托编号

    public String getFDATE() {
        return FDATE;
    }

    public void setFDATE(String FDATE) {
        this.FDATE = FDATE;
    }

    public String getSERIALNO() {
        return SERIALNO;
    }

    public void setSERIALNO(String SERIALNO) {
        this.SERIALNO = SERIALNO;
    }
}