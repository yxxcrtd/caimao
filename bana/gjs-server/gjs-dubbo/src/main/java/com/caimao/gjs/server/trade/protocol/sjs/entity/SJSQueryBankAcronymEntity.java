package com.caimao.gjs.server.trade.protocol.sjs.entity;

import java.io.Serializable;

/**
 * 查询银行简称
 */
public class SJSQueryBankAcronymEntity implements Serializable {
    private String bank_abbr; //银行简称

    public String getBank_abbr() {
        return bank_abbr;
    }

    public void setBank_abbr(String bank_abbr) {
        this.bank_abbr = bank_abbr;
    }
}