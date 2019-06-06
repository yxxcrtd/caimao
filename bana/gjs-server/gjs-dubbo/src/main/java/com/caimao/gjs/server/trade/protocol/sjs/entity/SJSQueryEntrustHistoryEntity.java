package com.caimao.gjs.server.trade.protocol.sjs.entity;

import java.io.Serializable;

/**
 * 查询历史委托
 */
public class SJSQueryEntrustHistoryEntity implements Serializable {
    private String entr_date; //委托日期
    private String order_no; //报单号
    private String market_id; //交易市场
    private String exch_type; //交易类型
    private String prod_code; //合约代码
    private String entr_price; //委托价格
    private String entr_amount; //委托数量
    private String remain_amount; //剩余数量
    private String offset_flag; //开平标志
    private String deli_flag; //交收标志
    private String bs; //买卖方向
    private String entr_stat; //委托状态
    private String cancel_flag; //撤单标志
    private String accept_time; //应答时间
    private String e_term_type; //委托渠道
    private String e_exch_time; //委托时间
    private String c_term_type; //撤消渠道
    private String c_exch_time; //撤销时间
    private String local_order_no; //本地报单号

    public String getEntr_date() {
        return entr_date;
    }

    public void setEntr_date(String entr_date) {
        this.entr_date = entr_date;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getMarket_id() {
        return market_id;
    }

    public void setMarket_id(String market_id) {
        this.market_id = market_id;
    }

    public String getExch_type() {
        return exch_type;
    }

    public void setExch_type(String exch_type) {
        this.exch_type = exch_type;
    }

    public String getProd_code() {
        return prod_code;
    }

    public void setProd_code(String prod_code) {
        this.prod_code = prod_code;
    }

    public String getEntr_price() {
        return entr_price;
    }

    public void setEntr_price(String entr_price) {
        this.entr_price = entr_price;
    }

    public String getEntr_amount() {
        return entr_amount;
    }

    public void setEntr_amount(String entr_amount) {
        this.entr_amount = entr_amount;
    }

    public String getRemain_amount() {
        return remain_amount;
    }

    public void setRemain_amount(String remain_amount) {
        this.remain_amount = remain_amount;
    }

    public String getOffset_flag() {
        return offset_flag;
    }

    public void setOffset_flag(String offset_flag) {
        this.offset_flag = offset_flag;
    }

    public String getDeli_flag() {
        return deli_flag;
    }

    public void setDeli_flag(String deli_flag) {
        this.deli_flag = deli_flag;
    }

    public String getBs() {
        return bs;
    }

    public void setBs(String bs) {
        this.bs = bs;
    }

    public String getEntr_stat() {
        return entr_stat;
    }

    public void setEntr_stat(String entr_stat) {
        this.entr_stat = entr_stat;
    }

    public String getCancel_flag() {
        return cancel_flag;
    }

    public void setCancel_flag(String cancel_flag) {
        this.cancel_flag = cancel_flag;
    }

    public String getAccept_time() {
        return accept_time;
    }

    public void setAccept_time(String accept_time) {
        this.accept_time = accept_time;
    }

    public String getE_term_type() {
        return e_term_type;
    }

    public void setE_term_type(String e_term_type) {
        this.e_term_type = e_term_type;
    }

    public String getE_exch_time() {
        return e_exch_time;
    }

    public void setE_exch_time(String e_exch_time) {
        this.e_exch_time = e_exch_time;
    }

    public String getC_term_type() {
        return c_term_type;
    }

    public void setC_term_type(String c_term_type) {
        this.c_term_type = c_term_type;
    }

    public String getC_exch_time() {
        return c_exch_time;
    }

    public void setC_exch_time(String c_exch_time) {
        this.c_exch_time = c_exch_time;
    }

    public String getLocal_order_no() {
        return local_order_no;
    }

    public void setLocal_order_no(String local_order_no) {
        this.local_order_no = local_order_no;
    }
}