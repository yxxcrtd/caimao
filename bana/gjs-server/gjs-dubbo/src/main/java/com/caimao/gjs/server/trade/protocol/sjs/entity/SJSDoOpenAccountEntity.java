package com.caimao.gjs.server.trade.protocol.sjs.entity;

import java.io.Serializable;

/**
 * 开户
 */
public class SJSDoOpenAccountEntity implements Serializable {
    private String acct_no; //客户号
    private String sms_validatecode; //短信验证码

    public String getAcct_no() {
        return acct_no;
    }

    public void setAcct_no(String acct_no) {
        this.acct_no = acct_no;
    }

    public String getSms_validatecode() {
        return sms_validatecode;
    }

    public void setSms_validatecode(String sms_validatecode) {
        this.sms_validatecode = sms_validatecode;
    }
}