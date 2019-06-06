package com.caimao.gjs.server.trade.protocol.njs.entity.res;

public class FNJSQueryTradeUserIsSignRes{
    private String ADAPTER; //协议号
    private String STATE; //状态 0为成功， 错误对应相应的错误代码
    private String MSG; //错误信息
    private String SIGNUP; //是否已签约
    private String SIGNDATE; //签约日期

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