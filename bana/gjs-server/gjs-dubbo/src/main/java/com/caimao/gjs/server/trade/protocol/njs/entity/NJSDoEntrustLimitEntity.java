package com.caimao.gjs.server.trade.protocol.njs.entity;

import java.io.Serializable;

/**
 * 下限价单
 */
public class NJSDoEntrustLimitEntity implements Serializable {
    private String SERIALNO; //委托编号

    public String getSERIALNO() {
        return SERIALNO;
    }

    public void setSERIALNO(String SERIALNO) {
        this.SERIALNO = SERIALNO;
    }
}