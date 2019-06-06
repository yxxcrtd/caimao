package com.caimao.gjs.server.trade.protocol.sjs.entity.req;

import java.io.Serializable;

public class FSJSDoOpenAccountReq implements Serializable{
    private String acct_no; //客户号
    private String bk_acct_no; //银行账户
    private String cust_name; //客户名称
    private String cert_type_id; //证件类型
    private String cert_num; //证件号码
    private String branch_id; //所属代理机构
    private String grade_id; //所属客户级别
    private String exch_pwd; //交易密码
    private String fund_pwd; //资金密码
    private String area_code; //地区代码
    private String mobile_phone; //手机
    private String tel; //联系电话
    private String addr; //联系地址
    private String zipcode; //邮政编码
    private String email; //电子邮箱
    private String cust_type_id; //所属客户分组
    private String broker_list; //所属客户经理
    private String sms_validatecode; //短信验证码
    private String score_riskevaluation; //风险评估分数

    public String getAcct_no() {
        return acct_no;
    }

    public void setAcct_no(String acct_no) {
        this.acct_no = acct_no;
    }

    public String getBk_acct_no() {
        return bk_acct_no;
    }

    public void setBk_acct_no(String bk_acct_no) {
        this.bk_acct_no = bk_acct_no;
    }

    public String getCust_name() {
        return cust_name;
    }

    public void setCust_name(String cust_name) {
        this.cust_name = cust_name;
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

    public String getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(String branch_id) {
        this.branch_id = branch_id;
    }

    public String getGrade_id() {
        return grade_id;
    }

    public void setGrade_id(String grade_id) {
        this.grade_id = grade_id;
    }

    public String getExch_pwd() {
        return exch_pwd;
    }

    public void setExch_pwd(String exch_pwd) {
        this.exch_pwd = exch_pwd;
    }

    public String getFund_pwd() {
        return fund_pwd;
    }

    public void setFund_pwd(String fund_pwd) {
        this.fund_pwd = fund_pwd;
    }

    public String getArea_code() {
        return area_code;
    }

    public void setArea_code(String area_code) {
        this.area_code = area_code;
    }

    public String getMobile_phone() {
        return mobile_phone;
    }

    public void setMobile_phone(String mobile_phone) {
        this.mobile_phone = mobile_phone;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCust_type_id() {
        return cust_type_id;
    }

    public void setCust_type_id(String cust_type_id) {
        this.cust_type_id = cust_type_id;
    }

    public String getBroker_list() {
        return broker_list;
    }

    public void setBroker_list(String broker_list) {
        this.broker_list = broker_list;
    }

    public String getSms_validatecode() {
        return sms_validatecode;
    }

    public void setSms_validatecode(String sms_validatecode) {
        this.sms_validatecode = sms_validatecode;
    }

    public String getScore_riskevaluation() {
        return score_riskevaluation;
    }

    public void setScore_riskevaluation(String score_riskevaluation) {
        this.score_riskevaluation = score_riskevaluation;
    }
}
