package com.caimao.gjs.server.trade.protocol.sjs.entity;

import java.io.Serializable;

/**
 * 查询签约银行编码
 */
public class SJSQuerySignedBankCodeEntity implements Serializable {
    private String bank_no; //银行代码

    public String getBank_no() {
        return bank_no;
    }

    public void setBank_no(String bank_no) {
        this.bank_no = bank_no;
    }
}