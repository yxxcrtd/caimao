package com.caimao.gjs.api.entity.history;

import java.io.Serializable;

/**
 * SJS历史交易
 *
 * Created by yangxinxin@huobi.com on 2015/10/20
 */
public class GjsSJSHistoryTradeEntity implements Serializable {
    private Long id;
    private String trader_id; //交易员编号
    private String exch_date; // 成交日期，YYYYMMDD
    private String exch_time; // 成交时间，HH24MISS
    private String match_no; // 成交单号
    private String order_no; // 报单号
    private String market_id; // 交易市场，常量：b_market_id
    private String prod_code; // 合约代码
    private String exch_type; // 交易类型，常量：exch_code
    private float match_price; // 成交价格
    private int match_amount; // 成交数量
    private String bs; // 买卖方向，常量：b_buyorsell
    private String offset_flag; // 开平标志，常量：b_offset_flag
    private String deli_flag; // 交收标志，常量：b_deli_flag
    private float exch_bal; // 成交金额
    private float exch_fare; // 交易费用
    private float margin; // 保证金
    private String term_type; // 委托渠道，常量：term_type
    private String local_order_no; // 本地报单号，二级系统生成

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTrader_id() {
        return trader_id;
    }

    public void setTrader_id(String trader_id) {
        this.trader_id = trader_id;
    }

    public String getExch_date() {
        return exch_date;
    }

    public void setExch_date(String exch_date) {
        this.exch_date = exch_date;
    }

    public String getExch_time() {
        return exch_time;
    }

    public void setExch_time(String exch_time) {
        this.exch_time = exch_time;
    }

    public String getMatch_no() {
        return match_no;
    }

    public void setMatch_no(String match_no) {
        this.match_no = match_no;
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

    public float getMatch_price() {
        return match_price;
    }

    public void setMatch_price(float match_price) {
        this.match_price = match_price;
    }

    public int getMatch_amount() {
        return match_amount;
    }

    public void setMatch_amount(int match_amount) {
        this.match_amount = match_amount;
    }

    public String getBs() {
        return bs;
    }

    public void setBs(String bs) {
        this.bs = bs;
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

    public float getExch_bal() {
        return exch_bal;
    }

    public void setExch_bal(float exch_bal) {
        this.exch_bal = exch_bal;
    }

    public float getExch_fare() {
        return exch_fare;
    }

    public void setExch_fare(float exch_fare) {
        this.exch_fare = exch_fare;
    }

    public float getMargin() {
        return margin;
    }

    public void setMargin(float margin) {
        this.margin = margin;
    }

    public String getTerm_type() {
        return term_type;
    }

    public void setTerm_type(String term_type) {
        this.term_type = term_type;
    }

    public String getLocal_order_no() {
        return local_order_no;
    }

    public void setLocal_order_no(String local_order_no) {
        this.local_order_no = local_order_no;
    }
}
