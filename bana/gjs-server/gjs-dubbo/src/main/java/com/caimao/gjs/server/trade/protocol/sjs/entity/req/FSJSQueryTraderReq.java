package com.caimao.gjs.server.trade.protocol.sjs.entity.req;

import java.io.Serializable;

public class FSJSQueryTraderReq implements Serializable{
    private String acct_no; //客户号
    private String cert_type_id; //证件类型
    private String cert_num; //证件号码

    public String getAcct_no() {
        return acct_no;
    }

    public void setAcct_no(String acct_no) {
        this.acct_no = acct_no;
    }

    public String getCert_type_id() {
        return cert_type_id;
    }

    public void setCert_type_id(String cert_type_id) {
        this.cert_type_id = cert_type_id;
    }

    public String getCert_num() {
        return cert_num;
    }

    public void setCert_num(String cert_num) {
        this.cert_num = cert_num;
    }
}
