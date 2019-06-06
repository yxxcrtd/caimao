package com.caimao.gjs.server.trade.protocol.njs.entity;

import java.io.Serializable;

/**
 * 查询用户是否已经签约
 */
public class NJSQueryTradeUserIsSignEntity implements Serializable {
    private String SIGNUP; //是否已签约
    private String SIGNDATE; //签约日期

    public String getSIGNUP() {
        return SIGNUP;
    }

    public void setSIGNUP(String SIGNUP) {
        this.SIGNUP = SIGNUP;
    }

    public String getSIGNDATE() {
        return SIGNDATE;
    }

    public void setSIGNDATE(String SIGNDATE) {
        this.SIGNDATE = SIGNDATE;
    }
}