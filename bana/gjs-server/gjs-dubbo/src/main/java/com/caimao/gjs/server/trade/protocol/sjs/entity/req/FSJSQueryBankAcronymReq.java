package com.caimao.gjs.server.trade.protocol.sjs.entity.req;

import java.io.Serializable;

public class FSJSQueryBankAcronymReq implements Serializable{
    private String bank_no;

    public String getBank_no() {
        return bank_no;
    }

    public void setBank_no(String bank_no) {
        this.bank_no = bank_no;
    }
}
