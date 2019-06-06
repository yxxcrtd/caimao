package com.caimao.gjs.server.trade.protocol.njs.entity.res;

public class FNJSQueryFundsManageRes {
    private String ADAPTER; //协议编号
    private String STATE; //状态
    private String MSG; //错误信息
    private String BANKID; //银行编号
    private String BANKNAME; //银行名称
    private String CUSTTRADEID; //对应会员号
    private String CUSTBANKACCTNO; //客户银行账号
    private String MONEYTYPE; //币种 0人民币 1美元 2港币 3英镑 4法郎 5日元 6欧元
    private String COMPID; //市场公司编号
    private String BANKTYPE; //银行转账类型 0正常转账类型 1网银入金 2网银出金 3网银出/入金 4混合模式
    private String URL; //网银地址
    private String IFRATE; //是否收取手续费 0收取 1不收取
    private String TRADEFUNCLIST; //交易功能
    private String TRADEFUNCPWD; //必输银行密码交易
    private String INNEEDPWD; //入金是否需要密码 Y需要 N不需要
    private String OUTNEEDPWD; //出金是否需要密码 Y需要 N不需要
    private String QUERYNEEDPWD; //查询银行余额是否需要密码 Y需要 N不需要
    private String SIGN; //本行标记 0本行 1他行

    public String getADAPTER() {
        return ADAPTER;
    }

    public void setADAPTER(String ADAPTER) {
        this.ADAPTER = ADAPTER;
    }

    public String getSTATE() {
        return STATE;
    }

    public void setSTATE(String STATE) {
        this.STATE = STATE;
    }

    public String getMSG() {
        return MSG;
    }

    public void setMSG(String MSG) {
        this.MSG = MSG;
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

    public String getMONEYTYPE() {
        return MONEYTYPE;
    }

    public void setMONEYTYPE(String MONEYTYPE) {
        this.MONEYTYPE = MONEYTYPE;
    }

    public String getCOMPID() {
        return COMPID;
    }

    public void setCOMPID(String COMPID) {
        this.COMPID = COMPID;
    }

    public String getBANKTYPE() {
        return BANKTYPE;
    }

    public void setBANKTYPE(String BANKTYPE) {
        this.BANKTYPE = BANKTYPE;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getIFRATE() {
        return IFRATE;
    }

    public void setIFRATE(String IFRATE) {
        this.IFRATE = IFRATE;
    }

    public String getTRADEFUNCLIST() {
        return TRADEFUNCLIST;
    }

    public void setTRADEFUNCLIST(String TRADEFUNCLIST) {
        this.TRADEFUNCLIST = TRADEFUNCLIST;
    }

    public String getTRADEFUNCPWD() {
        return TRADEFUNCPWD;
    }

    public void setTRADEFUNCPWD(String TRADEFUNCPWD) {
        this.TRADEFUNCPWD = TRADEFUNCPWD;
    }

    public String getINNEEDPWD() {
        return INNEEDPWD;
    }

    public void setINNEEDPWD(String INNEEDPWD) {
        this.INNEEDPWD = INNEEDPWD;
    }

    public String getOUTNEEDPWD() {
        return OUTNEEDPWD;
    }

    public void setOUTNEEDPWD(String OUTNEEDPWD) {
        this.OUTNEEDPWD = OUTNEEDPWD;
    }

    public String getQUERYNEEDPWD() {
        return QUERYNEEDPWD;
    }

    public void setQUERYNEEDPWD(String QUERYNEEDPWD) {
        this.QUERYNEEDPWD = QUERYNEEDPWD;
    }

    public String getSIGN() {
        return SIGN;
    }

    public void setSIGN(String SIGN) {
        this.SIGN = SIGN;
    }
}