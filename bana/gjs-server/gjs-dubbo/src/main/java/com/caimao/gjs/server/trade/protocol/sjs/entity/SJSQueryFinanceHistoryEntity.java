package com.caimao.gjs.server.trade.protocol.sjs.entity;

import java.io.Serializable;

/**
 * 资金历史
 */
public class SJSQueryFinanceHistoryEntity implements Serializable {
    private String exch_date; //交易日期;
    private String last_bal; //上日余额;
    private String last_can_use; //上日可用;
    private String curr_bal; //当日余额;
    private String curr_can_use; //当日可用;
    private String curr_can_get; //当日可提;
    private String last_margin; //上日保证金;
    private String last_reserve; //上日溢短备付金;
    private String out_bal; //当日岀金;
    private String in_bal; //当日入金;
    private String real_buy; //成交买入金额;
    private String real_sell; //成交卖出金额;
    private String real_reserve; //成交溢短备付金;
    private String real_margin; //成交保证金;
    private String base_margin; //当前基础保证金;
    private String last_base_margin; //上日基础保证金;
    private String deli_prepare; //当日交割准备金;
    private String last_deli_prepare; //上日交割准备金;
    private String deli_margin; //当日交割保证金;
    private String last_deli_margin; //上日交割保证金;
    private String real_exch_fare; //当日交易费用;
    private String other_fare; //其他费用;
    private String pay_breach; //支付违约金;
    private String take_breach; //收到违约金;
    private String cov_surplus; //平仓盈余;
    private String mark_surplus; //盯市盈余;
    private String float_surplus; //浮动盈余;
    private String last_long_froz; //上日冻结资金;
    private String day_long_froz; //当日冻结资金;
    private String last_forward_froz; //上日远期盈亏冻结;
    private String day_forward_froz; //当日远期盈亏冻结;
    private String inte_integral; //利息积数 ;
    private String puni_integral; //罚息积数;
    private String wait_incr_inte; //待入账利息;
    private String wait_incr_inte_tax; //待入账利息税;
    private String day_incr_inte; //当日入账利息;
    private String day_incr_inte_tax; //当日入账利息税;
    private String last_take_margin; //上日提货保证金冻结;
    private String day_take_margin; //当日提货保证金冻结;
    private String last_stor_fare_froz; //上日仓储费冻结;
    private String day_stor_fare_froz; //当日仓储费冻结;

    public String getExch_date() {
        return exch_date;
    }

    public void setExch_date(String exch_date) {
        this.exch_date = exch_date;
    }

    public String getLast_bal() {
        return last_bal;
    }

    public void setLast_bal(String last_bal) {
        this.last_bal = last_bal;
    }

    public String getLast_can_use() {
        return last_can_use;
    }

    public void setLast_can_use(String last_can_use) {
        this.last_can_use = last_can_use;
    }

    public String getCurr_bal() {
        return curr_bal;
    }

    public void setCurr_bal(String curr_bal) {
        this.curr_bal = curr_bal;
    }

    public String getCurr_can_use() {
        return curr_can_use;
    }

    public void setCurr_can_use(String curr_can_use) {
        this.curr_can_use = curr_can_use;
    }

    public String getCurr_can_get() {
        return curr_can_get;
    }

    public void setCurr_can_get(String curr_can_get) {
        this.curr_can_get = curr_can_get;
    }

    public String getLast_margin() {
        return last_margin;
    }

    public void setLast_margin(String last_margin) {
        this.last_margin = last_margin;
    }

    public String getLast_reserve() {
        return last_reserve;
    }

    public void setLast_reserve(String last_reserve) {
        this.last_reserve = last_reserve;
    }

    public String getOut_bal() {
        return out_bal;
    }

    public void setOut_bal(String out_bal) {
        this.out_bal = out_bal;
    }

    public String getIn_bal() {
        return in_bal;
    }

    public void setIn_bal(String in_bal) {
        this.in_bal = in_bal;
    }

    public String getReal_buy() {
        return real_buy;
    }

    public void setReal_buy(String real_buy) {
        this.real_buy = real_buy;
    }

    public String getReal_sell() {
        return real_sell;
    }

    public void setReal_sell(String real_sell) {
        this.real_sell = real_sell;
    }

    public String getReal_reserve() {
        return real_reserve;
    }

    public void setReal_reserve(String real_reserve) {
        this.real_reserve = real_reserve;
    }

    public String getReal_margin() {
        return real_margin;
    }

    public void setReal_margin(String real_margin) {
        this.real_margin = real_margin;
    }

    public String getBase_margin() {
        return base_margin;
    }

    public void setBase_margin(String base_margin) {
        this.base_margin = base_margin;
    }

    public String getLast_base_margin() {
        return last_base_margin;
    }

