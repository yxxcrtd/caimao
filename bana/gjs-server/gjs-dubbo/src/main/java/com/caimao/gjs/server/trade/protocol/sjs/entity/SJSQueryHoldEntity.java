package com.caimao.gjs.server.trade.protocol.sjs.entity;

import java.io.Serializable;

/**
 * 查询持仓
 */
public class SJSQueryHoldEntity implements Serializable {
    private String prod_code; //合约代码
    private String long_amt; //当前多仓
    private String short_amt; //当前空仓
    private String can_use_long; //可用多仓
    private String can_use_short; //可用空仓
    private String day_open_long; //当日开多仓
    private String day_open_short; //当日开空仓
    private String day_cov_long; //当日平多仓
    private String day_cov_short; //当日平空仓
    private String day_deli_long; //当日交割多仓
    private String day_deli_short; //当日交割空仓
    private String day_cov_long_froz; //当日平多仓冻结
    private String day_cov_short_froz; //当日平空仓冻结
    private String deli_long_froz; //当日交割多仓冻结
    private String deli_short_froz; //当日交割空仓冻结
    private String long_open_price; //多头开仓均价
    private String short_open_price; //空头开仓均价
    private String long_posi_price; //多头持仓均价
    private String short_posi_price; //空头持仓均价
    private String long_margin; //多头持仓保证金
    private String short_margin; //空头持仓保证金
    private String last_settle_price; //上日结算价

    public String getProd_code() {
        return prod_code;
    }

    public void setProd_code(String prod_code) {
        this.prod_code = prod_code;
    }

    public String getLong_amt() {
        return long_amt;
    }

    public void setLong_amt(String long_amt) {
        this.long_amt = long_amt;
    }

    public String getShort_amt() {
        return short_amt;
    }

    public void setShort_amt(String short_amt) {
        this.short_amt = short_amt;
    }

    public String getCan_use_long() {
        return can_use_long;
    }

    public void setCan_use_long(String can_use_long) {
        this.can_use_long = can_use_long;
    }

    public String getCan_use_short() {
        return can_use_short;
    }

    public void setCan_use_short(String can_use_short) {
        this.can_use_short = can_use_short;
    }

    public String getDay_open_long() {
        return day_open_long;
    }

    public void setDay_open_long(String day_open_long) {
        this.day_open_long = day_open_long;
    }

    public String getDay_open_short() {
        return day_open_short;
    }

    public void setDay_open_short(String day_open_short) {
        this.day_open_short = day_open_short;
    }

    public String getDay_cov_long() {
        return day_cov_long;
    }

    public void setDay_cov_long(String day_cov_long) {
        this.day_cov_long = day_cov_long;
    }

    public String getDay_cov_short() {
        return day_cov_short;
    }

    public void setDay_cov_short(String day_cov_short) {
        this.day_cov_short = day_cov_short;
    }

    public String getDay_deli_long() {
        return day_deli_long;
    }

    public void setDay_deli_long(String day_deli_long) {
        this.day_deli_long = day_deli_long;
    }

    public String getDay_deli_short() {
        return day_deli_short;
    }

    public void setDay_deli_short(String day_deli_short) {
        this.day_deli_short = day_deli_short;
    }

    public String getDay_cov_long_froz() {
        return day_cov_long_froz;
    }

    public void setDay_cov_long_froz(String day_cov_long_froz) {
        this.day_cov_long_froz = day_cov_long_froz;
    }

    public String getDay_cov_short_froz() {
        return day_cov_short_froz;
    }

    public void setDay_cov_short_froz(String day_cov_short_froz) {
        this.day_cov_short_froz = day_cov_short_froz;
    }

    public String getDeli_long_froz() {
        return deli_long_froz;
    }

    public void setDeli_long_froz(String deli_long_froz) {
        this.deli_long_froz = deli_long_froz;
    }

    public String getDeli_short_froz() {
        return deli_short_froz;
    }

    public void setDeli_short_froz(String deli_short_froz) {
        this.deli_short_froz = deli_short_froz;
    }

    public String getLong_open_price() {
        return long_open_price;
    }

    public void setLong_open_price(String long_open_price) {
        this.long_open_price = long_open_price;
    }

    public String getShort_open_price() {
        return short_open_price;
    }

    public void setShort_open_price(String short_open_price) {
        this.short_open_price = short_open_price;
    }

    public String getLong_posi_price() {
        return long_posi_price;
    }

    public void setLong_posi_price(String long_posi_price) {
        this.long_posi_price = long_posi_price;
    }

    public String getShort_posi_price() {
        return short_posi_price;
    }

    public void setShort_posi_price(String short_posi_price) {
        this.short_posi_price = short_posi_price;
    }

    public String getLong_margin() {
        return long_margin;
    }

    public void setLong_margin(String long_margin) {
        this.long_margin = long_margin;
    }

    public String getShort_margin() {
        return short_margin;
    }

    public void setShort_margin(String short_margin) {
        this.short_margin = short_margin;
    }

    public String getLast_settle_price() {
        return last_settle_price;
    }

    public void setLast_settle_price(String last_settle_price) {
        this.last_settle_price = last_settle_price;
    }
}