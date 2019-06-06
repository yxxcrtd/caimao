package com.caimao.gjs.server.trade.protocol.sjs.entity;

import java.io.Serializable;

/**
 * 查询委托
 */
public class SJSQueryEntrustEntity implements Serializable {
    private String order_no; //委托单号
    private String entr_date; //委托日期
    private String entr_time; //委托时间
    private String market_id; //交易市场
    private String exch_type; //交易类型
    private String prod_code; //合约代码
    private String entr_amt; //委托手数
    private String entr_price; //委托价格
    private String unmatch_amt; //未成交手数
    private String offset_flag; //开平标志
    private String entr_stat; //委托单状态
    private String cancel_time; //撤消时间
    private String entr_term_type; //委托渠道
    private String cancel_term_type; //撤消渠道
    private String local_order_no; //本地报单号

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getEntr_date() {
        return entr_date;
    }

    public void setEntr_date(String entr_date) {
        this.entr_date = entr_date;
    }

    public String getEntr_time() {
        return entr_time;
    }

    public void setEntr_time(String entr_time) {
        this.entr_time = entr_time;
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

    public String getEntr_amt() {
        return entr_amt;
    }

    public void setEntr_amt(String entr_amt) {
        this.entr_amt = entr_amt;
    }

    public String getEntr_price() {
        return entr_price;
    }

    public void setEntr_price(String entr_price) {
        this.entr_price = entr_price;
    }

    public String getUnmatch_amt() {
        return unmatch_amt;
    }

    public void setUnmatch_amt(String unmatch_amt) {
        this.unmatch_amt = unmatch_amt;
    }

    public String getOffset_flag() {
        return offset_flag;
    }

    public void setOffset_flag(String offset_flag) {
        this.offset_flag = offset_flag;
    }

    public String getEntr_stat() {
        return entr_stat;
    }

    public void setEntr_stat(String entr_stat) {
        this.entr_stat = entr_stat;
    }

    public String getCancel_time() {
        return cancel_time;
    }

    public void setCancel_time(String cancel_time) {
        this.cancel_time = cancel_time;
    }

    public String getEntr_term_type() {
        return entr_term_type;
    }

    public void setEntr_term_type(String entr_term_type) {
        this.entr_term_type = entr_term_type;
    }

    public String getCancel_term_type() {
        return cancel_term_type;
    }

    public void setCancel_term_type(String cancel_term_type) {
        this.cancel_term_type = cancel_term_type;
    }

    public String getLocal_order_no() {
        return local_order_no;
    }

    public void setLocal_order_no(String local_order_no) {
        this.local_order_no = local_order_no;
    }
}