package com.caimao.gjs.server.trade.protocol.sjs.entity.req;

import java.io.Serializable;

public class FSJSQueryFinanceHistoryReq implements Serializable{
    private String start_date; //开始日期
    private String end_date; //结束日期
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
