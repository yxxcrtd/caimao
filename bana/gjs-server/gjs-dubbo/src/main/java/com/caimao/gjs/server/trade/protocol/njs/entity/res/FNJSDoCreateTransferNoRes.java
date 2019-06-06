package com.caimao.gjs.server.trade.protocol.njs.entity.res;

public class FNJSDoCreateTransferNoRes {
    private String ADAPTER; //协议号
    private String STATE; //状态 0为成功， 错误对应相应的错误代码
    private String MSG; //错误信息
    private String RETCODE; //返回代码
    private String RETINFO; //描述
    private String MERID; //商户编号
    private String ORDERNO; //订单编号

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

    public String getRETCODE() {
        return RETCODE;
    }

    public void setRETCODE(String RETCODE) {
        this.RETCODE = RETCODE;
    }

    public String getRETINFO() {
        return RETINFO;
    }

    public void setRETINFO(String RETINFO) {
        this.RETINFO = RETINFO;
    }

    public String getMERID() {
        return MERID;
    }

    public void setMERID(String MERID) {
        this.MERID = MERID;
    }

    public String getORDERNO() {
        return ORDERNO;
    }

    public void setORDERNO(String ORDERNO) {
        this.ORDERNO = ORDERNO;
    }
}