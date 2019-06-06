package com.caimao.gjs.server.trade.protocol.sjs.entity;

import java.io.Serializable;

/**
 * 平仓试算
 */
public class SJSQueryCloseCalculateEntity implements Serializable {
    private String user_id; //客户编号
    private String cust_abbr; //客户简称
    private String prod_code; //合约代码
    private String remain_long; //多仓剩余数量
    private String remain_short; //空仓剩余数量
    private String cov_exch_fare; //平仓手续费
    private String cov_surplus; //平仓盈亏
    private String can_use_bal; //可用余额

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCust_abbr() {
        return cust_abbr;
    }

    public void setCust_abbr(String cust_abbr) {
        this.cust_abbr = cust_abbr;
    }

    public String getProd_code() {
        return prod_code;
    }

    public void setProd_code(String prod_code) {
        this.prod_code = prod_code;
    }

    public String getRemain_long() {
        return remain_long;
    }

    public void setRemain_long(String remain_long) {
        this.remain_long = remain_long;
    }

    public String getRemain_short() {
        return remain_short;
    }

    public void setRemain_short(String remain_short) {
        this.remain_short = remain_short;
    }

    public String getCov_exch_fare() {
        return cov_exch_fare;
    }

    public void setCov_exch_fare(String cov_exch_fare) {
        this.cov_exch_fare = cov_exch_fare;
    }

    public String getCov_surplus() {
        return cov_surplus;
    }

    public void setCov_surplus(String cov_surplus) {
        this.cov_surplus = cov_surplus;
    }

    public String getCan_use_bal() {
        return can_use_bal;
    }

    public void setCan_use_bal(String can_use_bal) {
        this.can_use_bal = can_use_bal;
    }
}