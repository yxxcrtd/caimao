package com.caimao.gjs.server.trade.protocol.sjs.entity;

import java.io.Serializable;

/**
 * 查询转账记录
 */
public class SJSQueryTransferHistoryEntity implements Serializable {
    private String exch_date; //交易日期;
    private String serial_no; //转帐流水号;
    private String access_way; //存取方向;
    private String exch_bal; //转账金额;
    private String o_term_type; //转账渠道;
    private String bk_plat_date; //银行日期;
    private String bk_seq_no; //银行流水号;
    private String in_account_flag; //是否入账;
    private String check_stat1; //复核状态1;
    private String check_stat2; //复核状态2;
    private String bk_rsp_code; //银行响应代码;
    private String bk_rsp_msg; //银行响应消息;
    private String o_date; //出入金时间;

    public String getExch_date() {
        return exch_date;
    }

    public void setExch_date(String exch_date) {
        this.exch_date = exch_date;
    }

    public String getSerial_no() {
        return serial_no;
    }

    public void setSerial_no(String serial_no) {
        this.serial_no = serial_no;
    }

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

    public String getO_term_type() {
        return o_term_type;
    }

    public void setO_term_type(String o_term_type) {
        this.o_term_type = o_term_type;
    }

    public String getBk_plat_date() {
        return bk_plat_date;
    }

    public void setBk_plat_date(String bk_plat_date) {
        this.bk_plat_date = bk_plat_date;
    }

    public String getBk_seq_no() {
        return bk_seq_no;
    }

    public void setBk_seq_no(String bk_seq_no) {
        this.bk_seq_no = bk_seq_no;
    }

    public String getIn_account_flag() {
        return in_account_flag;
    }

    public void setIn_account_flag(String in_account_flag) {
        this.in_account_flag = in_account_flag;
    }

    public String getCheck_stat1() {
        return check_stat1;
    }

    public void setCheck_stat1(String check_stat1) {
        this.check_stat1 = check_stat1;
    }

    public String getCheck_stat2() {
        return check_stat2;
    }

    public void setCheck_stat2(String check_stat2) {
        this.check_stat2 = check_stat2;
    }

    public String getBk_rsp_code() {
        return bk_rsp_code;
    }

    public void setBk_rsp_code(String bk_rsp_code) {
        this.bk_rsp_code = bk_rsp_code;
    }

    public String getBk_rsp_msg() {
        return bk_rsp_msg;
    }

    public void setBk_rsp_msg(String bk_rsp_msg) {
        this.bk_rsp_msg = bk_rsp_msg;
    }

    public String getO_date() {
        return o_date;
    }

    public void setO_date(String o_date) {
        this.o_date = o_date;
    }
}