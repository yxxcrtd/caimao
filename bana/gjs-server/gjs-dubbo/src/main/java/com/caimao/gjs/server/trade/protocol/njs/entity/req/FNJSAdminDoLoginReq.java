package com.caimao.gjs.server.trade.protocol.njs.entity.req;

import java.io.Serializable;

public class FNJSAdminDoLoginReq implements Serializable{
    private String USERID; //用户编号
    private String USERPWD; //用户密码
    private String IPADDRESS; //ip地址

    public String getUSERID() {
        return USERID;
    }

    public void setUSERID(String USERID) {
        this.USERID = USERID;
    }

    public String getUSERPWD() {
        return USERPWD;
    }

    public void setUSERPWD(String USERPWD) {
        this.USERPWD = USERPWD;
    }

    public String getIPADDRESS() {
        return IPADDRESS;
    }

    public void setIPADDRESS(String IPADDRESS) {
        this.IPADDRESS = IPADDRESS;
    }
}