    public void setLast_base_margin(String last_base_margin) {
        this.last_base_margin = last_base_margin;
    }

    public String getDeli_prepare() {
        return deli_prepare;
    }

    public void setDeli_prepare(String deli_prepare) {
        this.deli_prepare = deli_prepare;
    }

    public String getLast_deli_prepare() {
        return last_deli_prepare;
    }

    public void setLast_deli_prepare(String last_deli_prepare) {
        this.last_deli_prepare = last_deli_prepare;
    }

    public String getDeli_margin() {
        return deli_margin;
    }

    public void setDeli_margin(String deli_margin) {
        this.deli_margin = deli_margin;
    }

    public String getLast_deli_margin() {
        return last_deli_margin;
    }

    public void setLast_deli_margin(String last_deli_margin) {
        this.last_deli_margin = last_deli_margin;
    }

    public String getReal_exch_fare() {
        return real_exch_fare;
    }

    public void setReal_exch_fare(String real_exch_fare) {
        this.real_exch_fare = real_exch_fare;
    }

    public String getOther_fare() {
        return other_fare;
    }

    public void setOther_fare(String other_fare) {
        this.other_fare = other_fare;
    }

    public String getPay_breach() {
        return pay_breach;
    }

    public void setPay_breach(String pay_breach) {
        this.pay_breach = pay_breach;
    }

    public String getTake_breach() {
        return take_breach;
    }

    public void setTake_breach(String take_breach) {
        this.take_breach = take_breach;
    }

    public String getCov_surplus() {
        return cov_surplus;
    }

    public void setCov_surplus(String cov_surplus) {
        this.cov_surplus = cov_surplus;
    }

    public String getMark_surplus() {
        return mark_surplus;
    }

    public void setMark_surplus(String mark_surplus) {
        this.mark_surplus = mark_surplus;
    }

    public String getFloat_surplus() {
        return float_surplus;
    }

    public void setFloat_surplus(String float_surplus) {
        this.float_surplus = float_surplus;
    }

    public String getLast_long_froz() {
        return last_long_froz;
    }

    public void setLast_long_froz(String last_long_froz) {
        this.last_long_froz = last_long_froz;
    }

    public String getDay_long_froz() {
        return day_long_froz;
    }

    public void setDay_long_froz(String day_long_froz) {
        this.day_long_froz = day_long_froz;
    }

    public String getLast_forward_froz() {
        return last_forward_froz;
    }

    public void setLast_forward_froz(String last_forward_froz) {
        this.last_forward_froz = last_forward_froz;
    }

    public String getDay_forward_froz() {
        return day_forward_froz;
    }

    public void setDay_forward_froz(String day_forward_froz) {
        this.day_forward_froz = day_forward_froz;
    }

    public String getInte_integral() {
        return inte_integral;
    }

    public void setInte_integral(String inte_integral) {
        this.inte_integral = inte_integral;
    }

    public String getPuni_integral() {
        return puni_integral;
    }

    public void setPuni_integral(String puni_integral) {
        this.puni_integral = puni_integral;
    }

    public String getWait_incr_inte() {
        return wait_incr_inte;
    }

    public void setWait_incr_inte(String wait_incr_inte) {
        this.wait_incr_inte = wait_incr_inte;
    }

    public String getWait_incr_inte_tax() {
        return wait_incr_inte_tax;
    }

    public void setWait_incr_inte_tax(String wait_incr_inte_tax) {
        this.wait_incr_inte_tax = wait_incr_inte_tax;
    }

    public String getDay_incr_inte() {
        return day_incr_inte;
    }

    public void setDay_incr_inte(String day_incr_inte) {
        this.day_incr_inte = day_incr_inte;
    }

    public String getDay_incr_inte_tax() {
        return day_incr_inte_tax;
    }

    public void setDay_incr_inte_tax(String day_incr_inte_tax) {
        this.day_incr_inte_tax = day_incr_inte_tax;
    }

    public String getLast_take_margin() {
        return last_take_margin;
    }

    public void setLast_take_margin(String last_take_margin) {
        this.last_take_margin = last_take_margin;
    }

    public String getDay_take_margin() {
        return day_take_margin;
    }

    public void setDay_take_margin(String day_take_margin) {
        this.day_take_margin = day_take_margin;
    }

    public String getLast_stor_fare_froz() {
        return last_stor_fare_froz;
    }

    public void setLast_stor_fare_froz(String last_stor_fare_froz) {
        this.last_stor_fare_froz = last_stor_fare_froz;
    }

    public String getDay_stor_fare_froz() {
        return day_stor_fare_froz;
    }

    public void setDay_stor_fare_froz(String day_stor_fare_froz) {
        this.day_stor_fare_froz = day_stor_fare_froz;
    }
}