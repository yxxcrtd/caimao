package com.caimao.gjs.server.trade.protocol.sjs.entity.req;

import java.io.Serializable;

public class FSJSAdminDoResetTradePwdReq implements Serializable{
    private String acct_no; //客户编号
    private String new_exch_pwd; //新交易密码

    public String getAcct_no() {
        return acct_no;
    }

    public void setAcct_no(String acct_no) {
        this.acct_no = acct_no;
    }

    public String getNew_exch_pwd() {
        return new_exch_pwd;
    }

    public void setNew_exch_pwd(String new_exch_pwd) {
        this.new_exch_pwd = new_exch_pwd;
    }
}
