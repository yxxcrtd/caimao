package com.caimao.gjs.server.trade.protocol.sjs.entity;

import java.io.Serializable;

/**
 * 交易登录
 */
public class SJSDoTradeLoginEntity implements Serializable {
    private String user_name; //客户名称
    private String acct_type; //客户类型
    private String member_id; //会员编号
    private String member_name; //会员名称
    private String branch_id; //代理机构编号
    private String branch_name; //代理机构名称
    private String m_sys_stat; //二级系统状态
    private String b_sys_stat; //交易所系统状态
    private String exch_date; //交易日期
    private String sys_date; //系统日期
    private String exch_time; //交易时间
    private String last_exch_date; //上个交易日起

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getAcct_type() {
        return acct_type;
    }

    public void setAcct_type(String acct_type) {
        this.acct_type = acct_type;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(String branch_id) {
        this.branch_id = branch_id;
    }

    public String getBranch_name() {
        return branch_name;
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }

    public String getM_sys_stat() {
        return m_sys_stat;
    }

    public void setM_sys_stat(String m_sys_stat) {
        this.m_sys_stat = m_sys_stat;
    }

    public String getB_sys_stat() {
        return b_sys_stat;
    }

    public void setB_sys_stat(String b_sys_stat) {
        this.b_sys_stat = b_sys_stat;
    }

    public String getExch_date() {
        return exch_date;
    }

    public void setExch_date(String exch_date) {
        this.exch_date = exch_date;
    }

    public String getSys_date() {
        return sys_date;
    }

    public void setSys_date(String sys_date) {
        this.sys_date = sys_date;
    }

    public String getExch_time() {
        return exch_time;
    }

    public void setExch_time(String exch_time) {
        this.exch_time = exch_time;
    }

    public String getLast_exch_date() {
        return last_exch_date;
    }

    public void setLast_exch_date(String last_exch_date) {
        this.last_exch_date = last_exch_date;
    }
}