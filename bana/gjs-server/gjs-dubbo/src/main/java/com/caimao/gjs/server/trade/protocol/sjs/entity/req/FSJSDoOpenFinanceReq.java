package com.caimao.gjs.server.trade.protocol.sjs.entity.req;

import java.io.Serializable;

public class FSJSDoOpenFinanceReq implements Serializable{
    private String acct_no; //客户号
    private String bank_no; //银行代码
    private String bk_mer_code; //商户号
    private String bk_acct_name; //银行客户名称
    private String bk_acct_no; //银行帐号
    private String bk_cert_type; //银行证件类别
    private String bk_cert_no; //银行证件号码
    private String phone_num; //手机号码

    public String getAcct_no() {
        return acct_no;
    }

    public void setAcct_no(String acct_no) {
        this.acct_no = acct_no;
    }

    public String getBank_no() {
        return bank_no;
    }

    public void setBank_no(String bank_no) {
        this.bank_no = bank_no;
    }

    public String getBk_mer_code() {
        return bk_mer_code;
    }

    public void setBk_mer_code(String bk_mer_code) {
        this.bk_mer_code = bk_mer_code;
    }

    public String getBk_acct_name() {
        return bk_acct_name;
    }

    public void setBk_acct_name(String bk_acct_name) {
        this.bk_acct_name = bk_acct_name;
    }

    public String getBk_acct_no() {
        return bk_acct_no;
    }

    public void setBk_acct_no(String bk_acct_no) {
        this.bk_acct_no = bk_acct_no;
    }

    public String getBk_cert_type() {
        return bk_cert_type;
    }

    public void setBk_cert_type(String bk_cert_type) {
        this.bk_cert_type = bk_cert_type;
    }

    public String getBk_cert_no() {
        return bk_cert_no;
    }

    public void setBk_cert_no(String bk_cert_no) {
        this.bk_cert_no = bk_cert_no;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }
}
