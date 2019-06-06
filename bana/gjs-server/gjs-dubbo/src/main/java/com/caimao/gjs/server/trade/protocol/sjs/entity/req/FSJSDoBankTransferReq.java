package com.caimao.gjs.server.trade.protocol.sjs.entity.req;

import java.io.Serializable;

public class FSJSDoBankTransferReq implements Serializable{
    private String access_way; //存取方向
    private String exch_bal; //转账金额
    private String fund_pwd; //资金密码 md5加密的资金密码，弄行用RSA加密

    public String getAccess_way() {
        return access_way;
    }

    public void setAccess_way(String access_way) {
        this.access_way = access_way;
    }

    public String getExch_bal() {
        return exch_bal;
    }

    public void setExch_bal(String exch_bal) {
        this.exch_bal = exch_bal;
    }

    public String getFund_pwd() {
        return fund_pwd;
    }

    public void setFund_pwd(String fund_pwd) {
        this.fund_pwd = fund_pwd;
    }
}
