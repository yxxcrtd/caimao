package com.caimao.gjs.server.trade.protocol.njs.entity;

import java.io.Serializable;

/**
 * 查询银行编号
 */
public class NJSQueryBankIdEntity implements Serializable {
    private String BANKID; //银行编号
    private String BANKNAME; //银行名称

    public String getBANKID() {
        return BANKID;
    }

    public void setBANKID(String BANKID) {
        this.BANKID = BANKID;
    }

    public String getBANKNAME() {
        return BANKNAME;
    }

    public void setBANKNAME(String BANKNAME) {
        this.BANKNAME = BANKNAME;
    }
}