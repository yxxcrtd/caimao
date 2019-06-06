package com.caimao.gjs.server.trade.protocol.njs.entity;

import java.io.Serializable;

/**
 * 查询转账记录
 */
public class NJSQueryTransferEntity implements Serializable {
    private String FIRMID; //交易商号
    private String CHANGETYPE; //变动类型 A入金 B出金 C质押 D解除质押 E银行入金 F银行出金
    private String MONEYTYPE; //币种 0人民币 1美元 2港币
    private String CHANGEMONEY; //变动资金
    private String DEALDATE; //处理日期 格式：YYYYMMDD
    private String DEALTIME; //处理时间 格式：HHMMSS
    private String FLAG; //出入金标记 A结算出入金 B在线出入金 C银行出入金
    private String STYLE; //资金状态
    private String USERID; //管理员代码
    private String BANKWATERID; //出入金流水号

    public String getFIRMID() {
        return FIRMID;
    }

    public void setFIRMID(String FIRMID) {
        this.FIRMID = FIRMID;
    }

    public String getCHANGETYPE() {
        return CHANGETYPE;
    }

    public void setCHANGETYPE(String CHANGETYPE) {
        this.CHANGETYPE = CHANGETYPE;
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

    public String getDEALDATE() {
        return DEALDATE;
    }

    public void setDEALDATE(String DEALDATE) {
        this.DEALDATE = DEALDATE;
    }

    public String getDEALTIME() {
        return DEALTIME;
    }

    public void setDEALTIME(String DEALTIME) {
        this.DEALTIME = DEALTIME;
    }

    public String getFLAG() {
        return FLAG;
    }

    public void setFLAG(String FLAG) {
        this.FLAG = FLAG;
    }

    public String getSTYLE() {
        return STYLE;
    }

    public void setSTYLE(String STYLE) {
        this.STYLE = STYLE;
    }

    public String getUSERID() {
        return USERID;
    }

    public void setUSERID(String USERID) {
        this.USERID = USERID;
    }

    public String getBANKWATERID() {
        return BANKWATERID;
    }

    public void setBANKWATERID(String BANKWATERID) {
        this.BANKWATERID = BANKWATERID;
    }
}