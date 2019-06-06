package com.caimao.gjs.server.trade.protocol.sjs.entity.req;

import java.io.Serializable;

public class FSJSQueryMatchReq implements Serializable{
    private String prod_code; //合约代码
    private String exch_type; //交易类型
    private String offset_flag; //开平标志
    private String local_order_no; //本地报单号
    private String paginal_num; //每页记录数
    private String curr_page; //当前页

    public String getProd_code() {
        return prod_code;
    }

    public void setProd_code(String prod_code) {
        this.prod_code = prod_code;
    }

    public String getExch_type() {
        return exch_type;
    }

    public void setExch_type(String exch_type) {
        this.exch_type = exch_type;
    }

    public String getOffset_flag() {
        return offset_flag;
    }

    public void setOffset_flag(String offset_flag) {
        this.offset_flag = offset_flag;
    }

    public String getLocal_order_no() {
        return local_order_no;
    }

    public void setLocal_order_no(String local_order_no) {
        this.local_order_no = local_order_no;
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
