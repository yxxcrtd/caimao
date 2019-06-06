package com.caimao.gjs.server.trade.protocol.njs.entity;

import java.io.Serializable;

/**
 * 出金
 */
public class NJSDoBankTransferOutEntity implements Serializable {
    private String BANKWATERID; //银行流水号
    private String BANKID; //银行编号
    private String BANKNAME; //银行名称
    private String CUSTBANKACCTNO; //客户银行账号
    private String CUSTTRADEID; //客户交易账号
    private String MONEYTYPE; //币种
    private String CHANGEMONEY; //转账资金
    private String COMPENABLEMONEY; //存管余额
    private String BANKENABLEMONEY; //银行余额
    private String MEMO; //业务名称

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

    public String getCUSTTRADEID() {
        return CUSTTRADEID;
    }

    public void setCUSTTRADEID(String CUSTTRADEID) {
        this.CUSTTRADEID = CUSTTRADEID;
    }

    public String getMONEYTYPE() {
        return MONEYTYPE;
    }

    public void setMONEYTYPE(String MONEYTYPE) {
        this.MONEYTYPE = MONEYTYPE;
    }

    public String getCHANGEMONEY() {
        return CHANGEMONEY;
    }

    public void setCHANGEMONEY(String CHANGEMONEY) {
        this.CHANGEMONEY = CHANGEMONEY;
    }

    public String getCOMPENABLEMONEY() {
        return COMPENABLEMONEY;
    }

    public void setCOMPENABLEMONEY(String COMPENABLEMONEY) {
        this.COMPENABLEMONEY = COMPENABLEMONEY;
    }

    public String getBANKENABLEMONEY() {
        return BANKENABLEMONEY;
    }

    public void setBANKENABLEMONEY(String BANKENABLEMONEY) {
        this.BANKENABLEMONEY = BANKENABLEMONEY;
    }

    public String getMEMO() {
        return MEMO;
    }

    public void setMEMO(String MEMO) {
        this.MEMO = MEMO;
    }
}