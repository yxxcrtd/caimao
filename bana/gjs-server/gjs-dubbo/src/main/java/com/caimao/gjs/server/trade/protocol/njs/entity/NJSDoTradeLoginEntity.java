package com.caimao.gjs.server.trade.protocol.njs.entity;

import java.io.Serializable;

/**
 * 交易登录
 */
public class NJSDoTradeLoginEntity implements Serializable {
    private String SYSDATE; //结算日期
    private String SYSTIME; //系统时间
    private String COUNTDATE; //前结算日期
    private String MEMBER; //所属交易商编号
    private String SCURSYSDATE; //系统日期
    private String CJSTRADESTA; //即时交收交易状态
    private String CJSTRADEATE; //即时交收系统状态
    private String CSETPROTOCOL; //签订委托交收协议 Y 已签订 N 未签订

    public String getSYSDATE() {
        return SYSDATE;
    }

    public void setSYSDATE(String SYSDATE) {
        this.SYSDATE = SYSDATE;
    }

    public String getSYSTIME() {
        return SYSTIME;
    }

    public void setSYSTIME(String SYSTIME) {
        this.SYSTIME = SYSTIME;
    }

    public String getCOUNTDATE() {
        return COUNTDATE;
    }

    public void setCOUNTDATE(String COUNTDATE) {
        this.COUNTDATE = COUNTDATE;
    }

    public String getMEMBER() {
        return MEMBER;
    }

    public void setMEMBER(String MEMBER) {
        this.MEMBER = MEMBER;
    }

    public String getSCURSYSDATE() {
        return SCURSYSDATE;
    }

    public void setSCURSYSDATE(String SCURSYSDATE) {
        this.SCURSYSDATE = SCURSYSDATE;
    }

    public String getCJSTRADESTA() {
        return CJSTRADESTA;
    }

    public void setCJSTRADESTA(String CJSTRADESTA) {
        this.CJSTRADESTA = CJSTRADESTA;
    }

    public String getCJSTRADEATE() {
        return CJSTRADEATE;
    }

    public void setCJSTRADEATE(String CJSTRADEATE) {
        this.CJSTRADEATE = CJSTRADEATE;
    }

    public String getCSETPROTOCOL() {
        return CSETPROTOCOL;
    }

    public void setCSETPROTOCOL(String CSETPROTOCOL) {
        this.CSETPROTOCOL = CSETPROTOCOL;
    }
}