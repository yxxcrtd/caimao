package com.caimao.gjs.server.trade.protocol.sjs.entity.req;

import java.io.Serializable;

public class FSJSAdminDoResetFundsPwdReq implements Serializable{
    private String acct_no; //客户编号
    private String new_fund_pwd; //新资金密码

    public String getAcct_no() {
        return acct_no;
    }

    public void setAcct_no(String acct_no) {
        this.acct_no = acct_no;
    }

    public String getNew_fund_pwd() {
        return new_fund_pwd;
    }

    public void setNew_fund_pwd(String new_fund_pwd) {
        this.new_fund_pwd = new_fund_pwd;
    }
}
