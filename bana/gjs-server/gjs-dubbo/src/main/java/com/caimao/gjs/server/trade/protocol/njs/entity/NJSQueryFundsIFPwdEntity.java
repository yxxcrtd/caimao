package com.caimao.gjs.server.trade.protocol.njs.entity;

import java.io.Serializable;

/**
 * 查询查询资金是否需要密码
 */
public class NJSQueryFundsIFPwdEntity implements Serializable {
    private String NEEDFLAG; //是否需要密码
    private String PASSTYPE; //密码类型

    public String getNEEDFLAG() {
        return NEEDFLAG;
    }

    public void setNEEDFLAG(String NEEDFLAG) {
        this.NEEDFLAG = NEEDFLAG;
    }

    public String getPASSTYPE() {
        return PASSTYPE;
    }

    public void setPASSTYPE(String PASSTYPE) {
        this.PASSTYPE = PASSTYPE;
    }
}