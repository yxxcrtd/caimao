package com.caimao.gjs.server.trade.protocol.njs.entity;

import java.io.Serializable;

/**
 * 查询银行转账记录
 */
public class NJSQueryBankTransferEntity implements Serializable {
    private String BANKID; //银行编号
    private String BANKNAME; //银行名称
    private String COMPWATERID; //转账流水号
    private String CUSTTRADEID; //对应会员号
    private String CUSTBANKACCTNO; //客户银行账号
    private String CUSTMONEYPWD; //客户资金密码
    private String FIRMNAME; //会员姓名
    private String CARDIDTYPE; //客户证件类型
    private String CARDID; //客户证件号
    private String TRADETYPE; //交易类型 0市场转银行 1银行转市场
    private String BUSINETYPE; //业务类型 0普通转账 1外汇业务
    private String MONEYSTY; //资金类型 0保证金 1贷款 2贷款入金
    private String MONEYTYPE; //币种 0人民币 1美元 2港币 3英镑 4法郎 5日元 6欧元
    private String CHANGEMONEY;//银行变动资金
    private String WATERSTATE; //流水状态 0成功 1存疑 2失败 3已冲正 4流水不存在
    private String CHECKSTATE; //审核状态 0未审核 1终审核 2初审 3初始否决 4终审否决 5待确认
    private String MEMO; //业务备注
    private String TRADEDATE; //交易日期
    private String TRADETIME; //交易时间
    private String MONEYCOUNTO; //市场变动资金
    private String TRASCOMM; //转账手续费
    private String MONEYCOUNTT; //变动总资金
    private String TRADESTART; //交易发起方

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

    public String getCOMPWATERID() {
        return COMPWATERID;
    }

    public void setCOMPWATERID(String COMPWATERID) {
        this.COMPWATERID = COMPWATERID;
    }

    public String getCUSTTRADEID() {
        return CUSTTRADEID;
    }

    public void setCUSTTRADEID(String CUSTTRADEID) {
        this.CUSTTRADEID = CUSTTRADEID;
    }

    public String getCUSTBANKACCTNO() {
        return CUSTBANKACCTNO;
    }

    public void setCUSTBANKACCTNO(String CUSTBANKACCTNO) {
        this.CUSTBANKACCTNO = CUSTBANKACCTNO;
    }

    public String getCUSTMONEYPWD() {
        return CUSTMONEYPWD;
    }

    public void setCUSTMONEYPWD(String CUSTMONEYPWD) {
        this.CUSTMONEYPWD = CUSTMONEYPWD;
    }

    public String getFIRMNAME() {
        return FIRMNAME;
    }

    public void setFIRMNAME(String FIRMNAME) {
        this.FIRMNAME = FIRMNAME;
    }

    public String getCARDIDTYPE() {
        return CARDIDTYPE;
    }

    public void setCARDIDTYPE(String CARDIDTYPE) {
        this.CARDIDTYPE = CARDIDTYPE;
    }

    public String getCARDID() {
        return CARDID;
    }

    public void setCARDID(String CARDID) {
        this.CARDID = CARDID;
    }

    public String getTRADETYPE() {
        return TRADETYPE;
    }

    public void setTRADETYPE(String TRADETYPE) {
        this.TRADETYPE = TRADETYPE;
    }

    public String getBUSINETYPE() {
        return BUSINETYPE;
    }

    public void setBUSINETYPE(String BUSINETYPE) {
        this.BUSINETYPE = BUSINETYPE;
    }

    public String getMONEYSTY() {
        return MONEYSTY;
    }

    public void setMONEYSTY(String MONEYSTY) {
        this.MONEYSTY = MONEYSTY;
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

    public String getWATERSTATE() {
        return WATERSTATE;
    }

    public void setWATERSTATE(String WATERSTATE) {
        this.WATERSTATE = WATERSTATE;
    }

    public String getCHECKSTATE() {
        return CHECKSTATE;
    }

    public void setCHECKSTATE(String CHECKSTATE) {
        this.CHECKSTATE = CHECKSTATE;
    }

    public String getMEMO() {
        return MEMO;
    }

    public void setMEMO(String MEMO) {
        this.MEMO = MEMO;
    }

    public String getTRADEDATE() {
        return TRADEDATE;
    }

    public void setTRADEDATE(String TRADEDATE) {
        this.TRADEDATE = TRADEDATE;
    }

    public String getTRADETIME() {
        return TRADETIME;
    }

    public void setTRADETIME(String TRADETIME) {
        this.TRADETIME = TRADETIME;
    }

    public String getMONEYCOUNTO() {
        return MONEYCOUNTO;
    }

    public void setMONEYCOUNTO(String MONEYCOUNTO) {
        this.MONEYCOUNTO = MONEYCOUNTO;
    }

    public String getTRASCOMM() {
        return TRASCOMM;
    }

    public void setTRASCOMM(String TRASCOMM) {
        this.TRASCOMM = TRASCOMM;
    }

    public String getMONEYCOUNTT() {
        return MONEYCOUNTT;
    }

    public void setMONEYCOUNTT(String MONEYCOUNTT) {
        this.MONEYCOUNTT = MONEYCOUNTT;
    }

    public String getTRADESTART() {
        return TRADESTART;
    }

    public void setTRADESTART(String TRADESTART) {
        this.TRADESTART = TRADESTART;
    }
}