package com.caimao.gjs.server.trade.protocol.sjs.entity.req;

import java.io.Serializable;

public class FSJSDoModifyPwdReq implements Serializable{
    private String exch_pwd; //新交易密码
    private String old_exch_pwd; //原交易密码
    private String fund_pwd; //新资金密码
    private String old_fund_pwd; //原资金密码

    public String getExch_pwd() {
        return exch_pwd;
    }

    public void setExch_pwd(String exch_pwd) {
        this.exch_pwd = exch_pwd;
    }

    public String getOld_exch_pwd() {
        return old_exch_pwd;
    }

    public void setOld_exch_pwd(String old_exch_pwd) {
        this.old_exch_pwd = old_exch_pwd;
    }

    public String getFund_pwd() {
        return fund_pwd;
    }

    public void setFund_pwd(String fund_pwd) {
        this.fund_pwd = fund_pwd;
    }

    public String getOld_fund_pwd() {
        return old_fund_pwd;
    }

    public void setOld_fund_pwd(String old_fund_pwd) {
        this.old_fund_pwd = old_fund_pwd;
    }
}
