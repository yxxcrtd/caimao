package com.caimao.gjs.server.trade.protocol.njs.entity;

import java.io.Serializable;

/**
 * 查询银行资产
 */
public class NJSQueryBankFundsEntity implements Serializable {
    private String BANKWATERID; //银行流水号
    private String BANKID; //银行编号
    private String BANKNAME; //银行名称
    private String CUSTBANKACCTNO; //客户银行账号
    private String MONEYTYPE; //币种 0人民币 1美元 2港币 3英镑 4法郎 5日元 6欧元
    private String BANKEOUTMONEY; //银行可出
    private String BANKENABLEMONEY; //银行可用

    public String getBANKWATERID() {
        return BANKWATERID;
    }

    public void setBANKWATERID(String BANKWATERID) {
        this.BANKWATERID = BANKWATERID;
    }

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

    public String getCUSTBANKACCTNO() {
        return CUSTBANKACCTNO;
    }

    public void setCUSTBANKACCTNO(String CUSTBANKACCTNO) {
        this.CUSTBANKACCTNO = CUSTBANKACCTNO;
    }

    public String getMONEYTYPE() {
        return MONEYTYPE;
    }

    public void setMONEYTYPE(String MONEYTYPE) {
        this.MONEYTYPE = MONEYTYPE;
    }

    public String getBANKEOUTMONEY() {
        return BANKEOUTMONEY;
    }

    public void setBANKEOUTMONEY(String BANKEOUTMONEY) {
        this.BANKEOUTMONEY = BANKEOUTMONEY;
    }

    public String getBANKENABLEMONEY() {
        return BANKENABLEMONEY;
    }

    public void setBANKENABLEMONEY(String BANKENABLEMONEY) {
        this.BANKENABLEMONEY = BANKENABLEMONEY;
    }
}