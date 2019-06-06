package com.caimao.gjs.server.trade.protocol.sjs.entity.req;

import java.io.Serializable;

public class FSJSQueryEntrustHistoryReq implements Serializable{
    private String start_date; //开始日期
    private String end_date; //结束日期
    private String prod_code; //合约代码
    private String offset_flag; //开平标志
    private String exch_type; //交易类型
    private String paginal_num; //每页记录数
    private String curr_page; //当前页

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getProd_code() {
        return prod_code;
    }

    public void setProd_code(String prod_code) {
        this.prod_code = prod_code;
    }

    public String getOffset_flag() {
        return offset_flag;
    }

    public void setOffset_flag(String offset_flag) {
        this.offset_flag = offset_flag;
    }

    public String getExch_type() {
        return exch_type;
    }

    public void setExch_type(String exch_type) {
        this.exch_type = exch_type;
    }

    public String getPaginal_num() {
        return paginal_num;
    }

    public void setPaginal_num(String paginal_num) {
        this.paginal_num = paginal_num;
    }

    public String getCurr_page() {
        return curr_page;
    }

    public void setCurr_page(String curr_page) {
        this.curr_page = curr_page;
    }
}
